
package com.DannaS2.LogiTrack.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AprobarSolicitudRequest(

        @NotNull(message = "La bodega es obligatoria")
        @Positive(message = "El ID de la bodega debe ser positivo")
        Long bodegaId
) {}
