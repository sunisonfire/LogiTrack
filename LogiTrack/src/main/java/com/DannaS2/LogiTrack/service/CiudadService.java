package com.DannaS2.LogiTrack.service;

import com.DannaS2.LogiTrack.dto.request.CiudadDtoRequest;
import com.DannaS2.LogiTrack.dto.response.CiudadDtoResponse;

import java.util.List;

public interface CiudadService {
    CiudadDtoResponse guardar(CiudadDtoRequest dto);
    CiudadDtoResponse actualizar(CiudadDtoRequest dto, Long id);
    void eliminar(Long id);
    List<CiudadDtoResponse> listar();
    CiudadDtoResponse buscarId(Long id);
}
