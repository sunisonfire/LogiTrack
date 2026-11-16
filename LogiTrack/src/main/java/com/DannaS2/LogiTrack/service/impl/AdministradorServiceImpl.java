package com.DannaS2.LogiTrack.service.impl;

import com.DannaS2.LogiTrack.dto.request.AdministradorDtoRequest;
import com.DannaS2.LogiTrack.dto.response.AdministradorDtoResponse;
import com.DannaS2.LogiTrack.exception.RecursoDuplicadoException;
import com.DannaS2.LogiTrack.exception.RecursoNoEncontradoException;
import com.DannaS2.LogiTrack.mapper.AdministradorMapper;
import com.DannaS2.LogiTrack.model.Administrador;
import com.DannaS2.LogiTrack.repository.AdministradorRepository;
import com.DannaS2.LogiTrack.repository.PersonaRepository;
import com.DannaS2.LogiTrack.service.AdministradorService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdministradorServiceImpl implements AdministradorService {

    private final AdministradorRepository administradorRepository;
    private final PersonaRepository personaRepository;
    private final AdministradorMapper administradorMapper;
    private final PasswordEncoder passwordEncoder;

    public AdministradorServiceImpl(AdministradorRepository administradorRepository,
                                    PersonaRepository personaRepository,
                                    AdministradorMapper administradorMapper,
                                    PasswordEncoder passwordEncoder) {
        this.administradorRepository = administradorRepository;
        this.personaRepository = personaRepository;
        this.administradorMapper = administradorMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public AdministradorDtoResponse guardar(AdministradorDtoRequest dto) {
        if (personaRepository.existsByDocumento(dto.persona().documento())) {
            throw new RecursoDuplicadoException("Ya existe una persona con ese documento");
        }
        if (administradorRepository.existsByUsername(dto.username())) {
            throw new RecursoDuplicadoException("El username ya está en uso");
        }
        Administrador admin = administradorMapper.DtoToEntity(dto);
        admin.setPassword(passwordEncoder.encode(dto.password()));   // contraseña cifrada
        return administradorMapper.entityToDto(administradorRepository.save(admin));
    }

    @Override
    @Transactional
    public AdministradorDtoResponse actualizar(AdministradorDtoRequest dto, Long id) {
        Administrador admin = buscarEntidad(id);
        personaRepository.findByDocumento(dto.persona().documento())
                .filter(p -> !p.getId().equals(id))
                .ifPresent(p -> { throw new RecursoDuplicadoException("Ya existe una persona con ese documento"); });
        administradorRepository.findByUsername(dto.username())
                .filter(a -> !a.getId().equals(id))
                .ifPresent(a -> { throw new RecursoDuplicadoException("El username ya está en uso"); });
        administradorMapper.updateEntityToDto(admin, dto);
        admin.setPassword(passwordEncoder.encode(dto.password()));   // contraseña cifrada
        return administradorMapper.entityToDto(admin);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        administradorRepository.delete(buscarEntidad(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdministradorDtoResponse> listar() {
        return administradorRepository.findAll().stream().map(administradorMapper::entityToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AdministradorDtoResponse buscarId(Long id) {
        return administradorMapper.entityToDto(buscarEntidad(id));
    }

    private Administrador buscarEntidad(Long id) {
        return administradorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Administrador no encontrado: " + id));
    }
}