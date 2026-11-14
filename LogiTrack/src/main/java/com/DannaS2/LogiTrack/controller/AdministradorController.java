package com.DannaS2.LogiTrack.controller;

import com.DannaS2.LogiTrack.dto.request.AdministradorDtoRequest;
import com.DannaS2.LogiTrack.dto.response.AdministradorDtoResponse;
import com.DannaS2.LogiTrack.service.AdministradorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administrador")
@RequiredArgsConstructor
public class AdministradorController {

    private final AdministradorService administradorService;

    // POST http://localhost:8080/api/administrador
    @PostMapping
    public ResponseEntity<AdministradorDtoResponse> guardar(@Valid @RequestBody AdministradorDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(administradorService.guardar(dto));
    }

    // GET http://localhost:8080/api/administrador
    @GetMapping
    public ResponseEntity<List<AdministradorDtoResponse>> listar() {
        return ResponseEntity.ok(administradorService.listar());
    }

    // GET http://localhost:8080/api/administrador/1
    @GetMapping("/{id}")
    public ResponseEntity<AdministradorDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(administradorService.buscarId(id));
    }

    // PUT http://localhost:8080/api/administrador/1
    @PutMapping("/{id}")
    public ResponseEntity<AdministradorDtoResponse> actualizar(@Valid @RequestBody AdministradorDtoRequest dto,
                                                               @PathVariable Long id) {
        return ResponseEntity.ok(administradorService.actualizar(dto, id));
    }

    // DELETE http://localhost:8080/api/administrador/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        administradorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}