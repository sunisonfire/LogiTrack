package com.DannaS2.LogiTrack.service.impl;

import com.DannaS2.LogiTrack.config.JwtService;
import com.DannaS2.LogiTrack.dto.request.LoginRequest;
import com.DannaS2.LogiTrack.dto.response.LoginResponse;
import com.DannaS2.LogiTrack.exception.CredencialesInvalidasException;
import com.DannaS2.LogiTrack.model.Administrador;
import com.DannaS2.LogiTrack.model.Empleado;
import com.DannaS2.LogiTrack.model.TipoPersona;
import com.DannaS2.LogiTrack.repository.AdministradorRepository;
import com.DannaS2.LogiTrack.repository.EmpleadoRepository;
import com.DannaS2.LogiTrack.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AdministradorRepository administradorRepository;
    private final EmpleadoRepository empleadoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        // Según el rol que escogió en el front, se busca en una tabla u otra
        return switch (request.rol()) {
            case ADMIN -> loginAdministrador(request);
            case EMPLEADO -> loginEmpleado(request);
        };
    }

    private LoginResponse loginAdministrador(LoginRequest request) {
        Administrador admin = administradorRepository.findByUsername(request.username())
                .filter(a -> passwordEncoder.matches(request.password(), a.getPassword()))
                .orElseThrow(CredencialesInvalidasException::new);

        String token = jwtService.generateToken(admin.getId(), admin.getUsername(), TipoPersona.ADMIN);
        return new LoginResponse(token, TipoPersona.ADMIN.name(), admin.getId(),
                admin.getNombre() + " " + admin.getApellido(), null);
    }

    private LoginResponse loginEmpleado(LoginRequest request) {
        Empleado empleado = empleadoRepository.findByUsername(request.username())
                .filter(e -> passwordEncoder.matches(request.password(), e.getPassword()))
                .orElseThrow(CredencialesInvalidasException::new);

        String token = jwtService.generateToken(empleado.getId(), empleado.getUsername(), TipoPersona.EMPLEADO);
        return new LoginResponse(token, TipoPersona.EMPLEADO.name(), empleado.getId(),
                empleado.getNombre() + " " + empleado.getApellido(), empleado.getBodega().getId());
    }
}