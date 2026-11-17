package com.DannaS2.LogiTrack.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Información de una bodega devuelta por el sistema")
public record BodegaDtoResponse(
        @Schema(description = "Identificador único de la bodega", example = "1")
        Long id,

        @Schema(description = "Nombre de la bodega", example = "Bodega Central")
        String nombre,

        @Schema(description = "Dirección o ubicación física de la bodega", example = "Calle 10 # 20-30, Zona Industrial")
        String ubicacion,

        @Schema(description = "Identificador de la ciudad donde está la bodega", example = "1")
        Long ciudadId,

        @Schema(description = "Nombre de la ciudad", example = "Medellín")
        String ciudad,

        @Schema(description = "Capacidad máxima de almacenamiento", example = "5000")
        Integer capacidad,

        @Schema(description = "Identificador de la persona encargada. Nulo si no tiene encargado", example = "2")
        Long encargadoId,

        @Schema(description = "Nombre de la persona encargada. Nulo si no tiene encargado", example = "Juan Pérez")
        String encargado
) {
}
