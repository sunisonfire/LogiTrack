package com.DannaS2.LogiTrack.dto.response;

import com.DannaS2.LogiTrack.model.TipoMovimiento;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;


@Schema(description = "Información de un movimiento de inventario con sus detalles")
public record MovimientoDtoResponse(
        @Schema(description = "Identificador único del movimiento", example = "1")
        Long id,

        @Schema(description = "Fecha y hora en que se registró el movimiento", example = "2026-11-16T21:40:00")
        LocalDateTime fecha,

        @Schema(description = "Tipo de movimiento", example = "TRANSFERENCIA")
        TipoMovimiento tipoMovimiento,

        @Schema(description = "Identificador de la persona que realizó el movimiento", example = "1")
        Long personaId,

        @Schema(description = "Nombre de la persona que realizó el movimiento", example = "Juan Pérez")
        String personaNombre,

        @Schema(description = "Identificador de la bodega de origen. Nulo en una ENTRADA", example = "1")
        Long bodegaOrigenId,

        @Schema(description = "Nombre de la bodega de origen. Nulo en una ENTRADA", example = "Bodega Central")
        String bodegaOrigenNombre,

        @Schema(description = "Identificador de la bodega de destino. Nulo en una SALIDA", example = "2")
        Long bodegaDestinoId,

        @Schema(description = "Nombre de la bodega de destino. Nulo en una SALIDA", example = "Bodega Norte")
        String bodegaDestinoNombre,

        @Schema(description = "Productos y cantidades que se movieron")
        List<DetalleMovimientoDtoResponse> detalles
) {
}
