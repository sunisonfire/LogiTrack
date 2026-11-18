package com.DannaS2.LogiTrack.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos necesarios para registrar o actualizar un empleado")
public record EmpleadoDtoRequest(

        @Schema(
                description = "Datos personales del empleado",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Los datos de la persona son obligatorios")
        @Valid
        PersonaDtoRequest persona,

        @Schema(
                description = "Nombre de usuario para iniciar sesión", example = "juan.perez",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "El username es obligatorio")
        @Size(min = 4, max = 50,
                message = "El username debe tener entre 4 y 50 caracteres")
        String username,

        @Schema(
                description = "Contraseña. Obligatoria al crear y opcional al actualizar",
                example = "ClaveSegura123", minLength = 8, maxLength = 72)
        @Size(min = 8, max = 72, message = "La contraseña debe tener entre 8 y 72 caracteres")
        String password,

        @Schema(
                description = "Identificador de la bodega asignada", example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "La bodega es obligatoria")
        @Positive(message = "El ID de la bodega debe ser positivo")
        Long bodegaId

) {}
