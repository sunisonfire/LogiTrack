package com.DannaS2.LogiTrack.service.impl;

import com.DannaS2.LogiTrack.dto.request.MarcaDtoRequest;
import com.DannaS2.LogiTrack.dto.response.MarcaDtoResponse;
import com.DannaS2.LogiTrack.exception.RecursoDuplicadoException;
import com.DannaS2.LogiTrack.exception.RecursoNoEncontradoException;
import com.DannaS2.LogiTrack.mapper.MarcaMapper;
import com.DannaS2.LogiTrack.model.Marca;
import com.DannaS2.LogiTrack.repository.MarcaRepository;
import com.DannaS2.LogiTrack.service.MarcaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MarcaServiceImpl implements MarcaService {

    private final MarcaRepository marcaRepository;
    private final MarcaMapper marcaMapper;

    public MarcaServiceImpl(MarcaRepository marcaRepository, MarcaMapper marcaMapper) {
        this.marcaRepository = marcaRepository;
        this.marcaMapper = marcaMapper;
    }

    @Override
    @Transactional
    public MarcaDtoResponse guardar(MarcaDtoRequest dto) {
        if (marcaRepository.existsByNombreIgnoreCase(dto.nombre())) {
            throw new RecursoDuplicadoException("Ya existe una marca con ese nombre");
        }
        return marcaMapper.entityToDto(marcaRepository.save(marcaMapper.DtoToEntity(dto)));
    }

    @Override
    @Transactional
    public MarcaDtoResponse actualizar(MarcaDtoRequest dto, Long id) {
        Marca marca = buscarEntidad(id);
        marcaRepository.findByNombreIgnoreCase(dto.nombre())
                .filter(otra -> !otra.getId().equals(id))
                .ifPresent(otra -> { throw new RecursoDuplicadoException("Ya existe una marca con ese nombre"); });
        marcaMapper.updateEntityToDto(marca, dto);
        return marcaMapper.entityToDto(marca);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        marcaRepository.delete(buscarEntidad(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarcaDtoResponse> listar() {
        return marcaRepository.findAll().stream().map(marcaMapper::entityToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MarcaDtoResponse buscarId(Long id) {
        return marcaMapper.entityToDto(buscarEntidad(id));
    }

    private Marca buscarEntidad(Long id) {
        return marcaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Marca no encontrada: " + id));
    }
}
