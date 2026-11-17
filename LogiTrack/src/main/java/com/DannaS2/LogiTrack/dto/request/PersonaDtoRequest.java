package com.DannaS2.LogiTrack.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Schema(description = "Datos personales básicos de una persona")
public record PersonaDtoRequest(
        // Nombre(s)
        @Schema(
                description = "Nombre de la persona",
                example = "Juan",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 80, message = "El nombre no puede superar 80 caracteres")
        String nombre,

        // Apellido(s)
        @Schema(
                description = "Apellido de la persona",
                example = "Pérez",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "El apellido es obligatorio")
        @Size(max = 80, message = "El apellido no puede superar 80 caracteres")
        String apellido,

        // Documento de identidad
        @Schema(
                description = "Número de documento de identidad",
                example = "1020304050",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "El documento es obligatorio")
        @Size(max = 20, message = "El documento no puede superar 20 caracteres")
        String documento
) {}
