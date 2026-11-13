package com.DannaS2.LogiTrack.dto.response;

import com.DannaS2.LogiTrack.model.TipoPersona;

public record EmpleadoDtoResponse(
        Long id,
        PersonaDtoResponse persona,
        String username,
        Long bodegaId,
        String bodegaNombre
) {
}
