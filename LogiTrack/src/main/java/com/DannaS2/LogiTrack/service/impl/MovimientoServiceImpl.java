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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final AdministradorRepository administradorRepository;
    private final EmpleadoRepository empleadoRepository;
    private final BodegaRepository bodegaRepository;
    private final ProductoRepository productoRepository;
    private final InventarioBodegaRepository inventarioRepository;
    private final MovimientoMapper movimientoMapper;
    private final DetalleMovimientoMapper detalleMapper;
    private final AuditoriaService auditoriaService;

    @Override
    @Transactional
    public MovimientoDtoResponse guardar(MovimientoDtoRequest dto) {

        // Obtener automáticamente al usuario autenticado.
        Persona persona = obtenerUsuarioAutenticado();

        // Resolver las bodegas indicadas.
        Bodega origen = obtenerBodega(dto.bodegaOrigenId());
        Bodega destino = obtenerBodega(dto.bodegaDestinoId());

        // Resolver los productos y construir los detalles.
        List<DetalleMovimiento> detalles = dto.detalles().stream()
                .map(detalle -> detalleMapper.DtoToEntity(
                        detalle,
                        obtenerProducto(detalle.productoId())
                ))
                .toList();

        // Aplicar los cambios al inventario.
        for (DetalleMovimiento detalle : detalles) {
            aplicarAlInventario(
                    dto.tipoMovimiento(),
                    origen,
                    destino,
                    detalle.getProducto(),
                    detalle.getCantidad()
            );
        }

        // Crear el movimiento con el usuario obtenido del JWT.
        Movimiento movimiento = movimientoMapper.DtoToEntity(
                dto,
                persona,
                origen,
                destino,
                detalles
        );

        Movimiento guardado = movimientoRepository.save(movimiento);

        MovimientoDtoResponse response =
                movimientoMapper.entityToDto(guardado);

        // Registrar la operación en auditoría.
        Map<String, Object> estadoMovimiento = crearEstadoMovimiento(response);

        auditoriaService.registrar(
                TipoOperacion.INSERT,
                "MOVIMIENTO",
                guardado.getId(),
                null,
                estadoMovimiento
        );

        return response;
    }

    private Persona obtenerUsuarioAutenticado() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {

            throw new BusinessRuleException(
                    "No se pudo identificar al usuario autenticado"
            );
        }

        String username = authentication.getName();

        boolean esAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean esEmpleado = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_EMPLEADO"));

        if (esAdmin) {
            return administradorRepository.findByUsername(username)
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No se encontró el administrador autenticado"
                    ));
        }

        if (esEmpleado) {
            return empleadoRepository.findByUsername(username)
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No se encontró el empleado autenticado"
                    ));
        }

        throw new BusinessRuleException(
                "El usuario no tiene un rol válido para registrar movimientos"
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoDtoResponse> listar() {
        return movimientoRepository.findAll()
                .stream()
                .map(movimientoMapper::entityToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MovimientoDtoResponse buscarId(Long id) {
        return movimientoRepository.findById(id)
                .map(movimientoMapper::entityToDto)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Movimiento no encontrado: " + id
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoDtoResponse> buscarTipo(TipoMovimiento tipo) {
        return movimientoRepository.findByTipo(tipo)
                .stream()
                .map(movimientoMapper::entityToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoDtoResponse> buscarPersona(Long personaId) {
        return movimientoRepository.findByPersonaId(personaId)
                .stream()
                .map(movimientoMapper::entityToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoDtoResponse> buscarBodegaOrigen(Long bodegaId) {
        return movimientoRepository.findByBodegaOrigenId(bodegaId)
                .stream()
                .map(movimientoMapper::entityToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoDtoResponse> buscarBodegaDestino(Long bodegaId) {
        return movimientoRepository.findByBodegaDestinoId(bodegaId)
                .stream()
                .map(movimientoMapper::entityToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoDtoResponse> buscarFechas(
            LocalDateTime desde,
            LocalDateTime hasta
    ) {

        if (desde == null || hasta == null || desde.isAfter(hasta)) {
            throw new BusinessRuleException(
                    "El rango de fechas no es válido"
            );
        }

        return movimientoRepository.findByFechaBetween(desde, hasta)
                .stream()
                .map(movimientoMapper::entityToDto)
                .toList();
    }

    // ---------------- INVENTARIO ----------------

    private void aplicarAlInventario(
            TipoMovimiento tipo,
            Bodega origen,
            Bodega destino,
            Producto producto,
            int cantidad
    ) {

        if (cantidad <= 0) {
            throw new BusinessRuleException(
                    "La cantidad debe ser mayor que cero"
            );
        }

        switch (tipo) {

            case ENTRADA ->
                    sumar(destino, producto, cantidad);

            case SALIDA ->
                    restar(origen, producto, cantidad);

            case TRANSFERENCIA -> {
                restar(origen, producto, cantidad);
                sumar(destino, producto, cantidad);
            }
        }
    }

    private void sumar(
            Bodega bodega,
            Producto producto,
            int cantidad
    ) {

        InventarioBodega inventario = inventarioRepository
                .findByBodegaIdAndProductoId(
                        bodega.getId(),
                        producto.getId()
                )
                .orElseGet(() -> {
                    InventarioBodega nuevo = new InventarioBodega();
                    nuevo.setBodega(bodega);
                    nuevo.setProducto(producto);
                    nuevo.setCantidad(0);
                    return nuevo;
                });

        inventario.setCantidad(inventario.getCantidad() + cantidad);

        inventarioRepository.save(inventario);
    }

    private void restar(
            Bodega bodega,
            Producto producto,
            int cantidad
    ) {

        InventarioBodega inventario = inventarioRepository
                .findByBodegaIdAndProductoId(
                        bodega.getId(),
                        producto.getId()
                )
                .orElseThrow(() -> new BusinessRuleException(
                        "La bodega " + bodega.getNombre()
                                + " no tiene inventario de "
                                + producto.getNombre()
                ));

        if (inventario.getCantidad() < cantidad) {
            throw new BusinessRuleException(
                    "Stock insuficiente de " + producto.getNombre()
                            + " en " + bodega.getNombre()
                            + ". Disponible: " + inventario.getCantidad()
                            + ", solicitado: " + cantidad
            );
        }

        inventario.setCantidad(inventario.getCantidad() - cantidad);

        inventarioRepository.save(inventario);
    }

    // ---------------- BÚSQUEDAS Y AUDITORÍA ----------------

    private Bodega obtenerBodega(Long id) {

        if (id == null) {
            return null;
        }

        return bodegaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Bodega no encontrada: " + id
                ));
    }

    private Producto obtenerProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Producto no encontrado: " + id
                ));
    }

    private Map<String, Object> crearEstadoMovimiento(
            MovimientoDtoResponse response
    ) {

        Map<String, Object> estadoMovimiento = new LinkedHashMap<>();

        estadoMovimiento.put("id", response.id());
        estadoMovimiento.put("fecha", response.fecha());
        estadoMovimiento.put("tipoMovimiento", response.tipoMovimiento());
        estadoMovimiento.put("personaId", response.personaId());
        estadoMovimiento.put("personaNombre", response.personaNombre());
        estadoMovimiento.put("bodegaOrigenId", response.bodegaOrigenId());
        estadoMovimiento.put("bodegaDestinoId", response.bodegaDestinoId());
        estadoMovimiento.put("detalles", response.detalles());

        return estadoMovimiento;
    }
}
