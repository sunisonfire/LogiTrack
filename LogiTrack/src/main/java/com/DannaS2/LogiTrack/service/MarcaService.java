package com.DannaS2.LogiTrack.service;

import com.DannaS2.LogiTrack.dto.request.MarcaDtoRequest;
import com.DannaS2.LogiTrack.dto.response.MarcaDtoResponse;

import java.util.List;

public interface MarcaService {
    MarcaDtoResponse guardar(MarcaDtoRequest dto);
    MarcaDtoResponse actualizar(MarcaDtoRequest dto, Long id);
    void eliminar(Long id);
    List<MarcaDtoResponse> listar();
    MarcaDtoResponse buscarId(Long id);
}
