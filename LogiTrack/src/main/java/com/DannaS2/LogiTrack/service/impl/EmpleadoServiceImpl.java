package com.DannaS2.LogiTrack.service.impl;

import com.DannaS2.LogiTrack.dto.request.EmpleadoDtoRequest;
import com.DannaS2.LogiTrack.dto.response.EmpleadoDtoResponse;
import com.DannaS2.LogiTrack.mapper.EmpleadoMapper;
import com.DannaS2.LogiTrack.model.Bodega;
import com.DannaS2.LogiTrack.model.Empleado;
import com.DannaS2.LogiTrack.repository.AdministradorRepository;
import com.DannaS2.LogiTrack.repository.BodegaRepository;
import com.DannaS2.LogiTrack.repository.EmpleadoRepository;
import com.DannaS2.LogiTrack.repository.PersonaRepository;
import com.DannaS2.LogiTrack.service.AuditoriaService;
import com.DannaS2.LogiTrack.service.EmpleadoService;
import com.DannaS2.LogiTrack.model.TipoOperacion;
import com.DannaS2.LogiTrack.exception.BusinessRuleException;
import com.DannaS2.LogiTrack.exception.RecursoDuplicadoException;
import com.DannaS2.LogiTrack.exception.RecursoNoEncontradoException;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final PersonaRepository personaRepository;
    private final AdministradorRepository administradorRepository;
    private final BodegaRepository bodegaRepository;
    private final EmpleadoMapper empleadoMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuditoriaService auditoriaService;

    @Override
    @Transactional
    public EmpleadoDtoResponse guardar(EmpleadoDtoRequest dto) {

        // La contraseña es obligatoria al crear un empleado.
        if (dto.password() == null || dto.password().isBlank()) {
            throw new BusinessRuleException(
                    "La contraseña es obligatoria al registrar un empleado"
            );
        }

        // Validar que el documento no esté registrado.
        if (personaRepository.existsByDocumento(dto.persona().documento())) {
            throw new RecursoDuplicadoException(
                    "Ya existe una persona registrada con ese documento"
            );
        }

        // El username debe ser único entre empleados y administradores.
        if (empleadoRepository.existsByUsername(dto.username())
                || administradorRepository.existsByUsername(dto.username())) {
            throw new RecursoDuplicadoException(
                    "El nombre de usuario ya está registrado"
            );
        }

        Bodega bodega = obtenerBodega(dto.bodegaId());

        Empleado empleado = empleadoMapper.DtoToEntity(dto, bodega);

        // Nunca guardar la contraseña en texto plano.
        empleado.setPassword(passwordEncoder.encode(dto.password()));

        Empleado empleadoGuardado = empleadoRepository.save(empleado);

        // Registrar auditoría de la creación.
        auditoriaService.registrar(
                TipoOperacion.INSERT,
                "EMPLEADO",
                empleadoGuardado.getId(),
                null,
                crearSnapshot(empleadoGuardado)
        );

        return empleadoMapper.entityToDto(empleadoGuardado);
    }

    @Override
    @Transactional
    public EmpleadoDtoResponse actualizar(EmpleadoDtoRequest dto, Long id) {

        Empleado empleado = buscarEntidad(id);

        // Verificar que el documento no pertenezca a otra persona.
        personaRepository.findByDocumento(dto.persona().documento())
                .filter(persona -> !Objects.equals(persona.getId(), id))
                .ifPresent(persona -> {
                    throw new RecursoDuplicadoException(
                            "El documento ya está registrado por otra persona"
                    );
                });

        // Verificar que el username no pertenezca a otro empleado.
        empleadoRepository.findByUsername(dto.username())
                .filter(otro -> !Objects.equals(otro.getId(), id))
                .ifPresent(otro -> {
                    throw new RecursoDuplicadoException(
                            "El nombre de usuario ya está registrado"
                    );
                });

        // Verificar que ningún administrador tenga ese username.
        if (administradorRepository.existsByUsername(dto.username())) {
            throw new RecursoDuplicadoException(
                    "El nombre de usuario ya está registrado por un administrador"
            );
        }

        Bodega bodega = obtenerBodega(dto.bodegaId());

        // Guardar el estado anterior para la auditoría.
        Map<String, Object> valoresAnteriores = crearSnapshot(empleado);

        // Actualizar los datos del empleado.
        empleadoMapper.updateEntityToDto(empleado, dto, bodega);

        // Solo cambiar la contraseña si se envió una nueva.
        if (dto.password() != null && !dto.password().isBlank()) {
            empleado.setPassword(passwordEncoder.encode(dto.password()));
        }

        Empleado empleadoActualizado = empleadoRepository.save(empleado);

        // Registrar auditoría de la actualización.
        auditoriaService.registrar(
                TipoOperacion.UPDATE,
                "EMPLEADO",
                empleadoActualizado.getId(),
                valoresAnteriores,
                crearSnapshot(empleadoActualizado)
        );

        return empleadoMapper.entityToDto(empleadoActualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {

        Empleado empleado = buscarEntidad(id);

        // Guardar los datos antes de eliminar.
        Map<String, Object> valoresAnteriores = crearSnapshot(empleado);

        // Registrar auditoría antes de borrar el empleado.
        auditoriaService.registrar(
                TipoOperacion.DELETE,
                "EMPLEADO",
                empleado.getId(),
                valoresAnteriores,
                null
        );

        empleadoRepository.delete(empleado);
    }

    @Override
    public List<EmpleadoDtoResponse> listar() {
        return empleadoRepository.findAll()
                .stream()
                .map(empleadoMapper::entityToDto)
                .toList();
    }

    @Override
    public EmpleadoDtoResponse buscarId(Long id) {
        return empleadoMapper.entityToDto(buscarEntidad(id));
    }

    @Override
    public List<EmpleadoDtoResponse> buscarBodega(Long bodegaId) {

        obtenerBodega(bodegaId);

        return empleadoRepository.findByBodegaId(bodegaId)
                .stream()
                .map(empleadoMapper::entityToDto)
                .toList();
    }

    private Empleado buscarEntidad(Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el empleado con ID: " + id
                ));
    }

    private Bodega obtenerBodega(Long bodegaId) {
        return bodegaRepository.findById(bodegaId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró la bodega con ID: " + bodegaId
                ));
    }

    // Información segura para almacenar en la auditoría.
    // Nunca incluimos la contraseña ni su hash.
    private Map<String, Object> crearSnapshot(Empleado empleado) {

        Map<String, Object> snapshot = new LinkedHashMap<>();

        snapshot.put("id", empleado.getId());
        snapshot.put("nombre", empleado.getNombre());
        snapshot.put("apellido", empleado.getApellido());
        snapshot.put("documento", empleado.getDocumento());
        snapshot.put("username", empleado.getUsername());
        snapshot.put("bodegaId", empleado.getBodega().getId());
        snapshot.put("bodegaNombre", empleado.getBodega().getNombre());

        return snapshot;
    }
}