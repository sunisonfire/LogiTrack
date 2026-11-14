package com.DannaS2.LogiTrack.service;

import com.DannaS2.LogiTrack.dto.response.AuditoriaDtoResponse;
import com.DannaS2.LogiTrack.model.TipoOperacion;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditoriaService {
    List<AuditoriaDtoResponse> listar();
    AuditoriaDtoResponse buscarId(Long id);
    List<AuditoriaDtoResponse> buscarPersona(Long personaId);
    List<AuditoriaDtoResponse> buscarTipo(TipoOperacion tipo);
    List<AuditoriaDtoResponse> buscarEntidad(String entidad, Long entidadId);
    List<AuditoriaDtoResponse> buscarFechas(LocalDateTime desde, LocalDateTime hasta);
}