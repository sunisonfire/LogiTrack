
package com.DannaS2.LogiTrack.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para solicitar el registro de un nuevo empleado")
public record RegistroRequest(

        @Schema(example = "Juan", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 80, message = "El nombre no puede superar 80 caracteres")
        String nombre,

        @Schema(example = "Perez", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "El apellido es obligatorio")
        @Size(max = 80, message = "El apellido no puede superar 80 caracteres")
        String apellido,

        @Schema(example = "1234567890", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "El documento es obligatorio")
        @Size(max = 20, message = "El documento no puede superar 20 caracteres")
        String documento,

        @Schema(example = "juan.perez", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "El nombre de usuario es obligatorio")
        @Size(min = 3, max = 50,
                message = "El usuario debe tener entre 3 y 50 caracteres")
        String username,

        @Schema(example = "ClaveSegura123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, max = 72,
                message = "La contraseña debe tener entre 8 y 72 caracteres")
        String password
) {}
