package com.DannaS2.LogiTrack.dto.request;

import com.DannaS2.LogiTrack.model.TipoMovimiento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;
//REVISAR EL ASSERT TRUE!


@Schema(description = "Datos necesarios para registrar un movimiento de inventario (entrada, salida o transferencia)")
public record MovimientoDtoRequest(
        // Define qué reglas de bodegas se aplican
        @Schema(
                description = "Tipo de movimiento. Define qué bodegas son obligatorias",
                example = "TRANSFERENCIA",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "El tipo de movimiento es obligatorio")
        TipoMovimiento tipoMovimiento,

        // Quién registra el movimiento
        @Schema(
                description = "Identificador de la persona que realiza el movimiento",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "La persona es obligatoria")
        @Positive(message = "El ID de la persona debe ser positivo")
        Long personaId,

        // Opcional: depende del tipo de movimiento (ver isBodegasValidas)
        @Schema(
                description = "Identificador de la bodega de origen. Obligatoria en SALIDA y TRANSFERENCIA; debe ir vacía en ENTRADA",
                example = "1"
        )
        Long bodegaOrigenId,

        // Opcional: depende del tipo de movimiento (ver isBodegasValidas)
        @Schema(
                description = "Identificador de la bodega de destino. Obligatoria en ENTRADA y TRANSFERENCIA; debe ir vacía en SALIDA",
                example = "2"
        )
        Long bodegaDestinoId,

        // Líneas del movimiento; cada una se valida con @Valid
        @Schema(
                description = "Productos y cantidades del movimiento. Debe tener al menos un detalle",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotEmpty(message = "El movimiento debe tener al menos un detalle")
        List<@NotNull(message = "El detalle no puede ser nulo") @Valid DetalleMovimientoDtoRequest> detalles
) {
    /**
     * Valida que las bodegas informadas correspondan al tipo de movimiento.
     *
     * @return {@code true} si la combinación de bodegas es válida para el tipo de movimiento
     *         (o si el tipo es {@code null}, caso que ya cubre {@code @NotNull})
     */
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
