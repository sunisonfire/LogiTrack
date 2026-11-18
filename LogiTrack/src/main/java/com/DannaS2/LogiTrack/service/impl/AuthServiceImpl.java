package com.DannaS2.LogiTrack.service.impl;

import com.DannaS2.LogiTrack.config.JwtService;
import com.DannaS2.LogiTrack.dto.request.LoginRequest;
import com.DannaS2.LogiTrack.dto.request.RegistroRequest;
import com.DannaS2.LogiTrack.dto.response.LoginResponse;
import com.DannaS2.LogiTrack.dto.response.RegistroResponse;
import com.DannaS2.LogiTrack.exception.CredencialesInvalidasException;
import com.DannaS2.LogiTrack.exception.RecursoDuplicadoException;
import com.DannaS2.LogiTrack.model.Administrador;
import com.DannaS2.LogiTrack.model.Empleado;
import com.DannaS2.LogiTrack.model.EstadoSolicitud;
import com.DannaS2.LogiTrack.model.SolicitudRegistro;
import com.DannaS2.LogiTrack.model.TipoPersona;
import com.DannaS2.LogiTrack.repository.AdministradorRepository;
import com.DannaS2.LogiTrack.repository.EmpleadoRepository;
import com.DannaS2.LogiTrack.repository.PersonaRepository;
import com.DannaS2.LogiTrack.repository.SolicitudRegistroRepository;
import com.DannaS2.LogiTrack.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AdministradorRepository administradorRepository;
    private final EmpleadoRepository empleadoRepository;
    private final PersonaRepository personaRepository;
    private final SolicitudRegistroRepository solicitudRegistroRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        return switch (request.rol()) {
            case ADMIN -> loginAdministrador(request);
            case EMPLEADO -> loginEmpleado(request);
        };
    }

    @Override
    @Transactional
    public RegistroResponse registrar(RegistroRequest request) {

        // Validar que el documento no esté registrado ni solicitado
        if (personaRepository.existsByDocumento(request.documento())
                || solicitudRegistroRepository.existsByDocumento(request.documento())) {
            throw new RecursoDuplicadoException(
                    "Ya existe una persona o solicitud con ese documento"
            );
        }

        // Validar que el username no esté ocupado por ningún rol
        if (administradorRepository.existsByUsername(request.username())
                || empleadoRepository.existsByUsername(request.username())
                || solicitudRegistroRepository.existsByUsername(request.username())) {
            throw new RecursoDuplicadoException(
                    "El nombre de usuario ya está en uso"
            );
        }

        // Crear solicitud de registro
        SolicitudRegistro solicitud = new SolicitudRegistro();
        solicitud.setNombre(request.nombre().trim());
        solicitud.setApellido(request.apellido().trim());
        solicitud.setDocumento(request.documento().trim());
        solicitud.setUsername(request.username().trim());

        // Guardar únicamente el hash de la contraseña
        solicitud.setPasswordHash(passwordEncoder.encode(request.password()));

        // Toda solicitud pública comienza pendiente
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);

        solicitudRegistroRepository.save(solicitud);

        return new RegistroResponse(
                "Solicitud de registro enviada. Espera la aprobación de un administrador."
        );
    }

    private LoginResponse loginAdministrador(LoginRequest request) {
        Administrador admin = administradorRepository.findByUsername(request.username())
                .filter(a -> passwordEncoder.matches(request.password(), a.getPassword()))
                .orElseThrow(CredencialesInvalidasException::new);

        String token = jwtService.generateToken(
                admin.getId(), admin.getUsername(), TipoPersona.ADMIN
        );

        return new LoginResponse(
                token,
                TipoPersona.ADMIN.name(),
                admin.getId(),
                admin.getNombre() + " " + admin.getApellido(),
                null
        );
    }

    private LoginResponse loginEmpleado(LoginRequest request) {
        Empleado empleado = empleadoRepository.findByUsername(request.username())
                .filter(e -> passwordEncoder.matches(request.password(), e.getPassword()))
                .orElseThrow(CredencialesInvalidasException::new);

        String token = jwtService.generateToken(
                empleado.getId(), empleado.getUsername(), TipoPersona.EMPLEADO
        );

        return new LoginResponse(
                token,
                TipoPersona.EMPLEADO.name(),
                empleado.getId(),
                empleado.getNombre() + " " + empleado.getApellido(),
                empleado.getBodega().getId()
        );
    }
}
