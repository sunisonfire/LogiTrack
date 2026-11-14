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

import java.util.List;

@Service
public class CiudadServiceImpl implements CiudadService {

    private final CiudadRepository ciudadRepository;
    private final CiudadMapper ciudadMapper;

    public CiudadServiceImpl(CiudadRepository ciudadRepository, CiudadMapper ciudadMapper) {
        this.ciudadRepository = ciudadRepository;
        this.ciudadMapper = ciudadMapper;
    }

    @Override
    @Transactional
    public CiudadDtoResponse guardar(CiudadDtoRequest dto) {
        if (ciudadRepository.existsByNombreIgnoreCase(dto.nombre())) {
            throw new RecursoDuplicadoException("Ya existe una ciudad con ese nombre");
        }
        return ciudadMapper.entityToDto(ciudadRepository.save(ciudadMapper.DtoToEntity(dto)));
    }

    @Override
    @Transactional
    public CiudadDtoResponse actualizar(CiudadDtoRequest dto, Long id) {
        Ciudad ciudad = buscarEntidad(id);
        ciudadRepository.findByNombreIgnoreCase(dto.nombre())
                .filter(otra -> !otra.getId().equals(id))   // otra ciudad distinta con ese nombre
                .ifPresent(otra -> { throw new RecursoDuplicadoException("Ya existe una ciudad con ese nombre"); });
        ciudadMapper.updateEntityToDto(ciudad, dto);
        return ciudadMapper.entityToDto(ciudad);   // al ser @Transactional, el cambio se guarda solo
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        ciudadRepository.delete(buscarEntidad(id));
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
