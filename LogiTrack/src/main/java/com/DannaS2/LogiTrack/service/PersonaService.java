package com.DannaS2.LogiTrack.service;

import com.DannaS2.LogiTrack.dto.response.PersonaDtoResponse;

import java.util.List;

public interface PersonaService {
    List<PersonaDtoResponse> listar();
    PersonaDtoResponse buscarId(Long id);
}
