package com.DannaS2.LogiTrack.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Información de una marca devuelta por el sistema")
public record MarcaDtoResponse(
        @Schema(description = "Identificador único de la marca", example = "1")
        Long id,

        @Schema(description = "Nombre de la marca", example = "Maybelline")
        String nombre
) {
}
