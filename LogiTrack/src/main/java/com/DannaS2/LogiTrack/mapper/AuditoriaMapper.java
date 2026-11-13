package com.DannaS2.LogiTrack.mapper;

import com.DannaS2.LogiTrack.dto.response.AuditoriaDtoResponse;
import com.DannaS2.LogiTrack.model.Auditoria;
import org.springframework.stereotype.Component;

// Solo lectura: la auditoría la crea el service, no el cliente
@Component
public class AuditoriaMapper {

    public AuditoriaDtoResponse entityToDto(Auditoria a) {
        if (a == null) return null;
        return new AuditoriaDtoResponse(
                a.getId(),
                a.getTipoOperacion(),
                a.getFechaHora(),
                a.getPersona() != null ? a.getPersona().getId() : null,
                a.getPersona() != null ? a.getPersona().getNombre() + " " + a.getPersona().getApellido() : null,
                a.getEntidad(),
                a.getEntidadId(),
                a.getValoresAnteriores(),
                a.getValoresNuevos()
        );
    }
}
