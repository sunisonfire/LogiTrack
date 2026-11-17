
package com.DannaS2.LogiTrack.dto.response;

import com.DannaS2.LogiTrack.model.Categoria;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Información de un producto devuelta por el sistema")
public record ProductoDtoResponse(

        @Schema(description = "Identificador único del producto", example = "1")
        Long id,

        @Schema(description = "Nombre del producto", example = "Lip Gloss")
        String nombre,

        @Schema(description = "Descripción del producto", example = "Brillo labial de larga duración")
        String descripcion,

        @Schema(description = "Precio del producto", example = "15000.00")
        BigDecimal precio,

        @Schema(description = "Categoría del producto", example = "LABIALES")
        Categoria categoria,

        @Schema(description = "Identificador de la marca asociada", example = "1")
        Long marcaId,

        @Schema(description = "Nombre de la marca asociada", example = "Maybelline")
        String marcaNombre

) {
}
