package com.DannaS2.LogiTrack.dto.response;

import com.DannaS2.LogiTrack.model.TipoOperacion;

import java.time.LocalDateTime;

public record AuditoriaDtoResponse(
        Long id,
        TipoOperacion tipoOperacion,
        LocalDateTime fechaHora,
        Long personaId,
        String personaNombre,
        String entidad,
        Long entidadId,
        String valoresAnteriores,
        String valoresNuevos
) {
}
