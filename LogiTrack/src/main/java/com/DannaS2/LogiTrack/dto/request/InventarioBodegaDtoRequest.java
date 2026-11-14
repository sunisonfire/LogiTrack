package com.DannaS2.LogiTrack.dto.request;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record InventarioBodegaDtoRequest(
        @NotNull(message = "La bodega es obligatoria")
        @Positive(message = "El ID de la bodega debe ser positivo")
        Long bodegaId,

        @NotNull(message = "El producto es obligatorio")
        @Positive(message = "El ID del producto debe ser positivo")
        Long productoId,

        @NotNull(message = "La cantidad es obligatoria")
        @PositiveOrZero(message = "La cantidad no puede ser negativa")
        Integer cantidad
) {}
