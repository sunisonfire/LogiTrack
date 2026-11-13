package com.DannaS2.LogiTrack.dto.response;

import com.DannaS2.LogiTrack.model.TipoMovimiento;

import java.time.LocalDateTime;
import java.util.List;

public record MovimientoDtoResponse(
        Long id,
        LocalDateTime fecha,
        TipoMovimiento tipoMovimiento,
        Long personaId,
        String personaNombre,
        Long bodegaOrigenId,
        String bodegaOrigenNombre,
        Long bodegaDestinoId,
        String bodegaDestinoNombre,
        List<DetalleMovimientoDtoResponse> detalles
) {
}
