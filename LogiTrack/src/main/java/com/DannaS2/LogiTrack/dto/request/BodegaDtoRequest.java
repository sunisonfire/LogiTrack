package com.DannaS2.LogiTrack.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;


@Schema(description = "Datos necesarios para registrar o actualizar una bodega")
public record BodegaDtoRequest(
        // Nombre de la bodega, obligatorio
        @Schema(
                description = "Nombre de la bodega",
                example = "Bodega Central",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
        String nombre,

        // Ubicación física o dirección
        @Schema(
                description = "Dirección o ubicación física de la bodega",
                example = "Calle 10 # 20-30, Zona Industrial",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "La ubicación es obligatoria")
        @Size(max = 150, message = "La ubicación no puede superar 150 caracteres")
        String ubicacion,

        // Llave foránea hacia ciudad
        @Schema(
                description = "Identificador de la ciudad donde está la bodega",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "La ciudad es obligatoria")
        @Positive(message = "El ID de la ciudad debe ser positivo")
        Long ciudadId,

        // Cuántas unidades puede almacenar la bodega
        @Schema(
                description = "Capacidad máxima de almacenamiento. Debe ser mayor que cero",
                example = "5000",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "La capacidad es obligatoria")
        @Positive(message = "La capacidad debe ser mayor que 0")
        Integer capacidad,

        // encargado_id es NULL en la BD, por eso no lleva @NotNull
        @Schema(
                description = "Identificador de la persona encargada de la bodega. Es opcional",
                example = "2"
        )
        Long encargadoId
) {}
