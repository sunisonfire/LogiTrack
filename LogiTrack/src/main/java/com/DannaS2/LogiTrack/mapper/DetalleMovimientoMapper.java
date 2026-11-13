package com.DannaS2.LogiTrack.mapper;

import com.DannaS2.LogiTrack.dto.request.DetalleMovimientoDtoRequest;
import com.DannaS2.LogiTrack.dto.response.DetalleMovimientoDtoResponse;
import com.DannaS2.LogiTrack.model.DetalleMovimiento;
import com.DannaS2.LogiTrack.model.Producto;
import org.springframework.stereotype.Component;

@Component
public class DetalleMovimientoMapper {

    public DetalleMovimientoDtoResponse entityToDto(DetalleMovimiento detalle) {
        if (detalle == null) return null;
        return new DetalleMovimientoDtoResponse(
                detalle.getId(),
                detalle.getMovimiento() != null ? detalle.getMovimiento().getId() : null,
                detalle.getProducto() != null ? detalle.getProducto().getId() : null,
                detalle.getProducto() != null ? detalle.getProducto().getNombre() : null,
                detalle.getCantidad()
        );
    }

    // El movimiento NO se asigna aquí: lo hace Movimiento.agregarDetalle()
    public DetalleMovimiento DtoToEntity(DetalleMovimientoDtoRequest dto, Producto producto) {
        if (dto == null) return null;
        DetalleMovimiento d = new DetalleMovimiento();
        d.setProducto(producto);
        d.setCantidad(dto.cantidad());
        return d;
    }
}
