
package com.DannaS2.LogiTrack.service.impl;

import com.DannaS2.LogiTrack.dto.response.AuditoriaDtoResponse;
import com.DannaS2.LogiTrack.exception.BusinessRuleException;
import com.DannaS2.LogiTrack.exception.RecursoNoEncontradoException;
import com.DannaS2.LogiTrack.mapper.AuditoriaMapper;
import com.DannaS2.LogiTrack.model.Administrador;
import com.DannaS2.LogiTrack.model.Auditoria;
import com.DannaS2.LogiTrack.model.Empleado;
import com.DannaS2.LogiTrack.model.Persona;
import com.DannaS2.LogiTrack.model.TipoOperacion;
import com.DannaS2.LogiTrack.repository.AdministradorRepository;
import com.DannaS2.LogiTrack.repository.AuditoriaRepository;
import com.DannaS2.LogiTrack.repository.EmpleadoRepository;
import com.DannaS2.LogiTrack.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuditoriaServiceImpl implements AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;
    private final AuditoriaMapper auditoriaMapper;
    private final AdministradorRepository administradorRepository;
    private final EmpleadoRepository empleadoRepository;
    private final JsonMapper jsonMapper;

    @Override
    public List<AuditoriaDtoResponse> listar() {
        return auditoriaRepository.findAll().stream()
                .map(auditoriaMapper::entityToDto).toList();
    }

    @Override
    public AuditoriaDtoResponse buscarId(Long id) {
        return auditoriaRepository.findById(id)
                .map(auditoriaMapper::entityToDto)
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

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void registrar(
            TipoOperacion tipoOperacion,
            String entidad,
            Long entidadId,
            Object valoresAnteriores,
            Object valoresNuevos
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new BusinessRuleException(
                    "No se pudo identificar al usuario que realiza la operación"
            );
        }

        boolean esAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));

        boolean esEmpleado = authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_EMPLEADO".equals(authority.getAuthority()));

        Persona actor;

        if (esAdmin) {
            Administrador admin = administradorRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new BusinessRuleException(
                            "No se encontró el administrador autenticado"
                    ));
            actor = admin;
        } else if (esEmpleado) {
            Empleado empleado = empleadoRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new BusinessRuleException(
                            "No se encontró el empleado autenticado"
                    ));
            actor = empleado;
        } else {
            throw new BusinessRuleException(
                    "El usuario autenticado no tiene un rol válido para auditoría"
            );
        }

        Auditoria auditoria = new Auditoria();
        auditoria.setTipoOperacion(tipoOperacion);
        auditoria.setPersona(actor);
        auditoria.setEntidad(entidad);
        auditoria.setEntidadId(entidadId);
        auditoria.setValoresAnteriores(toJson(valoresAnteriores));
        auditoria.setValoresNuevos(toJson(valoresNuevos));

        auditoriaRepository.save(auditoria);
    }

    private String toJson(Object value) {
        if (value == null) {
            return null;
        }

        try {
            return jsonMapper.writeValueAsString(value);
        } catch (JacksonException exception) {
            throw new IllegalStateException(
                    "No fue posible serializar los datos de auditoría",
                    exception
            );
        }
    }
}
