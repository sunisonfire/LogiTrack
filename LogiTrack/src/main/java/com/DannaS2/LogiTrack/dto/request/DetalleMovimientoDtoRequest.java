package com.DannaS2.LogiTrack.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DetalleMovimientoDtoRequest(
        @NotNull(message = "El producto es obligatorio")
        @Positive(message = "El ID del producto debe ser positivo")
        Long productoId,

        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad debe ser mayor que 0")
        Integer cantidad
) {}
