package com.DannaS2.LogiTrack.service.impl;

import com.DannaS2.LogiTrack.dto.request.AprobarSolicitudRequest;
import com.DannaS2.LogiTrack.dto.request.RechazarSolicitudRequest;
import com.DannaS2.LogiTrack.dto.response.SolicitudRegistroDtoResponse;
import com.DannaS2.LogiTrack.exception.BusinessRuleException;
import com.DannaS2.LogiTrack.exception.RecursoDuplicadoException;
import com.DannaS2.LogiTrack.exception.RecursoNoEncontradoException;
import com.DannaS2.LogiTrack.model.Administrador;
import com.DannaS2.LogiTrack.model.Bodega;
import com.DannaS2.LogiTrack.model.Empleado;
import com.DannaS2.LogiTrack.model.EstadoSolicitud;
import com.DannaS2.LogiTrack.model.SolicitudRegistro;
import com.DannaS2.LogiTrack.model.TipoOperacion;
import com.DannaS2.LogiTrack.repository.AdministradorRepository;
import com.DannaS2.LogiTrack.repository.BodegaRepository;
import com.DannaS2.LogiTrack.repository.EmpleadoRepository;
import com.DannaS2.LogiTrack.repository.PersonaRepository;
import com.DannaS2.LogiTrack.repository.SolicitudRegistroRepository;
import com.DannaS2.LogiTrack.service.AuditoriaService;
import com.DannaS2.LogiTrack.service.SolicitudRegistroService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SolicitudRegistroServiceImpl implements SolicitudRegistroService {

    private final SolicitudRegistroRepository solicitudRepository;
    private final AdministradorRepository administradorRepository;
    private final EmpleadoRepository empleadoRepository;
    private final PersonaRepository personaRepository;
    private final BodegaRepository bodegaRepository;
    private final AuditoriaService auditoriaService;

    @Override
    public List<SolicitudRegistroDtoResponse> listarPendientes() {
        return solicitudRepository
                .findByEstadoOrderByFechaSolicitudAsc(EstadoSolicitud.PENDIENTE)
                .stream()
                .map(this::convertirDto)
                .toList();
    }

    @Override
    @Transactional
    public SolicitudRegistroDtoResponse aprobar(
            Long id,
            AprobarSolicitudRequest request
    ) {
        SolicitudRegistro solicitud = buscarSolicitudPendiente(id);
        Administrador admin = obtenerAdministradorAutenticado();

        Bodega bodega = bodegaRepository.findById(request.bodegaId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una bodega con ID: " + request.bodegaId()
                ));

        // Volver a comprobar que los datos no se hayan registrado entretanto.
        if (personaRepository.existsByDocumento(solicitud.getDocumento())) {
            throw new RecursoDuplicadoException(
                    "Ya existe una persona registrada con ese documento"
            );
        }

        if (administradorRepository.existsByUsername(solicitud.getUsername())
                || empleadoRepository.existsByUsername(solicitud.getUsername())) {
            throw new RecursoDuplicadoException(
                    "El nombre de usuario ya está siendo utilizado"
            );
        }

        // Crear el empleado con la contraseña que ya está cifrada.
        Empleado empleado = new Empleado();
        empleado.setNombre(solicitud.getNombre());
        empleado.setApellido(solicitud.getApellido());
        empleado.setDocumento(solicitud.getDocumento());
        empleado.setUsername(solicitud.getUsername());
        empleado.setPassword(solicitud.getPasswordHash());
        empleado.setBodega(bodega);

        Empleado empleadoGuardado = empleadoRepository.save(empleado);

        // Actualizar la solicitud y guardar quién la revisó.
        Map<String, Object> valoresAnteriores = new LinkedHashMap<>();
        valoresAnteriores.put("estado", solicitud.getEstado().name());

        solicitud.setEstado(EstadoSolicitud.APROBADA);
        solicitud.setFechaRevision(LocalDateTime.now());
        solicitud.setRevisadoPor(admin);
        solicitud.setMotivoRechazo(null);

        solicitudRepository.save(solicitud);

        Map<String, Object> valoresNuevos = new LinkedHashMap<>();
        valoresNuevos.put("estado", solicitud.getEstado().name());
        valoresNuevos.put("revisadoPorId", admin.getId());

        // Registrar ambas operaciones dentro de la misma transacción.
        Map<String, Object> datosEmpleado = new LinkedHashMap<>();
        datosEmpleado.put("id", empleadoGuardado.getId());
        datosEmpleado.put("nombre", empleadoGuardado.getNombre());
        datosEmpleado.put("apellido", empleadoGuardado.getApellido());
        datosEmpleado.put("documento", empleadoGuardado.getDocumento());
        datosEmpleado.put("username", empleadoGuardado.getUsername());
        datosEmpleado.put("bodegaId", bodega.getId());

        auditoriaService.registrar(
                TipoOperacion.INSERT,
                "EMPLEADO",
                empleadoGuardado.getId(),
                null,
                datosEmpleado
        );

        auditoriaService.registrar(
                TipoOperacion.UPDATE,
                "SOLICITUD_REGISTRO",
                solicitud.getId(),
                valoresAnteriores,
                valoresNuevos
        );

        return convertirDto(solicitud);
    }

    @Override
    @Transactional
    public SolicitudRegistroDtoResponse rechazar(
            Long id,
            RechazarSolicitudRequest request
    ) {
        SolicitudRegistro solicitud = buscarSolicitudPendiente(id);
        Administrador admin = obtenerAdministradorAutenticado();

        Map<String, Object> valoresAnteriores = new LinkedHashMap<>();
        valoresAnteriores.put("estado", solicitud.getEstado().name());

        String motivo = request.motivo();
        if (motivo != null && motivo.isBlank()) {
            motivo = null;
        } else if (motivo != null) {
            motivo = motivo.trim();
        }

        solicitud.setEstado(EstadoSolicitud.RECHAZADA);
        solicitud.setFechaRevision(LocalDateTime.now());
        solicitud.setRevisadoPor(admin);
        solicitud.setMotivoRechazo(motivo);

        solicitudRepository.save(solicitud);

        Map<String, Object> valoresNuevos = new LinkedHashMap<>();
        valoresNuevos.put("estado", solicitud.getEstado().name());
        valoresNuevos.put("motivoRechazo", solicitud.getMotivoRechazo());
        valoresNuevos.put("revisadoPorId", admin.getId());

        auditoriaService.registrar(
                TipoOperacion.UPDATE,
                "SOLICITUD_REGISTRO",
                solicitud.getId(),
                valoresAnteriores,
                valoresNuevos
        );

        return convertirDto(solicitud);
    }

    private SolicitudRegistro buscarSolicitudPendiente(Long id) {
        SolicitudRegistro solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una solicitud con ID: " + id
                ));

        if (solicitud.getEstado() != EstadoSolicitud.PENDIENTE) {
            throw new BusinessRuleException(
                    "La solicitud ya fue revisada y no se puede modificar"
            );
        }

        return solicitud;
    }

    private Administrador obtenerAdministradorAutenticado() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new BusinessRuleException(
                    "No se pudo identificar al administrador autenticado"
            );
        }

        return administradorRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new BusinessRuleException(
                        "No se encontró el administrador autenticado"
                ));
    }

    private SolicitudRegistroDtoResponse convertirDto(
            SolicitudRegistro solicitud
    ) {
        return new SolicitudRegistroDtoResponse(
                solicitud.getId(),
                solicitud.getNombre(),
                solicitud.getApellido(),
                solicitud.getDocumento(),
                solicitud.getUsername(),
                solicitud.getFechaSolicitud(),
                solicitud.getEstado()
        );
    }
}