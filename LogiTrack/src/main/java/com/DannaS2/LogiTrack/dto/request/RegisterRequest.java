package com.DannaS2.LogiTrack.dto.request;

import com.DannaS2.LogiTrack.model.TipoPersona;
import jakarta.validation.constraints.*;

//REVISAR PATTERN Y ASSERT TRUE
public record RegisterRequest(
        @NotBlank @Size(max = 80) String nombre,
        @NotBlank @Size(max = 80) String apellido,
        @NotBlank @Pattern(regexp = "\\d{6,20}", message = "El documento debe tener entre 6 y 20 números") String documento,
        @NotBlank @Size(min = 4, max = 50) String username,
        @NotBlank @Size(min = 6, max = 72) String password,
        @NotNull TipoPersona rol,
        Long bodegaId
) {
    @AssertTrue(message = "Un EMPLEADO debe tener una bodega asignada")
    public boolean isBodegaValida() {
        return rol != TipoPersona.EMPLEADO || bodegaId != null;
    }
}