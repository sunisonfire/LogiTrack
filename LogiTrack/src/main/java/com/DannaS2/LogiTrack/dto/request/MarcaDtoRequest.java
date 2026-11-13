package com.DannaS2.LogiTrack.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record MarcaDtoRequest(
        @Positive(message = "El ID de la marca debe ser positivo")
        Long id,

        @NotBlank(message = "El nombre de la marca es obligatorio")
        @Size(max = 80, message = "El nombre no puede superar 80 caracteres")
        String nombre
) {}