package com.DannaS2.LogiTrack.controller;

import com.DannaS2.LogiTrack.dto.request.InventarioBodegaDtoRequest;
import com.DannaS2.LogiTrack.dto.response.InventarioBodegaDtoResponse;
import com.DannaS2.LogiTrack.service.InventarioBodegaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
public class InventarioBodegaController {

    private final InventarioBodegaService inventarioService;

    // POST http://localhost:8080/api/inventario
    @PostMapping
    public ResponseEntity<InventarioBodegaDtoResponse> guardar(@Valid @RequestBody InventarioBodegaDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioService.guardar(dto));
    }

    // GET http://localhost:8080/api/inventario
    @GetMapping
    public ResponseEntity<List<InventarioBodegaDtoResponse>> listar() {
        return ResponseEntity.ok(inventarioService.listar());
    }

    // GET http://localhost:8080/api/inventario/1
    @GetMapping("/{id}")
    public ResponseEntity<InventarioBodegaDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.buscarId(id));
    }

    // GET http://localhost:8080/api/inventario/filtroBodega?bodegaId=1
    @GetMapping("/filtroBodega")
    public ResponseEntity<List<InventarioBodegaDtoResponse>> buscarBodega(@RequestParam Long bodegaId) {
        return ResponseEntity.ok(inventarioService.buscarBodega(bodegaId));
    }

    // GET http://localhost:8080/api/inventario/filtroProducto?productoId=1
    @GetMapping("/filtroProducto")
    public ResponseEntity<List<InventarioBodegaDtoResponse>> buscarProducto(@RequestParam Long productoId) {
        return ResponseEntity.ok(inventarioService.buscarProducto(productoId));
    }

    // GET http://localhost:8080/api/inventario/stockBajo?limite=10
    @GetMapping("/stockBajo")
    public ResponseEntity<List<InventarioBodegaDtoResponse>> buscarStockBajo(@RequestParam Integer limite) {
        return ResponseEntity.ok(inventarioService.buscarStockBajo(limite));
    }

    // PUT http://localhost:8080/api/inventario/1
    @PutMapping("/{id}")
    public ResponseEntity<InventarioBodegaDtoResponse> actualizar(@Valid @RequestBody InventarioBodegaDtoRequest dto,
                                                                  @PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.actualizar(dto, id));
    }

    // DELETE http://localhost:8080/api/inventario/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        inventarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}