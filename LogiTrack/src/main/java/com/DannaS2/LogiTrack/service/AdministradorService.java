package com.DannaS2.LogiTrack.service;

import com.DannaS2.LogiTrack.dto.request.AdministradorDtoRequest;
import com.DannaS2.LogiTrack.dto.response.AdministradorDtoResponse;

import java.util.List;

public interface AdministradorService {
    AdministradorDtoResponse guardar(AdministradorDtoRequest dto);
    AdministradorDtoResponse actualizar(AdministradorDtoRequest dto, Long id);
    void eliminar(Long id);
    List<AdministradorDtoResponse> listar();
    AdministradorDtoResponse buscarId(Long id);
}
