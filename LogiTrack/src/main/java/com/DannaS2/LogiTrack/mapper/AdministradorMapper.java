package com.DannaS2.LogiTrack.mapper;

import com.DannaS2.LogiTrack.dto.request.AdministradorDtoRequest;
import com.DannaS2.LogiTrack.dto.response.AdministradorDtoResponse;
import com.DannaS2.LogiTrack.model.Administrador;
import org.springframework.stereotype.Component;

@Component
public class AdministradorMapper {

    private final PersonaMapper personaMapper;

    public AdministradorMapper(PersonaMapper personaMapper) {
        this.personaMapper = personaMapper;
    }

    public AdministradorDtoResponse entityToDto(Administrador admin) {
        if (admin == null) return null;
        return new AdministradorDtoResponse(
                admin.getId(),
                personaMapper.entityToDto(admin), // Administrador es una Persona
                admin.getUsername()
        );
    }

    public Administrador DtoToEntity(AdministradorDtoRequest dto) {
        if (dto == null) return null;
        Administrador a = new Administrador();
        a.setNombre(dto.persona().nombre());
        a.setApellido(dto.persona().apellido());
        a.setDocumento(dto.persona().documento());
        a.setUsername(dto.username());
        a.setPassword(dto.password()); // codificar en el servicio antes de guardar
        return a;
    }

    public void updateEntityToDto(Administrador admin, AdministradorDtoRequest dto) {
        admin.setNombre(dto.persona().nombre());
        admin.setApellido(dto.persona().apellido());
        admin.setDocumento(dto.persona().documento());
        admin.setUsername(dto.username());
        admin.setPassword(dto.password()); // codificar en el servicio antes de guardar
    }
}