package com.DannaS2.LogiTrack.dto.response;

import com.DannaS2.LogiTrack.model.TipoOperacion;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Registro de auditoría: quién hizo qué cambio, sobre qué entidad y cuándo")
public record AuditoriaDtoResponse(
        @Schema(description = "Identificador único del registro de auditoría", example = "1")
        Long id,

        @Schema(description = "Operación realizada", example = "UPDATE")
        TipoOperacion tipoOperacion,

        @Schema(description = "Fecha y hora en que ocurrió la operación", example = "2026-11-16T21:40:00")
        LocalDateTime fechaHora,

        @Schema(description = "Identificador de la persona que realizó la operación", example = "1")
        Long personaId,

        @Schema(description = "Nombre de la persona que realizó la operación", example = "Juan Pérez")
        String personaNombre,

        @Schema(description = "Nombre de la entidad afectada", example = "Producto")
        String entidad,

        @Schema(description = "Identificador del registro afectado dentro de la entidad", example = "5")
        Long entidadId,

        @Schema(description = "Valores antes del cambio. Puede ser nulo en una creación", example = "{\"precio\": 14000.00}")
        String valoresAnteriores,

        @Schema(description = "Valores después del cambio. Puede ser nulo en una eliminación", example = "{\"precio\": 15000.00}")
        String valoresNuevos
) {
}
