package com.DannaS2.LogiTrack.mapper;

import com.DannaS2.LogiTrack.dto.request.AuditoriaDtoRequest;
import com.DannaS2.LogiTrack.dto.response.AuditoriaDtoResponse;
import com.DannaS2.LogiTrack.model.Auditoria;
import org.springframework.stereotype.Component;

// La auditoría es un registro histórico: no se actualiza, por eso no hay updateEntityToDto.
@Component
public class AuditoriaMapper {

    public AuditoriaDtoResponse entityToDto(Auditoria a) {
        if (a == null) return null;
        return new AuditoriaDtoResponse(
                a.getId(),
                a.getTipoOperacion(),
                a.getFechaHora(),
                a.getPersona() != null ? a.getPersona().getId() : null,
                a.getPersona() != null ? a.getPersona().getNombre() + " " + a.getPersona().getApellido(): null,
                a.getEntidad(),
                a.getEntidadId(),
                a.getValoresAnteriores(),
                a.getValoresNuevos()
        );
    }

    public Auditoria DtoToEntity(AuditoriaDtoRequest dto) {
        if (dto == null) return null;
        Auditoria a = new Auditoria();
        a.setTipoOperacion(dto.tipoOperacion());
        a.setPersona(dto.persona());
        a.setEntidad(dto.entidad());
        a.setEntidadId(dto.entidadId());
        a.setValoresAnteriores(dto.valoresAnteriores());
        a.setValoresNuevos(dto.valoresNuevos());
        return a;
    }
}
