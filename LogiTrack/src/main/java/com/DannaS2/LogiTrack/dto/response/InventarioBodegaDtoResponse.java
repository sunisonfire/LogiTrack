package com.DannaS2.LogiTrack.dto.response;

public record InventarioBodegaDtoResponse(
        Long id,
        Long bodegaId,
        String bodegaNombre,
        Long productoId,
        String productoNombre,
        Integer cantidad
) {
}
