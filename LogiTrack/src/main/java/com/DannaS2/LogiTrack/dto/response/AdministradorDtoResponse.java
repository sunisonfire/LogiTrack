package com.DannaS2.LogiTrack.dto.response;

import com.DannaS2.LogiTrack.model.TipoPersona;

public record AdministradorDtoResponse(
        Long id,
        PersonaDtoResponse persona,
        String username
) {}

