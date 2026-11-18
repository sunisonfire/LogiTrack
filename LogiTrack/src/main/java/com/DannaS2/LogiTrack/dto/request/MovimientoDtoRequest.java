
package com.DannaS2.LogiTrack.dto.request;

import com.DannaS2.LogiTrack.model.TipoMovimiento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Datos necesarios para registrar un movimiento de inventario")
public record MovimientoDtoRequest(

        @Schema(
                description = "Tipo de movimiento: ENTRADA, SALIDA o TRANSFERENCIA",
                example = "TRANSFERENCIA",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "El tipo de movimiento es obligatorio")
        TipoMovimiento tipoMovimiento,

        @Schema(
                description = "Bodega de origen. Obligatoria en SALIDA y TRANSFERENCIA",
                example = "1"
        )
        Long bodegaOrigenId,

        @Schema(
                description = "Bodega de destino. Obligatoria en ENTRADA y TRANSFERENCIA",
                example = "2"
        )
        Long bodegaDestinoId,

        @Schema(
                description = "Productos y cantidades del movimiento",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotEmpty(message = "El movimiento debe tener al menos un detalle")
        List<
                @NotNull(message = "El detalle no puede ser nulo")
                @Valid DetalleMovimientoDtoRequest
                > detalles
) {

    @AssertTrue(message = "Las bodegas no corresponden al tipo de movimiento")
    public boolean isBodegasValidas() {

        if (tipoMovimiento == null) {
            return true;
        }

        return switch (tipoMovimiento) {
            case ENTRADA ->
                    bodegaOrigenId == null && bodegaDestinoId != null;

            case SALIDA ->
                    bodegaOrigenId != null && bodegaDestinoId == null;

            case TRANSFERENCIA ->
                    bodegaOrigenId != null
                            && bodegaDestinoId != null
                            && !bodegaOrigenId.equals(bodegaDestinoId);
        };
    }
}
