package com.DannaS2.LogiTrack.mapper;

import com.DannaS2.LogiTrack.dto.request.CiudadDtoRequest;
import com.DannaS2.LogiTrack.dto.response.CiudadDtoResponse;
import com.DannaS2.LogiTrack.model.Ciudad;
import org.springframework.stereotype.Component;

@Component
public class CiudadMapper {

    public CiudadDtoResponse entityToDto(Ciudad ciudad) {
        if (ciudad == null) return null;
        return new CiudadDtoResponse(ciudad.getId(), ciudad.getNombre());
    }

    public Ciudad DtoToEntity(CiudadDtoRequest dto) {
        if (dto == null) return null;
        Ciudad c = new Ciudad();
        c.setNombre(dto.nombre());
        return c;
    }

    public void updateEntityToDto(Ciudad ciudad, CiudadDtoRequest dto) {
        ciudad.setNombre(dto.nombre());
    }
}
