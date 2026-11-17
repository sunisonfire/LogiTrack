package com.DannaS2.LogiTrack.dto.request;

import com.DannaS2.LogiTrack.model.TipoPersona;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Credenciales para iniciar sesión")
public record LoginRequest(
        // Usuario con el que se registró la persona
        @Schema(
                description = "Nombre de usuario registrado",
                example = "juan.perez",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "El usuario es obligatorio")
        String username,

        // Contraseña ingresada
        @Schema(
                description = "Contraseña del usuario",
                example = "Clave12345",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "La contraseña es obligatoria")
        String password,

        // Tipo de usuario que intenta ingresar
        @Schema(
                description = "Rol con el que se inicia sesión",
                example = "EMPLEADO",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "El rol es obligatorio")
        TipoPersona rol
) {}
