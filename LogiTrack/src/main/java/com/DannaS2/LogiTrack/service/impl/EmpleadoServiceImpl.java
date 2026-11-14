package com.DannaS2.LogiTrack.service.impl;

import com.DannaS2.LogiTrack.dto.request.EmpleadoDtoRequest;
import com.DannaS2.LogiTrack.dto.response.EmpleadoDtoResponse;
import com.DannaS2.LogiTrack.exception.RecursoDuplicadoException;
import com.DannaS2.LogiTrack.exception.RecursoNoEncontradoException;
import com.DannaS2.LogiTrack.mapper.EmpleadoMapper;
import com.DannaS2.LogiTrack.model.Bodega;
import com.DannaS2.LogiTrack.model.Empleado;
import com.DannaS2.LogiTrack.repository.BodegaRepository;
import com.DannaS2.LogiTrack.repository.EmpleadoRepository;
import com.DannaS2.LogiTrack.repository.PersonaRepository;
import com.DannaS2.LogiTrack.service.EmpleadoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final PersonaRepository personaRepository;
    private final BodegaRepository bodegaRepository;
    private final EmpleadoMapper empleadoMapper;

    public EmpleadoServiceImpl(EmpleadoRepository empleadoRepository, PersonaRepository personaRepository,
                               BodegaRepository bodegaRepository, EmpleadoMapper empleadoMapper) {
        this.empleadoRepository = empleadoRepository;
        this.personaRepository = personaRepository;
        this.bodegaRepository = bodegaRepository;
        this.empleadoMapper = empleadoMapper;
    }

    @Override
    @Transactional
    public EmpleadoDtoResponse guardar(EmpleadoDtoRequest dto) {
        if (personaRepository.existsByDocumento(dto.persona().documento())) {
            throw new RecursoDuplicadoException("Ya existe una persona con ese documento");
        }
        if (empleadoRepository.existsByUsername(dto.username())) {
            throw new RecursoDuplicadoException("El username ya está en uso");
        }
        Bodega bodega = obtenerBodega(dto.bodegaId());                  // CAMBIO: por id
        Empleado empleado = empleadoMapper.DtoToEntity(dto, bodega);    // CAMBIO: el mapper recibe la bodega
        // TODO (al hacer seguridad): codificar la contraseña
        return empleadoMapper.entityToDto(empleadoRepository.save(empleado));
    }

    @Override
    @Transactional
    public EmpleadoDtoResponse actualizar(EmpleadoDtoRequest dto, Long id) {
        Empleado empleado = buscarEntidad(id);
        personaRepository.findByDocumento(dto.persona().documento())
                .filter(p -> !p.getId().equals(id))
                .ifPresent(p -> { throw new RecursoDuplicadoException("Ya existe una persona con ese documento"); });
        empleadoRepository.findByUsername(dto.username())
                .filter(e -> !e.getId().equals(id))
                .ifPresent(e -> { throw new RecursoDuplicadoException("El username ya está en uso"); });
        Bodega bodega = obtenerBodega(dto.bodegaId());                  // CAMBIO
        empleadoMapper.updateEntityToDto(empleado, dto, bodega);        // CAMBIO
        // TODO (al hacer seguridad): codificar la contraseña
        return empleadoMapper.entityToDto(empleado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        empleadoRepository.delete(buscarEntidad(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpleadoDtoResponse> listar() {
        return empleadoRepository.findAll().stream().map(empleadoMapper::entityToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EmpleadoDtoResponse buscarId(Long id) {
        return empleadoMapper.entityToDto(buscarEntidad(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpleadoDtoResponse> buscarBodega(Long bodegaId) {
        return empleadoRepository.findByBodegaId(bodegaId).stream().map(empleadoMapper::entityToDto).toList();
    }

    private Empleado buscarEntidad(Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Empleado no encontrado: " + id));
    }

    private Bodega obtenerBodega(Long id) {
        return bodegaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Bodega no encontrada: " + id));
    }
}