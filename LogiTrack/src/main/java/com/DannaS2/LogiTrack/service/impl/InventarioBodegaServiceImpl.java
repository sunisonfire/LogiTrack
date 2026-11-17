package com.DannaS2.LogiTrack.service.impl;

import com.DannaS2.LogiTrack.dto.request.InventarioBodegaDtoRequest;
import com.DannaS2.LogiTrack.dto.response.InventarioBodegaDtoResponse;
import com.DannaS2.LogiTrack.exception.BusinessRuleException;
import com.DannaS2.LogiTrack.exception.RecursoDuplicadoException;
import com.DannaS2.LogiTrack.exception.RecursoNoEncontradoException;
import com.DannaS2.LogiTrack.mapper.InventarioBodegaMapper;
import com.DannaS2.LogiTrack.model.Bodega;
import com.DannaS2.LogiTrack.model.InventarioBodega;
import com.DannaS2.LogiTrack.model.Producto;
import com.DannaS2.LogiTrack.repository.BodegaRepository;
import com.DannaS2.LogiTrack.repository.InventarioBodegaRepository;
import com.DannaS2.LogiTrack.repository.ProductoRepository;
import com.DannaS2.LogiTrack.service.AuditoriaService;
import com.DannaS2.LogiTrack.service.InventarioBodegaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventarioBodegaServiceImpl implements InventarioBodegaService {

    private final InventarioBodegaRepository inventarioRepository;
    private final BodegaRepository bodegaRepository;
    private final ProductoRepository productoRepository;
    private final InventarioBodegaMapper inventarioMapper;
    private final AuditoriaService auditoriaService;

    @Override
    @Transactional
    public InventarioBodegaDtoResponse guardar(InventarioBodegaDtoRequest dto) {
        Bodega bodega = obtenerBodega(dto.bodegaId());
        Producto producto = obtenerProducto(dto.productoId());
        if (inventarioRepository.existsByBodegaIdAndProductoId(bodega.getId(), producto.getId())) {
            throw new RecursoDuplicadoException("Ese producto ya tiene inventario en esa bodega");
        }
        InventarioBodega inv = inventarioMapper.DtoToEntity(dto, bodega, producto);
        InventarioBodega guardado = inventarioRepository.save(inv);
        InventarioBodegaDtoResponse response = inventarioMapper.entityToDto(guardado);
        auditoriaService.registrar(com.DannaS2.LogiTrack.model.TipoOperacion.INSERT, "INVENTARIO_BODEGA", guardado.getId(), null, response);
        return response;
    }

    @Override
    @Transactional
    public InventarioBodegaDtoResponse actualizar(InventarioBodegaDtoRequest dto, Long id) {
        InventarioBodega inv = buscarEntidad(id);
        // Bodega y producto son la identidad del registro: solo se corrige la cantidad
        if (!inv.getBodega().getId().equals(dto.bodegaId())
                || !inv.getProducto().getId().equals(dto.productoId())) {
            throw new BusinessRuleException("No se puede cambiar la bodega ni el producto de un inventario");
        }
        InventarioBodegaDtoResponse anterior = inventarioMapper.entityToDto(inv);
        inventarioMapper.updateEntityToDto(inv, dto);
        InventarioBodegaDtoResponse actualizado = inventarioMapper.entityToDto(inv);
        auditoriaService.registrar(com.DannaS2.LogiTrack.model.TipoOperacion.UPDATE, "INVENTARIO_BODEGA", inv.getId(), anterior, actualizado);
        return actualizado;
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        InventarioBodega inv = buscarEntidad(id);
        InventarioBodegaDtoResponse anterior = inventarioMapper.entityToDto(inv);
        inventarioRepository.delete(inv);
        auditoriaService.registrar(com.DannaS2.LogiTrack.model.TipoOperacion.DELETE, "INVENTARIO_BODEGA", id, anterior, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioBodegaDtoResponse> listar() {
        return inventarioRepository.findAll().stream().map(inventarioMapper::entityToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public InventarioBodegaDtoResponse buscarId(Long id) {
        return inventarioMapper.entityToDto(buscarEntidad(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioBodegaDtoResponse> buscarBodega(Long bodegaId) {
        return inventarioRepository.findByBodegaId(bodegaId).stream().map(inventarioMapper::entityToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioBodegaDtoResponse> buscarProducto(Long productoId) {
        return inventarioRepository.findByProductoId(productoId).stream().map(inventarioMapper::entityToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioBodegaDtoResponse> buscarStockBajo(Integer limite) {
        return inventarioRepository.findByCantidadLessThan(limite).stream().map(inventarioMapper::entityToDto).toList();
    }

    private InventarioBodega buscarEntidad(Long id) {
        return inventarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Inventario no encontrado: " + id));
    }

    private Bodega obtenerBodega(Long id) {
        return bodegaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Bodega no encontrada: " + id));
    }

    private Producto obtenerProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado: " + id));
    }
}