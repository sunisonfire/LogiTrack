package com.DannaS2.LogiTrack.dto.response;

public record DetalleMovimientoDtoResponse(
        Long id,
        Long movimientoId,
        Long productoId,
        String productoNombre,
        Integer cantidad
) {
}
