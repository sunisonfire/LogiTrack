package com.DannaS2.LogiTrack.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MarcaDtoRequest(
        @NotBlank(message = "El nombre de la marca es obligatorio")
        @Size(max = 80, message = "El nombre no puede superar 80 caracteres")
        String nombre
) {}