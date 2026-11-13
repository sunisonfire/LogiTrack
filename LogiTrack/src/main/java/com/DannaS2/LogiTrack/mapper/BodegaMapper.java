package com.DannaS2.LogiTrack.mapper;

import com.DannaS2.LogiTrack.dto.request.BodegaDtoRequest;
import com.DannaS2.LogiTrack.dto.response.BodegaDtoResponse;
import com.DannaS2.LogiTrack.model.Bodega;
import com.DannaS2.LogiTrack.model.Ciudad;
import com.DannaS2.LogiTrack.model.Persona;
import org.springframework.stereotype.Component;

@Component
public class BodegaMapper {

    public BodegaDtoResponse entityToDto(Bodega bodega) {
        if (bodega == null) return null;
        return new BodegaDtoResponse(
                bodega.getId(),
                bodega.getNombre(),
                bodega.getUbicacion(),
                bodega.getCiudad() != null ? bodega.getCiudad().getId() : null,
                bodega.getCiudad() != null ? bodega.getCiudad().getNombre() : null,
                bodega.getCapacidad(),
                bodega.getEncargado() != null ? bodega.getEncargado().getId() : null,
                bodega.getEncargado() != null
                        ? bodega.getEncargado().getNombre() + " " + bodega.getEncargado().getApellido()
                        : null
        );
    }

    // Ciudad y encargado los resuelve el servicio (encargado puede ser null)
    public Bodega DtoToEntity(BodegaDtoRequest dto, Ciudad ciudad, Persona encargado) {
        if (dto == null) return null;
        Bodega b = new Bodega();
        b.setNombre(dto.nombre());
        b.setUbicacion(dto.ubicacion());
        b.setCiudad(ciudad);
        b.setCapacidad(dto.capacidad());
        b.setEncargado(encargado);
        return b;
    }

    public void updateEntityToDto(Bodega bodega, BodegaDtoRequest dto, Ciudad ciudad, Persona encargado) {
        bodega.setNombre(dto.nombre());
        bodega.setUbicacion(dto.ubicacion());
        bodega.setCiudad(ciudad);
        bodega.setCapacidad(dto.capacidad());
        bodega.setEncargado(encargado);
    }
}