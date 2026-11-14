package com.DannaS2.LogiTrack.controller;

import com.DannaS2.LogiTrack.dto.request.EmpleadoDtoRequest;
import com.DannaS2.LogiTrack.dto.response.EmpleadoDtoResponse;
import com.DannaS2.LogiTrack.service.EmpleadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleado")
@RequiredArgsConstructor
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    // POST http://localhost:8080/api/empleado
    @PostMapping
    public ResponseEntity<EmpleadoDtoResponse> guardar(@Valid @RequestBody EmpleadoDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(empleadoService.guardar(dto));
    }

    // GET http://localhost:8080/api/empleado
    @GetMapping
    public ResponseEntity<List<EmpleadoDtoResponse>> listar() {
        return ResponseEntity.ok(empleadoService.listar());
    }

    // GET http://localhost:8080/api/empleado/1
    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(empleadoService.buscarId(id));
    }

    // PUT http://localhost:8080/api/empleado/1
    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoDtoResponse> actualizar(@Valid @RequestBody EmpleadoDtoRequest dto,
                                                               @PathVariable Long id) {
        return ResponseEntity.ok(empleadoService.actualizar(dto, id));
    }

    // DELETE http://localhost:8080/api/empleado/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        empleadoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // EmpleadoController
    // GET /api/empleado/filtroBodega?bodegaId=1
    @GetMapping("/filtroBodega")
    public ResponseEntity<List<EmpleadoDtoResponse>> buscarBodega(@RequestParam Long bodegaId) {
        return ResponseEntity.ok(empleadoService.buscarBodega(bodegaId));
    }
}