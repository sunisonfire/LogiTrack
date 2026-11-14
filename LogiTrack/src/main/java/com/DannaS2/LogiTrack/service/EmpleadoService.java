package com.DannaS2.LogiTrack.service;

import com.DannaS2.LogiTrack.dto.request.EmpleadoDtoRequest;
import com.DannaS2.LogiTrack.dto.response.EmpleadoDtoResponse;

import java.util.List;

public interface EmpleadoService {
    EmpleadoDtoResponse guardar(EmpleadoDtoRequest dto);
    EmpleadoDtoResponse actualizar(EmpleadoDtoRequest dto, Long id);
    void eliminar(Long id);
    List<EmpleadoDtoResponse> listar();
    EmpleadoDtoResponse buscarId(Long id);
    List<EmpleadoDtoResponse> buscarBodega(Long bodegaId);
}
