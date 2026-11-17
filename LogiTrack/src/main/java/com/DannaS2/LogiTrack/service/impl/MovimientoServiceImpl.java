package com.DannaS2.LogiTrack.service.impl;

import com.DannaS2.LogiTrack.dto.request.MovimientoDtoRequest;
import com.DannaS2.LogiTrack.dto.response.MovimientoDtoResponse;
import com.DannaS2.LogiTrack.exception.BusinessRuleException;
import com.DannaS2.LogiTrack.exception.RecursoNoEncontradoException;
import com.DannaS2.LogiTrack.mapper.DetalleMovimientoMapper;
import com.DannaS2.LogiTrack.mapper.MovimientoMapper;
import com.DannaS2.LogiTrack.model.*;
import com.DannaS2.LogiTrack.repository.*;
import com.DannaS2.LogiTrack.service.AuditoriaService;
import com.DannaS2.LogiTrack.service.MovimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final PersonaRepository personaRepository;
    private final BodegaRepository bodegaRepository;
    private final ProductoRepository productoRepository;
    private final InventarioBodegaRepository inventarioRepository;
    private final MovimientoMapper movimientoMapper;
    private final DetalleMovimientoMapper detalleMapper;
    private final AuditoriaService auditoriaService;

    @Override
    @Transactional
    public MovimientoDtoResponse guardar(MovimientoDtoRequest dto) {
        Persona persona = personaRepository.findById(dto.personaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Persona no encontrada: " + dto.personaId()));
        Bodega origen = obtenerBodega(dto.bodegaOrigenId());
        Bodega destino = obtenerBodega(dto.bodegaDestinoId());

        List<DetalleMovimiento> detalles = dto.detalles().stream()
                .map(d -> detalleMapper.DtoToEntity(d, obtenerProducto(d.productoId())))
                .toList();

        for (DetalleMovimiento d : detalles) {
            aplicarAlInventario(dto.tipoMovimiento(), origen, destino, d.getProducto(), d.getCantidad());
        }

        Movimiento movimiento = movimientoMapper.DtoToEntity(dto, persona, origen, destino, detalles);
        Movimiento guardado = movimientoRepository.save(movimiento);
        MovimientoDtoResponse response = movimientoMapper.entityToDto(guardado);
        auditoriaService.registrar(com.DannaS2.LogiTrack.model.TipoOperacion.INSERT, "MOVIMIENTO", guardado.getId(), null, response);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoDtoResponse> listar() {
        return movimientoRepository.findAll().stream().map(movimientoMapper::entityToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MovimientoDtoResponse buscarId(Long id) {
        return movimientoRepository.findById(id).map(movimientoMapper::entityToDto)
                .orElseThrow(() -> new RecursoNoEncontradoException("Movimiento no encontrado: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoDtoResponse> buscarTipo(TipoMovimiento tipo) {
        return movimientoRepository.findByTipo(tipo).stream().map(movimientoMapper::entityToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoDtoResponse> buscarPersona(Long personaId) {
        return movimientoRepository.findByPersonaId(personaId).stream().map(movimientoMapper::entityToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoDtoResponse> buscarBodegaOrigen(Long bodegaId) {
        return movimientoRepository.findByBodegaOrigenId(bodegaId).stream().map(movimientoMapper::entityToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoDtoResponse> buscarBodegaDestino(Long bodegaId) {
        return movimientoRepository.findByBodegaDestinoId(bodegaId).stream().map(movimientoMapper::entityToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoDtoResponse> buscarFechas(LocalDateTime desde, LocalDateTime hasta) {
        return movimientoRepository.findByFechaBetween(desde, hasta).stream().map(movimientoMapper::entityToDto).toList();
    }

    // ---------- inventario ----------

    private void aplicarAlInventario(TipoMovimiento tipo, Bodega origen, Bodega destino,
                                     Producto producto, int cantidad) {
        switch (tipo) {
            case ENTRADA -> sumar(destino, producto, cantidad);
            case SALIDA -> restar(origen, producto, cantidad);
            case TRANSFERENCIA -> {
                restar(origen, producto, cantidad);
                sumar(destino, producto, cantidad);
            }
        }
    }

    private void sumar(Bodega bodega, Producto producto, int cantidad) {
        InventarioBodega inv = inventarioRepository
                .findByBodegaIdAndProductoId(bodega.getId(), producto.getId())
                .orElseGet(() -> {
                    InventarioBodega nuevo = new InventarioBodega();
                    nuevo.setBodega(bodega);
                    nuevo.setProducto(producto);
                    return nuevo;   // cantidad ya inicia en 0
                });
        inv.setCantidad(inv.getCantidad() + cantidad);
        inventarioRepository.save(inv);
    }

    private void restar(Bodega bodega, Producto producto, int cantidad) {
        InventarioBodega inv = inventarioRepository
                .findByBodegaIdAndProductoId(bodega.getId(), producto.getId())
                .orElseThrow(() -> new BusinessRuleException(
                        "La bodega " + bodega.getNombre() + " no tiene inventario de " + producto.getNombre()));
        if (inv.getCantidad() < cantidad) {
            throw new BusinessRuleException("Stock insuficiente de " + producto.getNombre()
                    + " en " + bodega.getNombre() + ": disponible " + inv.getCantidad()
                    + ", solicitado " + cantidad);
        }
        inv.setCantidad(inv.getCantidad() - cantidad);
        inventarioRepository.save(inv);
    }

    // ---------- búsquedas ----------

    // Las bodegas son opcionales según el tipo (null en ENTRADA origen, SALIDA destino)
    private Bodega obtenerBodega(Long id) {
        if (id == null) return null;
        return bodegaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Bodega no encontrada: " + id));
    }

    private Producto obtenerProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado: " + id));
    }
}