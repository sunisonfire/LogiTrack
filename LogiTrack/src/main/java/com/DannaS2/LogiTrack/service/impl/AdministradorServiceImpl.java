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
import com.DannaS2.LogiTrack.model.TipoOperacion;
import com.DannaS2.LogiTrack.service.AuditoriaService;
import java.util.Map;
import java.util.LinkedHashMap;

import java.util.List;

@Service
public class AdministradorServiceImpl implements AdministradorService {

    private final AdministradorRepository administradorRepository;
    private final PersonaRepository personaRepository;
    private final AdministradorMapper administradorMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuditoriaService auditoriaService;


    public AdministradorServiceImpl(
            AdministradorRepository administradorRepository,
            PersonaRepository personaRepository,
            AdministradorMapper administradorMapper,
            PasswordEncoder passwordEncoder,
            AuditoriaService auditoriaService) {

        this.administradorRepository = administradorRepository;
        this.personaRepository = personaRepository;
        this.administradorMapper = administradorMapper;
        this.passwordEncoder = passwordEncoder;
        this.auditoriaService = auditoriaService;
    }

    @Override
    @Transactional
    public AdministradorDtoResponse guardar(AdministradorDtoRequest dto) {

        if (personaRepository.existsByDocumento(dto.persona().documento())) {
            throw new RecursoDuplicadoException(
                    "Ya existe una persona con ese documento"
            );
        }

        if (administradorRepository.existsByUsername(dto.username())) {
            throw new RecursoDuplicadoException(
                    "El username ya está en uso"
            );
        }

        Administrador admin = administradorMapper.DtoToEntity(dto);

        // La contraseña siempre se almacena cifrada.
        admin.setPassword(passwordEncoder.encode(dto.password()));

        Administrador guardado = administradorRepository.save(admin);

        AdministradorDtoResponse response =
                administradorMapper.entityToDto(guardado);

        auditoriaService.registrar(
                TipoOperacion.INSERT,
                "ADMINISTRADOR",
                guardado.getId(),
                null,
                crearSnapshot(guardado)
        );

        return response;
    }

    @Override
    @Transactional
    public AdministradorDtoResponse actualizar(
            AdministradorDtoRequest dto,
            Long id
    ) {

        Administrador admin = buscarEntidad(id);

        personaRepository.findByDocumento(dto.persona().documento())
                .filter(p -> !p.getId().equals(id))
                .ifPresent(p -> {
                    throw new RecursoDuplicadoException(
                            "Ya existe una persona con ese documento"
                    );
                });

        administradorRepository.findByUsername(dto.username())
                .filter(a -> !a.getId().equals(id))
                .ifPresent(a -> {
                    throw new RecursoDuplicadoException(
                            "El username ya está en uso"
                    );
                });

        // Capturar los datos anteriores.
        Map<String, Object> anterior = crearSnapshot(admin);

        // Preparar los nuevos datos sin incluir la contraseña.
        Map<String, Object> nuevos = crearSnapshot(dto, id);

        // Registrar antes de modificar el username.
        // Así AuditoriaService todavía puede identificar al usuario autenticado.
        auditoriaService.registrar(
                TipoOperacion.UPDATE,
                "ADMINISTRADOR",
                id,
                anterior,
                nuevos
        );

        administradorMapper.updateEntityToDto(admin, dto);

        // Mantener el cifrado de contraseñas.
        admin.setPassword(passwordEncoder.encode(dto.password()));

        Administrador actualizado = administradorRepository.save(admin);

        return administradorMapper.entityToDto(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {

        Administrador admin = buscarEntidad(id);

        Map<String, Object> anterior = crearSnapshot(admin);

        auditoriaService.registrar(
                TipoOperacion.DELETE,
                "ADMINISTRADOR",
                id,
                anterior,
                null
        );

        administradorRepository.delete(admin);
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

    private Map<String, Object> crearSnapshot(Administrador admin) {

        Map<String, Object> snapshot = new LinkedHashMap<>();

        snapshot.put("id", admin.getId());
        snapshot.put("nombre", admin.getNombre());
        snapshot.put("apellido", admin.getApellido());
        snapshot.put("documento", admin.getDocumento());
        snapshot.put("username", admin.getUsername());

        return snapshot;
    }

    private Map<String, Object> crearSnapshot(
            AdministradorDtoRequest dto,
            Long id
    ) {

        Map<String, Object> snapshot = new LinkedHashMap<>();

        snapshot.put("id", id);
        snapshot.put("nombre", dto.persona().nombre());
        snapshot.put("apellido", dto.persona().apellido());
        snapshot.put("documento", dto.persona().documento());
        snapshot.put("username", dto.username());

        return snapshot;
    }
}