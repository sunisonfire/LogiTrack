package com.DannaS2.LogiTrack.service.impl;

import com.DannaS2.LogiTrack.dto.response.AuditoriaDtoResponse;
import com.DannaS2.LogiTrack.exception.RecursoNoEncontradoException;
import com.DannaS2.LogiTrack.mapper.AuditoriaMapper;
import com.DannaS2.LogiTrack.model.TipoOperacion;
import com.DannaS2.LogiTrack.repository.AuditoriaRepository;
import com.DannaS2.LogiTrack.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuditoriaServiceImpl implements AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;
    private final AuditoriaMapper auditoriaMapper;

    @Override
    public List<AuditoriaDtoResponse> listar() {
        return auditoriaRepository.findAll().stream().map(auditoriaMapper::entityToDto).toList();
    }

    @Override
    public AuditoriaDtoResponse buscarId(Long id) {
        return auditoriaRepository.findById(id).map(auditoriaMapper::entityToDto)
                .orElseThrow(() -> new RecursoNoEncontradoException("Auditoría no encontrada: " + id));
    }

    @Override
    public List<AuditoriaDtoResponse> buscarPersona(Long personaId) {
        return auditoriaRepository.findByPersonaIdOrderByFechaHoraDesc(personaId).stream()
                .map(auditoriaMapper::entityToDto).toList();
    }

    @Override
    public List<AuditoriaDtoResponse> buscarTipo(TipoOperacion tipo) {
        return auditoriaRepository.findByTipoOperacion(tipo).stream()
                .map(auditoriaMapper::entityToDto).toList();
    }

    @Override
    public List<AuditoriaDtoResponse> buscarEntidad(String entidad, Long entidadId) {
        return auditoriaRepository.findByEntidadAndEntidadIdOrderByFechaHoraDesc(entidad, entidadId).stream()
                .map(auditoriaMapper::entityToDto).toList();
    }

    @Override
    public List<AuditoriaDtoResponse> buscarFechas(LocalDateTime desde, LocalDateTime hasta) {
        return auditoriaRepository.findByFechaHoraBetween(desde, hasta).stream()
                .map(auditoriaMapper::entityToDto).toList();
    }
}