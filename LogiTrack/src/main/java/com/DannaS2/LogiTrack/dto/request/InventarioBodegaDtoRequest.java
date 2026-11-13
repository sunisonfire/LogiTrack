package com.DannaS2.LogiTrack.dto.request;

import com.DannaS2.LogiTrack.model.Bodega;
import com.DannaS2.LogiTrack.model.Producto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record InventarioBodegaDtoRequest(
        @Positive(message = "El ID del inventario debe ser positivo")
        Long id,

        @NotNull(message = "La bodega es obligatoria")
        Bodega bodega,

        @NotNull(message = "El producto es obligatorio")
        Producto producto,

        @NotNull(message = "La cantidad es obligatoria")
        @PositiveOrZero(message = "La cantidad no puede ser negativa")
        Integer cantidad
) {}
