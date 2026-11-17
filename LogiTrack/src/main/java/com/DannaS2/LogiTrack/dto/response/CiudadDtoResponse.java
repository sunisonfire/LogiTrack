package com.DannaS2.LogiTrack.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Información de una ciudad devuelta por el sistema")
public record CiudadDtoResponse(
        @Schema(description = "Identificador único de la ciudad", example = "1")
        Long id,

        @Schema(description = "Nombre de la ciudad", example = "Medellín")
        String nombre
) {
}
