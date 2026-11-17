package com.DannaS2.LogiTrack.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Información de un administrador devuelta por el sistema")
public record AdministradorDtoResponse(
        @Schema(description = "Identificador único del administrador", example = "1")
        Long id,

        @Schema(description = "Datos personales del administrador")
        PersonaDtoResponse persona,

        @Schema(description = "Nombre de usuario con el que inicia sesión", example = "admin.logitrack")
        String username
) {}

