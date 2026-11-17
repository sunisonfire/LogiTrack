package com.DannaS2.LogiTrack.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


@Schema(description = "Línea de un movimiento: producto y cantidad que se mueve")
public record DetalleMovimientoDtoRequest(
        // Producto involucrado en este detalle
        @Schema(
                description = "Identificador del producto que se mueve",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "El producto es obligatorio")
        @Positive(message = "El ID del producto debe ser positivo")
        Long productoId,

        // Cantidad movida (siempre positiva)
        @Schema(
                description = "Unidades del producto que se mueven. Debe ser mayor que cero",
                example = "20",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad debe ser mayor que 0")
        Integer cantidad
) {}
