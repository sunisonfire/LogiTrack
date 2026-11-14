package com.DannaS2.LogiTrack.service.impl;

import com.DannaS2.LogiTrack.dto.response.PersonaDtoResponse;
import com.DannaS2.LogiTrack.exception.RecursoNoEncontradoException;
import com.DannaS2.LogiTrack.mapper.PersonaMapper;
import com.DannaS2.LogiTrack.repository.PersonaRepository;
import com.DannaS2.LogiTrack.service.PersonaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersonaServiceImpl implements PersonaService {

    private final PersonaRepository personaRepository;
    private final PersonaMapper personaMapper;

    public PersonaServiceImpl(PersonaRepository personaRepository, PersonaMapper personaMapper) {
        this.personaRepository = personaRepository;
        this.personaMapper = personaMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonaDtoResponse> listar() {
        return personaRepository.findAll().stream().map(personaMapper::entityToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PersonaDtoResponse buscarId(Long id) {
        return personaRepository.findById(id).map(personaMapper::entityToDto)
                .orElseThrow(() -> new RecursoNoEncontradoException("Persona no encontrada: " + id));
    }
}
