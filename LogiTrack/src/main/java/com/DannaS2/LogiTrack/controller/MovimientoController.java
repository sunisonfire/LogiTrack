package com.DannaS2.LogiTrack.controller;

import com.DannaS2.LogiTrack.dto.request.MovimientoDtoRequest;
import com.DannaS2.LogiTrack.dto.response.MovimientoDtoResponse;
import com.DannaS2.LogiTrack.model.TipoMovimiento;
import com.DannaS2.LogiTrack.service.MovimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/movimiento")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    // POST http://localhost:8080/api/movimiento
    @PostMapping
    public ResponseEntity<MovimientoDtoResponse> guardar(@Valid @RequestBody MovimientoDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movimientoService.guardar(dto));
    }

    // GET http://localhost:8080/api/movimiento
    @GetMapping
    public ResponseEntity<List<MovimientoDtoResponse>> listar() {
        return ResponseEntity.ok(movimientoService.listar());
    }

    // GET http://localhost:8080/api/movimiento/1
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(movimientoService.buscarId(id));
    }

    // GET http://localhost:8080/api/movimiento/filtroTipo?tipo=ENTRADA
    @GetMapping("/filtroTipo")
    public ResponseEntity<List<MovimientoDtoResponse>> buscarTipo(@RequestParam TipoMovimiento tipo) {
        return ResponseEntity.ok(movimientoService.buscarTipo(tipo));
    }

    // GET http://localhost:8080/api/movimiento/filtroPersona?personaId=1
    @GetMapping("/filtroPersona")
    public ResponseEntity<List<MovimientoDtoResponse>> buscarPersona(@RequestParam Long personaId) {
        return ResponseEntity.ok(movimientoService.buscarPersona(personaId));
    }

    // GET http://localhost:8080/api/movimiento/filtroOrigen?bodegaId=1
    @GetMapping("/filtroOrigen")
    public ResponseEntity<List<MovimientoDtoResponse>> buscarOrigen(@RequestParam Long bodegaId) {
        return ResponseEntity.ok(movimientoService.buscarBodegaOrigen(bodegaId));
    }

    // GET http://localhost:8080/api/movimiento/filtroDestino?bodegaId=1
    @GetMapping("/filtroDestino")
    public ResponseEntity<List<MovimientoDtoResponse>> buscarDestino(@RequestParam Long bodegaId) {
        return ResponseEntity.ok(movimientoService.buscarBodegaDestino(bodegaId));
    }

    // GET http://localhost:8080/api/movimiento/filtroFechas?desde=2026-11-01T00:00:00&hasta=2026-11-30T23:59:59
    @GetMapping("/filtroFechas")
    public ResponseEntity<List<MovimientoDtoResponse>> buscarFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {
        return ResponseEntity.ok(movimientoService.buscarFechas(desde, hasta));
    }
}