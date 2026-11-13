package com.DannaS2.LogiTrack.dto.request;

import com.DannaS2.LogiTrack.model.Persona;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record BodegaDtoRequest(
        @Positive(message = "El ID de la bodega debe ser positivo")
        Long id,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
        String nombre,

        @NotBlank(message = "La ubicación es obligatoria")
        @Size(max = 150, message = "La ubicación no puede superar 150 caracteres")
        String ubicacion,

        @NotBlank(message = "La ciudad es obligatoria")
        @Size(max = 80, message = "La ciudad no puede superar 80 caracteres")
        String ciudad,

        @NotNull(message = "La capacidad es obligatoria")
        @Positive(message = "La capacidad debe ser mayor que 0")
        Integer capacidad,

        // encargado_id es NULL en la BD, por eso no lleva @NotNull
        Persona encargado
) {}
