package com.DannaS2.LogiTrack.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;


@Schema(description = "Datos necesarios para registrar un empleado")
public record EmpleadoDtoRequest(
        // Datos personales (nombre, apellido, documento)
        @Schema(
                description = "Datos personales del empleado",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Los datos de la persona son obligatorios")
        @Valid
        PersonaDtoRequest persona,

        // Usuario de acceso
        @Schema(
                description = "Nombre de usuario con el que iniciará sesión. Entre 4 y 50 caracteres",
                example = "juan.perez",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "El username es obligatorio")
        @Size(min = 4, max = 50, message = "El username debe tener entre 4 y 50 caracteres")
        String username,

        // Contraseña de acceso
        @Schema(
                description = "Contraseña de acceso. Debe tener entre 8 y 100 caracteres",
                example = "Clave12345",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
        String password,

        // Bodega a la que se asigna el empleado
        @Schema(
                description = "Identificador de la bodega a la que se asigna el empleado",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "La bodega es obligatoria")
        @Positive(message = "El ID de la bodega debe ser positivo")
        Long bodegaId
) {}
