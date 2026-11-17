package com.DannaS2.LogiTrack.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Schema(description = "Datos necesarios para registrar un administrador")
public record AdministradorDtoRequest(
        // Obligatorio y se valida también el contenido interno de PersonaDtoRequest
        @Schema(
                description = "Datos personales del administrador",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Los datos de la persona son obligatorios")
        @Valid
        PersonaDtoRequest persona,

        // Nombre de usuario para el login
        @Schema(
                description = "Nombre de usuario con el que iniciará sesión",
                example = "admin.logitrack",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "El username es obligatorio")
        String username,


        // Contraseña: no puede estar vacía y tiene límites de longitud
        @Schema(
                description = "Contraseña de acceso. Debe tener entre 8 y 100 caracteres",
                example = "Clave12345",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
        String password) {


}
