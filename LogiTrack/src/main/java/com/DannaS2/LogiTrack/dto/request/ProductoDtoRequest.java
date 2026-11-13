package com.DannaS2.LogiTrack.dto.request;

import com.DannaS2.LogiTrack.model.Categoria;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductoDtoRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 50, message = "El nombre no puede superar 50 caracteres")
        String nombre,

        @Size(max = 100, message = "La descripción no puede superar 100 caracteres")
        String descripcion,

        @NotNull(message = "El precio es obligatorio")
        @Positive(message = "El precio debe ser mayor que 0")
        @Digits(integer = 10, fraction = 2, message = "El precio admite máximo 10 enteros y 2 decimales")
        BigDecimal precio,

        @NotNull(message = "La categoría es obligatoria")
        Categoria categoria,

        @NotNull(message = "La marca es obligatoria")
        @Positive(message = "El ID de la marca debe ser positivo")
        Long marcaId
) {}
