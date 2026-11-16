package com.DannaS2.LogiTrack.service.impl;

import com.DannaS2.LogiTrack.exception.BusinessRuleException;
import com.DannaS2.LogiTrack.model.Administrador;
import com.DannaS2.LogiTrack.model.Auditoria;
import com.DannaS2.LogiTrack.model.Empleado;
import com.DannaS2.LogiTrack.model.Persona;
import com.DannaS2.LogiTrack.model.TipoOperacion;
import com.DannaS2.LogiTrack.repository.AdministradorRepository;
import com.DannaS2.LogiTrack.repository.AuditoriaRepository;
import com.DannaS2.LogiTrack.repository.EmpleadoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuditoriaRegistroService {
    private final AuditoriaRepository auditoriaRepository;
    private final AdministradorRepository administradorRepository;
    private final EmpleadoRepository empleadoRepository;
    private final ObjectMapper objectMapper;

    @Transactional(propagation = Propagation.MANDATORY)
    public void registrar(TipoOperacion tipoOperacion, String entidad, Long entidadId,
                          Object valoresAnteriores, Object valoresNuevos) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new BusinessRuleException("No se pudo identificar al usuario que realiza la operación");
        }
        boolean esAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
        boolean esEmpleado = authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_EMPLEADO".equals(authority.getAuthority()));
        Persona actor;
        if (esAdmin) {
            Administrador admin = administradorRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new BusinessRuleException("No se encontró el administrador autenticado"));
            actor = admin;
        } else if (esEmpleado) {
            Empleado empleado = empleadoRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new BusinessRuleException("No se encontró el empleado autenticado"));
            actor = empleado;
        } else {
            throw new BusinessRuleException("El usuario autenticado no tiene un rol válido para auditoría");
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
        if (value == null) return null;
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("No fue posible serializar los datos de auditoría", exception);
        }
    }
}
