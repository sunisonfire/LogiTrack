package com.DannaS2.LogiTrack.dto.response;

import com.DannaS2.LogiTrack.model.TipoPersona;

public record EmpleadoDtoResponse(
        Long id,
        String nombre,
        String apellido,
        String documento,
        TipoPersona tipoPersona,
        String username,
        Long bodegaId,
        String bodegaNombre
) {
}
