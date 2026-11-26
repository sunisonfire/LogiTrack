package com.DannaS2.LogiTrack.service.impl;

import com.DannaS2.LogiTrack.dto.request.CiudadDtoRequest;
import com.DannaS2.LogiTrack.dto.response.CiudadDtoResponse;
import com.DannaS2.LogiTrack.exception.RecursoDuplicadoException;
import com.DannaS2.LogiTrack.exception.RecursoNoEncontradoException;
import com.DannaS2.LogiTrack.mapper.CiudadMapper;
import com.DannaS2.LogiTrack.model.Ciudad;
import com.DannaS2.LogiTrack.repository.CiudadRepository;
import com.DannaS2.LogiTrack.service.CiudadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.DannaS2.LogiTrack.model.TipoOperacion;
import com.DannaS2.LogiTrack.service.AuditoriaService;

import java.util.List;

@Service
public class CiudadServiceImpl implements CiudadService {

    private final CiudadRepository ciudadRepository;
    private final CiudadMapper ciudadMapper;
    private final AuditoriaService auditoriaService;

    public CiudadServiceImpl(
            CiudadRepository ciudadRepository,
            CiudadMapper ciudadMapper,
            AuditoriaService auditoriaService) {

        this.ciudadRepository = ciudadRepository;
        this.ciudadMapper = ciudadMapper;
        this.auditoriaService = auditoriaService;
    }

    @Override
    @Transactional
    public CiudadDtoResponse guardar(CiudadDtoRequest dto) {

        if (ciudadRepository.existsByNombreIgnoreCase(dto.nombre())) {
            throw new RecursoDuplicadoException(
                    "Ya existe una ciudad con ese nombre"
            );
        }

        Ciudad ciudad = ciudadRepository.save(
                ciudadMapper.DtoToEntity(dto)
        );

        CiudadDtoResponse respuesta = ciudadMapper.entityToDto(ciudad);

        auditoriaService.registrar(
                TipoOperacion.INSERT,
                "CIUDAD",
                ciudad.getId(),
                null,
                respuesta
        );

        return respuesta;
    }

    @Override
    @Transactional
    public CiudadDtoResponse actualizar(CiudadDtoRequest dto, Long id) {

        Ciudad ciudad = buscarEntidad(id);

        ciudadRepository.findByNombreIgnoreCase(dto.nombre())
                .filter(otra -> !otra.getId().equals(id))
                .ifPresent(otra -> {
                    throw new RecursoDuplicadoException(
                            "Ya existe una ciudad con ese nombre"
                    );
                });

        // Guardamos el estado anterior
        CiudadDtoResponse anterior = ciudadMapper.entityToDto(ciudad);

        // Actualizamos
        ciudadMapper.updateEntityToDto(ciudad, dto);

        // Guardamos el estado nuevo
        CiudadDtoResponse actualizado = ciudadMapper.entityToDto(ciudad);

        auditoriaService.registrar(
                TipoOperacion.UPDATE,
                "CIUDAD",
                ciudad.getId(),
                anterior,
                actualizado
        );

        return actualizado;
    }

    @Override
    @Transactional
    public void eliminar(Long id) {

        Ciudad ciudad = buscarEntidad(id);

        // Capturamos los datos antes de eliminar
        CiudadDtoResponse anterior = ciudadMapper.entityToDto(ciudad);

        ciudadRepository.delete(ciudad);

        auditoriaService.registrar(
                TipoOperacion.DELETE,
                "CIUDAD",
                id,
                anterior,
                null
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<CiudadDtoResponse> listar() {
        return ciudadRepository.findAll().stream().map(ciudadMapper::entityToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CiudadDtoResponse buscarId(Long id) {
        return ciudadMapper.entityToDto(buscarEntidad(id));
    }

    private Ciudad buscarEntidad(Long id) {
        return ciudadRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ciudad no encontrada: " + id));
    }
}
