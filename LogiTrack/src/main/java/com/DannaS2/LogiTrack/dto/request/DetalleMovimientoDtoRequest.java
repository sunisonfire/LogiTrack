package com.DannaS2.LogiTrack.dto.request;

import com.DannaS2.LogiTrack.model.Movimiento;
import com.DannaS2.LogiTrack.model.Producto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DetalleMovimientoDtoRequest(
        @NotNull(message = "El movimiento es obligatorio")
        Movimiento movimiento,

        @NotNull(message = "El producto es obligatorio")
        Producto producto,

        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad debe ser mayor que 0")
        Integer cantidad
) {}
