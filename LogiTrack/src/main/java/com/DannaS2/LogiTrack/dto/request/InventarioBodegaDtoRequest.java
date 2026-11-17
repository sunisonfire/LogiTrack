package com.DannaS2.LogiTrack.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;


@Schema(description = "Datos necesarios para registrar o actualizar el inventario de un producto en una bodega")
public record InventarioBodegaDtoRequest(
        // Bodega que contiene el stock
        @Schema(
                description = "Identificador de la bodega donde está el stock",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "La bodega es obligatoria")
        @Positive(message = "El ID de la bodega debe ser positivo")
        Long bodegaId,

        // Producto almacenado
        @Schema(
                description = "Identificador del producto almacenado",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "El producto es obligatorio")
        @Positive(message = "El ID del producto debe ser positivo")
        Long productoId,

        // Stock actual; se permite 0 (sin existencias)
        @Schema(
                description = "Unidades disponibles del producto en la bodega. Puede ser cero, no negativa",
                example = "150",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "La cantidad es obligatoria")
        @PositiveOrZero(message = "La cantidad no puede ser negativa")
        Integer cantidad
) {}
