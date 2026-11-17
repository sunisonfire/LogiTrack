package com.DannaS2.LogiTrack.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Inventario de un producto dentro de una bodega")
public record InventarioBodegaDtoResponse(
        @Schema(description = "Identificador único del registro de inventario", example = "1")
        Long id,

        @Schema(description = "Identificador de la bodega", example = "1")
        Long bodegaId,

        @Schema(description = "Nombre de la bodega", example = "Bodega Central")
        String bodegaNombre,

        @Schema(description = "Identificador del producto", example = "1")
        Long productoId,

        @Schema(description = "Nombre del producto", example = "Lip Gloss")
        String productoNombre,

        @Schema(description = "Unidades disponibles del producto en la bodega", example = "150")
        Integer cantidad
) {
}
