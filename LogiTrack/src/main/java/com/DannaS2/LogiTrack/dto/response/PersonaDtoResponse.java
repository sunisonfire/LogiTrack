package com.DannaS2.LogiTrack.dto.response;

import com.DannaS2.LogiTrack.model.TipoPersona;

public record PersonaDtoResponse(
        Long id,
        String nombre,
        String apellido,
        String documento,
        TipoPersona tipoPersona
) {
}
