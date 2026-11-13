package com.DannaS2.LogiTrack.dto.request;

import com.DannaS2.LogiTrack.model.Bodega;
import com.DannaS2.LogiTrack.model.DetalleMovimiento;
import com.DannaS2.LogiTrack.model.Persona;
import com.DannaS2.LogiTrack.model.TipoMovimiento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;
//REVISAR EL ASSERT TRUE!
public record MovimientoDtoRequest(
        @NotNull(message = "El tipo de movimiento es obligatorio")
        TipoMovimiento tipoMovimiento,

        @NotNull(message = "La persona es obligatoria")
        @Positive(message = "El ID de la persona debe ser positivo")
        Long personaId,

        Long bodegaOrigenId,

        Long bodegaDestinoId,

        @NotEmpty(message = "El movimiento debe tener al menos un detalle")
        List<@NotNull(message = "El detalle no puede ser nulo") @Valid DetalleMovimientoDtoRequest> detalles
) {
    // Replica el CHECK chk_movimiento_bodegas de la BD
    @AssertTrue(message = "Las bodegas no corresponden al tipo de movimiento")
    public boolean isBodegasValidas() {
        if (tipoMovimiento == null) return true; // ya lo valida @NotNull
        return switch (tipoMovimiento) {
            case ENTRADA -> bodegaOrigenId == null && bodegaDestinoId != null;
            case SALIDA -> bodegaOrigenId != null && bodegaDestinoId == null;
            case TRANSFERENCIA -> bodegaOrigenId != null && bodegaDestinoId != null
                    && !bodegaOrigenId.equals(bodegaDestinoId);
        };
    }
}