package com.DannaS2.LogiTrack.controller;

import com.DannaS2.LogiTrack.dto.response.PersonaDtoResponse;
import com.DannaS2.LogiTrack.service.PersonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persona")
@RequiredArgsConstructor
public class PersonaController {

    private final PersonaService personaService;

    // GET http://localhost:8080/api/persona
    @GetMapping
    public ResponseEntity<List<PersonaDtoResponse>> listar() {
        return ResponseEntity.ok(personaService.listar());
    }

    // GET http://localhost:8080/api/persona/1
    @GetMapping("/{id}")
    public ResponseEntity<PersonaDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(personaService.buscarId(id));
    }
}