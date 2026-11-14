package com.DannaS2.LogiTrack.controller;

import com.DannaS2.LogiTrack.dto.request.BodegaDtoRequest;
import com.DannaS2.LogiTrack.dto.response.BodegaDtoResponse;
import com.DannaS2.LogiTrack.service.BodegaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bodega")
@RequiredArgsConstructor
public class BodegaController {

    private final BodegaService bodegaService;

    // POST http://localhost:8080/api/bodega
    @PostMapping
    public ResponseEntity<BodegaDtoResponse> guardar(@Valid @RequestBody BodegaDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bodegaService.guardar(dto));
    }

    // GET http://localhost:8080/api/bodega
    @GetMapping
    public ResponseEntity<List<BodegaDtoResponse>> listar() {
        return ResponseEntity.ok(bodegaService.listar());
    }

    // GET http://localhost:8080/api/bodega/1
    @GetMapping("/{id}")
    public ResponseEntity<BodegaDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(bodegaService.buscarId(id));
    }

    // PUT http://localhost:8080/api/bodega/1
    @PutMapping("/{id}")
    public ResponseEntity<BodegaDtoResponse> actualizar(@Valid @RequestBody BodegaDtoRequest dto,
                                                          @PathVariable Long id) {
        return ResponseEntity.ok(bodegaService.actualizar(dto, id));
    }

    // DELETE http://localhost:8080/api/bodega/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        bodegaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // BodegaController
    // GET /api/bodega/filtroCiudad?ciudadId=1
    @GetMapping("/filtroCiudad")
    public ResponseEntity<List<BodegaDtoResponse>> buscarCiudad(@RequestParam Long ciudadId) {
        return ResponseEntity.ok(bodegaService.buscarCiudad(ciudadId));
    }
}