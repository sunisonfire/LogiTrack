package com.DannaS2.LogiTrack.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CiudadDtoRequest(
        @Positive(message = "El ID de la ciudad debe ser positivo")
        Long id,

        @NotBlank(message = "El nombre de la ciudad es obligatorio")
        @Size(max = 80, message = "El nombre no puede superar 80 caracteres")
        String nombre
) {}
