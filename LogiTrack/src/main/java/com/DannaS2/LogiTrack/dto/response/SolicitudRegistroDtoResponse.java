
package com.DannaS2.LogiTrack.dto.response;

import com.DannaS2.LogiTrack.model.EstadoSolicitud;
import java.time.LocalDateTime;

public record SolicitudRegistroDtoResponse(
        Long id,
        String nombre,
        String apellido,
        String documento,
        String username,
        LocalDateTime fechaSolicitud,
        EstadoSolicitud estado
) {}
