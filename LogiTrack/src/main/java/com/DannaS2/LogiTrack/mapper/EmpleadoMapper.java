package com.DannaS2.LogiTrack.mapper;

import com.DannaS2.LogiTrack.dto.request.EmpleadoDtoRequest;
import com.DannaS2.LogiTrack.dto.response.EmpleadoDtoResponse;
import com.DannaS2.LogiTrack.model.Bodega;
import com.DannaS2.LogiTrack.model.Empleado;
import org.springframework.stereotype.Component;

@Component
public class EmpleadoMapper {

    private final PersonaMapper personaMapper;

    public EmpleadoMapper(PersonaMapper personaMapper) {
        this.personaMapper = personaMapper;
    }

    public EmpleadoDtoResponse entityToDto(Empleado empleado) {
        if (empleado == null) {
            return null;
        }

        return new EmpleadoDtoResponse(
                empleado.getId(),
                personaMapper.entityToDto(empleado),
                empleado.getUsername(),
                empleado.getBodega() != null ? empleado.getBodega().getId() : null,
                empleado.getBodega() != null ? empleado.getBodega().getNombre() : null
        );
    }

    public Empleado DtoToEntity(EmpleadoDtoRequest dto, Bodega bodega) {
        if (dto == null) {
            return null;
        }

        Empleado empleado = new Empleado();
        empleado.setNombre(dto.persona().nombre());
        empleado.setApellido(dto.persona().apellido());
        empleado.setDocumento(dto.persona().documento());
        empleado.setUsername(dto.username());
        empleado.setBodega(bodega);

        return empleado;
    }

    public void updateEntityToDto(
            Empleado empleado,
            EmpleadoDtoRequest dto,
            Bodega bodega
    ) {
        empleado.setNombre(dto.persona().nombre());
        empleado.setApellido(dto.persona().apellido());
        empleado.setDocumento(dto.persona().documento());
        empleado.setUsername(dto.username());
        empleado.setBodega(bodega);
    }
}
