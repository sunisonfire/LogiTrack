package com.DannaS2.LogiTrack.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Información de un empleado devuelta por el sistema")
public record EmpleadoDtoResponse(
        @Schema(description = "Identificador único del empleado", example = "1")
        Long id,

        @Schema(description = "Datos personales del empleado")
        PersonaDtoResponse persona,

        @Schema(description = "Nombre de usuario con el que inicia sesión", example = "juan.perez")
        String username,

        @Schema(description = "Identificador de la bodega a la que está asignado", example = "1")
        Long bodegaId,

        @Schema(description = "Nombre de la bodega a la que está asignado", example = "Bodega Central")
        String bodegaNombre
) {
}
