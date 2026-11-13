package com.DannaS2.LogiTrack.dto.response;

import com.DannaS2.LogiTrack.model.Categoria;

import java.math.BigDecimal;

public record ProductoDtoResponse(
        Long id,
        String nombre,
        String descripcion,
        BigDecimal precio,
        Categoria categoria,
        Long marcaId,
        String marcaNombre
) {
}
