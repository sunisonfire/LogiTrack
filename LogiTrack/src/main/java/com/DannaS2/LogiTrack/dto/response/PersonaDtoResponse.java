package com.DannaS2.LogiTrack.dto.response;

import com.DannaS2.LogiTrack.model.TipoPersona;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos personales de una persona devueltos por el sistema")
public record PersonaDtoResponse(
        @Schema(description = "Identificador único de la persona", example = "1")
        Long id,

        @Schema(description = "Nombre de la persona", example = "Juan")
        String nombre,

        @Schema(description = "Apellido de la persona", example = "Pérez")
        String apellido,

        @Schema(description = "Número de documento de identidad", example = "1020304050")
        String documento,

        @Schema(description = "Tipo de persona en el sistema", example = "EMPLEADO")
        TipoPersona tipoPersona
) {
}
