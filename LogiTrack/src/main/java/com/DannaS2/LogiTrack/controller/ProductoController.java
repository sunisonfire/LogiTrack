package com.DannaS2.LogiTrack.controller;

import com.DannaS2.LogiTrack.dto.request.ProductoDtoRequest;
import com.DannaS2.LogiTrack.dto.response.ProductoDtoResponse;
import com.DannaS2.LogiTrack.model.Categoria;
import com.DannaS2.LogiTrack.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/producto")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    // POST http://localhost:8080/api/producto
    @PostMapping
    public ResponseEntity<ProductoDtoResponse> guardar(@Valid @RequestBody ProductoDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.guardar(dto));
    }

    // GET http://localhost:8080/api/producto
    @GetMapping
    public ResponseEntity<List<ProductoDtoResponse>> listar() {
        return ResponseEntity.ok(productoService.listar());
    }

    // GET http://localhost:8080/api/producto/1
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.buscarId(id));
    }

    // GET http://localhost:8080/api/producto/filtroCategoria?categoria=LABIALES
    @GetMapping("/filtroCategoria")
    public ResponseEntity<List<ProductoDtoResponse>> buscarCategoria(@RequestParam Categoria categoria) {
        return ResponseEntity.ok(productoService.buscarCategoria(categoria));
    }

    // GET http://localhost:8080/api/producto/filtroNombre?texto=mate
    @GetMapping("/filtroNombre")
    public ResponseEntity<List<ProductoDtoResponse>> buscarNombre(@RequestParam String texto) {
        return ResponseEntity.ok(productoService.buscarNombre(texto));
    }

    // PUT http://localhost:8080/api/producto/1
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDtoResponse> actualizar(@Valid @RequestBody ProductoDtoRequest dto,
                                                          @PathVariable Long id) {
        return ResponseEntity.ok(productoService.actualizar(dto, id));
    }

    // DELETE http://localhost:8080/api/producto/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}