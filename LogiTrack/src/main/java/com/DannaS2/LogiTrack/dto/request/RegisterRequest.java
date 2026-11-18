package com.DannaS2.LogiTrack.dto.request;

import com.DannaS2.LogiTrack.model.TipoPersona;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

//REVISAR PATTERN Y ASSERT TRUE
//está diseñado para registrar directamente a un administrador o empleado.

@Schema(description = "Datos necesarios para registrar un nuevo usuario (administrador o empleado)")
public record RegisterRequest(
        @Schema(description = "Nombre de la persona", example = "Juan", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Size(max = 80) String nombre,

        @Schema(description = "Apellido de la persona", example = "Pérez", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Size(max = 80) String apellido,

        // El documento solo admite números, de 6 a 20 dígitos
        @Schema(description = "Documento de identidad. Solo números, entre 6 y 20 dígitos", example = "1020304050", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Pattern(regexp = "\\d{6,20}", message = "El documento debe tener entre 6 y 20 números") String documento,

        @Schema(description = "Nombre de usuario. Entre 4 y 50 caracteres", example = "juan.perez", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Size(min = 4, max = 50) String username,

        @Schema(description = "Contraseña de acceso. Entre 6 y 72 caracteres", example = "Clave12345", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Size(min = 6, max = 72) String password,

        @Schema(description = "Rol del usuario", example = "EMPLEADO", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull TipoPersona rol,

        @Schema(description = "Identificador de la bodega asignada. Obligatorio solo si el rol es EMPLEADO", example = "1")
        Long bodegaId
) {
    /**
     * Valida que un EMPLEADO tenga bodega asignada.
     *
     * @return {@code true} si el rol no es EMPLEADO, o si lo es y trae {@code bodegaId}
     */
    @AssertTrue(message = "Un EMPLEADO debe tener una bodega asignada")
    public boolean isBodegaValida() {
        return rol != TipoPersona.EMPLEADO || bodegaId != null;
    }
}
