package com.DannaS2.LogiTrack.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos necesarios para registrar o actualizar una ciudad")
public record CiudadDtoRequest(
        // Nombre de la ciudad
        @Schema(
                description = "Nombre de la ciudad",
                example = "Medellín",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "El nombre de la ciudad es obligatorio")
        @Size(max = 80, message = "El nombre no puede superar 80 caracteres")
        String nombre
) {}
