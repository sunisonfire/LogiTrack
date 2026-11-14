package com.DannaS2.LogiTrack.service;

import com.DannaS2.LogiTrack.dto.request.MovimientoDtoRequest;
import com.DannaS2.LogiTrack.dto.response.MovimientoDtoResponse;
import com.DannaS2.LogiTrack.model.TipoMovimiento;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoService {
    MovimientoDtoResponse guardar(MovimientoDtoRequest dto);
    List<MovimientoDtoResponse> listar();
    MovimientoDtoResponse buscarId(Long id);
    List<MovimientoDtoResponse> buscarTipo(TipoMovimiento tipo);
    List<MovimientoDtoResponse> buscarPersona(Long personaId);
    List<MovimientoDtoResponse> buscarBodegaOrigen(Long bodegaId);
    List<MovimientoDtoResponse> buscarBodegaDestino(Long bodegaId);
    List<MovimientoDtoResponse> buscarFechas(LocalDateTime desde, LocalDateTime hasta);
}