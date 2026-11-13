package com.DannaS2.LogiTrack.mapper;

import com.DannaS2.LogiTrack.dto.request.EmpleadoDtoRequest;
import com.DannaS2.LogiTrack.dto.response.EmpleadoDtoResponse;
import com.DannaS2.LogiTrack.model.Empleado;
import org.springframework.stereotype.Component;

@Component
public class EmpleadoMapper {

    private final PersonaMapper personaMapper;

    public EmpleadoMapper(PersonaMapper personaMapper) {
        this.personaMapper = personaMapper;
    }

    public EmpleadoDtoResponse entityToDto(Empleado empleado) {
        if (empleado == null) return null;
        return new EmpleadoDtoResponse(
                empleado.getId(),
                personaMapper.entityToDto(empleado), // Empleado es una Persona
                empleado.getUsername(),
                empleado.getBodega() != null ? empleado.getBodega().getId() : null,
                empleado.getBodega() != null ? empleado.getBodega().getNombre() : null
        );
    }

    public Empleado DtoToEntity(EmpleadoDtoRequest dto) {
        if (dto == null) return null;
        Empleado e = new Empleado();
        e.setNombre(dto.persona().nombre());
        e.setApellido(dto.persona().apellido());
        e.setDocumento(dto.persona().documento());
        e.setUsername(dto.username());
        e.setPassword(dto.password()); // codificar en el servicio antes de guardar
        e.setBodega(dto.bodega());
        return e;
    }

    public void updateEntityToDto(Empleado empleado, EmpleadoDtoRequest dto) {
        empleado.setNombre(dto.persona().nombre());
        empleado.setApellido(dto.persona().apellido());
        empleado.setDocumento(dto.persona().documento());
        empleado.setUsername(dto.username());
        empleado.setPassword(dto.password()); // codificar en el servicio antes de guardar
        empleado.setBodega(dto.bodega());
    }
}