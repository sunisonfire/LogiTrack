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
import com.DannaS2.LogiTrack.model.TipoOperacion;
import com.DannaS2.LogiTrack.service.AuditoriaService;

import java.util.List;

@Service
public class MarcaServiceImpl implements MarcaService {

    private final MarcaRepository marcaRepository;
    private final MarcaMapper marcaMapper;
    private final AuditoriaService auditoriaService;


    public MarcaServiceImpl(
            MarcaRepository marcaRepository,
            MarcaMapper marcaMapper,
            AuditoriaService auditoriaService) {

        this.marcaRepository = marcaRepository;
        this.marcaMapper = marcaMapper;
        this.auditoriaService = auditoriaService;
    }

    @Override
    @Transactional
    public MarcaDtoResponse guardar(MarcaDtoRequest dto) {

        if (marcaRepository.existsByNombreIgnoreCase(dto.nombre())) {
            throw new RecursoDuplicadoException(
                    "Ya existe una marca con ese nombre"
            );
        }

        Marca guardada = marcaRepository.save(
                marcaMapper.DtoToEntity(dto)
        );

        MarcaDtoResponse response = marcaMapper.entityToDto(guardada);

        auditoriaService.registrar(
                TipoOperacion.INSERT,
                "MARCA",
                guardada.getId(),
                null,
                response
        );

        return response;
    }

    @Override
    @Transactional
    public MarcaDtoResponse actualizar(MarcaDtoRequest dto, Long id) {

        Marca marca = buscarEntidad(id);

        marcaRepository.findByNombreIgnoreCase(dto.nombre())
                .filter(otra -> !otra.getId().equals(id))
                .ifPresent(otra -> {
                    throw new RecursoDuplicadoException(
                            "Ya existe una marca con ese nombre"
                    );
                });

        MarcaDtoResponse anterior = marcaMapper.entityToDto(marca);

        marcaMapper.updateEntityToDto(marca, dto);

        MarcaDtoResponse actualizado = marcaMapper.entityToDto(marca);

        auditoriaService.registrar(
                TipoOperacion.UPDATE,
                "MARCA",
                marca.getId(),
                anterior,
                actualizado
        );

        return actualizado;
    }

    @Override
    @Transactional
    public void eliminar(Long id) {

        Marca marca = buscarEntidad(id);

        MarcaDtoResponse anterior = marcaMapper.entityToDto(marca);

        marcaRepository.delete(marca);

        auditoriaService.registrar(
                TipoOperacion.DELETE,
                "MARCA",
                id,
                anterior,
                null
        );
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
