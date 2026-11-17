package com.DannaS2.LogiTrack.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Línea de un movimiento: producto y cantidad que se movió")
public record DetalleMovimientoDtoResponse(
        @Schema(description = "Identificador único del detalle", example = "1")
        Long id,

        @Schema(description = "Identificador del movimiento al que pertenece el detalle", example = "1")
        Long movimientoId,

        @Schema(description = "Identificador del producto movido", example = "1")
        Long productoId,

        @Schema(description = "Nombre del producto movido", example = "Lip Gloss")
        String productoNombre,

        @Schema(description = "Unidades movidas del producto", example = "20")
        Integer cantidad
) {
}
