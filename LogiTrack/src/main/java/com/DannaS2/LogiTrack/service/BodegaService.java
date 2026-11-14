package com.DannaS2.LogiTrack.service;

import com.DannaS2.LogiTrack.dto.request.BodegaDtoRequest;
import com.DannaS2.LogiTrack.dto.response.BodegaDtoResponse;

import java.util.List;

public interface BodegaService {
    BodegaDtoResponse guardar(BodegaDtoRequest dto);
    BodegaDtoResponse actualizar(BodegaDtoRequest dto, Long id);
    void eliminar(Long id);
    List<BodegaDtoResponse> listar();
    BodegaDtoResponse buscarId(Long id);
    List<BodegaDtoResponse> buscarCiudad(Long ciudadId);
}
