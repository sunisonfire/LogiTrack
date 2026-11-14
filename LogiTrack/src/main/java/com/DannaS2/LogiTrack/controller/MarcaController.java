package com.DannaS2.LogiTrack.controller;

import com.DannaS2.LogiTrack.dto.request.MarcaDtoRequest;
import com.DannaS2.LogiTrack.dto.response.MarcaDtoResponse;
import com.DannaS2.LogiTrack.service.MarcaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marca")
@RequiredArgsConstructor
public class MarcaController {

    private final MarcaService marcaService;

    // POST http://localhost:8080/api/marca
    @PostMapping
    public ResponseEntity<MarcaDtoResponse> guardar(@Valid @RequestBody MarcaDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(marcaService.guardar(dto));
    }

    // GET http://localhost:8080/api/marca
    @GetMapping
    public ResponseEntity<List<MarcaDtoResponse>> listar() {
        return ResponseEntity.ok(marcaService.listar());
    }

    // GET http://localhost:8080/api/marca/1
    @GetMapping("/{id}")
    public ResponseEntity<MarcaDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(marcaService.buscarId(id));
    }

    // PUT http://localhost:8080/api/marca/1
    @PutMapping("/{id}")
    public ResponseEntity<MarcaDtoResponse> actualizar(@Valid @RequestBody MarcaDtoRequest dto,
                                                        @PathVariable Long id) {
        return ResponseEntity.ok(marcaService.actualizar(dto, id));
    }

    // DELETE http://localhost:8080/api/marca/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        marcaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}