package com.DannaS2.LogiTrack.controller;

import com.DannaS2.LogiTrack.dto.request.CiudadDtoRequest;
import com.DannaS2.LogiTrack.dto.response.CiudadDtoResponse;
import com.DannaS2.LogiTrack.service.CiudadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ciudad")
@RequiredArgsConstructor
public class CiudadController {

    private final CiudadService ciudadService;

    // POST http://localhost:8080/api/ciudad
    @PostMapping
    public ResponseEntity<CiudadDtoResponse> guardar(@Valid @RequestBody CiudadDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ciudadService.guardar(dto));
    }

    // GET http://localhost:8080/api/ciudad
    @GetMapping
    public ResponseEntity<List<CiudadDtoResponse>> listar() {
        return ResponseEntity.ok(ciudadService.listar());
    }

    // GET http://localhost:8080/api/ciudad/1
    @GetMapping("/{id}")
    public ResponseEntity<CiudadDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(ciudadService.buscarId(id));
    }

    // PUT http://localhost:8080/api/ciudad/1
    @PutMapping("/{id}")
    public ResponseEntity<CiudadDtoResponse> actualizar(@Valid @RequestBody CiudadDtoRequest dto,
                                                        @PathVariable Long id) {
        return ResponseEntity.ok(ciudadService.actualizar(dto, id));
    }

    // DELETE http://localhost:8080/api/ciudad/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ciudadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}