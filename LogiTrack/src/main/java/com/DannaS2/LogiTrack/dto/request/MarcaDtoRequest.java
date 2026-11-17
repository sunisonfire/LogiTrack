package com.DannaS2.LogiTrack.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos necesarios para registrar o actualizar una marca")
public record MarcaDtoRequest(
        // Nombre de la marca
        @Schema(
                description = "Nombre de la marca",
                example = "Maybelline",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "El nombre de la marca es obligatorio")
        @Size(max = 80, message = "El nombre no puede superar 80 caracteres")
        String nombre
) {}
