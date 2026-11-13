package com.DannaS2.LogiTrack.mapper;

import com.DannaS2.LogiTrack.dto.request.MarcaDtoRequest;
import com.DannaS2.LogiTrack.dto.response.MarcaDtoResponse;
import com.DannaS2.LogiTrack.model.Marca;
import org.springframework.stereotype.Component;

@Component
public class MarcaMapper {

    public MarcaDtoResponse entityToDto(Marca marca) {
        if (marca == null) return null;
        return new MarcaDtoResponse(marca.getId(), marca.getNombre());
    }

    public Marca DtoToEntity(MarcaDtoRequest dto) {
        if (dto == null) return null;
        Marca m = new Marca();
        m.setNombre(dto.nombre());
        return m;
    }

    public void updateEntityToDto(Marca marca, MarcaDtoRequest dto) {
        marca.setNombre(dto.nombre());
    }
}
