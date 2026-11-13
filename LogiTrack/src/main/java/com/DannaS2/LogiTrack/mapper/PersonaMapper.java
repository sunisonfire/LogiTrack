package com.DannaS2.LogiTrack.mapper;

import com.DannaS2.LogiTrack.dto.request.PersonaDtoRequest;
import com.DannaS2.LogiTrack.dto.response.PersonaDtoResponse;
import com.DannaS2.LogiTrack.model.Persona;
import org.springframework.stereotype.Component;

@Component
public class PersonaMapper {

    public PersonaDtoResponse entityToDto(Persona persona) {
        if (persona == null) return null;
        return new PersonaDtoResponse(
                persona.getId(),
                persona.getNombre(),
                persona.getApellido(),
                persona.getDocumento(),
                persona.getTipoPersona()
        );
    }

    // tipoPersona no se asigna: la columna "tipo" es insertable=false (la define el discriminador)
    public Persona DtoToEntity(PersonaDtoRequest dto) {
        if (dto == null) return null;
        Persona p = new Persona();
        p.setNombre(dto.nombre());
        p.setApellido(dto.apellido());
        p.setDocumento(dto.documento());
        return p;
    }

    public void updateEntityToDto(Persona persona, PersonaDtoRequest dto) {
        persona.setNombre(dto.nombre());
        persona.setApellido(dto.apellido());
        persona.setDocumento(dto.documento());
    }
}
