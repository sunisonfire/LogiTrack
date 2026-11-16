package com.DannaS2.LogiTrack.dto.request;

import com.DannaS2.LogiTrack.model.TipoPersona;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotBlank(message = "El usuario es obligatorio")
        String username,

        @NotBlank(message = "La contraseña es obligatoria")
        String password,

        @NotNull(message = "El rol es obligatorio")
        TipoPersona rol
) {}