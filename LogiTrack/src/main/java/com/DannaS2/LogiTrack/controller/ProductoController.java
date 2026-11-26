
package com.DannaS2.LogiTrack.controller;

import com.DannaS2.LogiTrack.dto.request.ProductoDtoRequest;
import com.DannaS2.LogiTrack.dto.response.ProductoDtoResponse;
import com.DannaS2.LogiTrack.model.Categoria;
import com.DannaS2.LogiTrack.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/producto")
@RequiredArgsConstructor
@Tag(
        name = "Productos",
        description = "Operaciones para gestionar y consultar los productos"
)
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    @Operation(
            summary = "Registrar un producto",
            description = "Registra un nuevo producto con su nombre, descripción, precio, categoría y marca."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Producto creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Los datos enviados no cumplen las validaciones"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para registrar productos")
    })
    public ResponseEntity<ProductoDtoResponse> guardar(
            @Valid @RequestBody ProductoDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.guardar(dto));
    }

    @GetMapping
    @Operation(
            summary = "Listar productos",
            description = "Obtiene la lista de productos registrados en el sistema."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de productos obtenida correctamente"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar productos")
    })
    public ResponseEntity<List<ProductoDtoResponse>> listar() {
        return ResponseEntity.ok(productoService.listar());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar producto por ID",
            description = "Obtiene los datos de un producto utilizando su identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar productos"),
            @ApiResponse(responseCode = "404", description = "No se encontró un producto con ese ID")
    })
    public ResponseEntity<ProductoDtoResponse> buscar(
            @Parameter(description = "Identificador del producto", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(productoService.buscarId(id));
    }

    @GetMapping("/filtroCategoria")
    @Operation(
            summary = "Filtrar productos por categoría",
            description = "Obtiene los productos que pertenecen a la categoría indicada."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Productos filtrados correctamente"),
            @ApiResponse(responseCode = "400", description = "La categoría indicada no es válida"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar productos")
    })
    public ResponseEntity<List<ProductoDtoResponse>> buscarCategoria(
            @Parameter(description = "Categoría por la que se desea filtrar", example = "LABIALES")
            @RequestParam Categoria categoria) {
        return ResponseEntity.ok(productoService.buscarCategoria(categoria));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar un producto",
            description = "Actualiza los datos de un producto existente utilizando su identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Los datos enviados no cumplen las validaciones"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para actualizar productos"),
            @ApiResponse(responseCode = "404", description = "No se encontró un producto con ese ID")
    })
    public ResponseEntity<ProductoDtoResponse> actualizar(
            @Valid @RequestBody ProductoDtoRequest dto,
            @Parameter(description = "Identificador del producto que se actualizará", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(productoService.actualizar(dto, id));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar un producto",
            description = "Elimina un producto utilizando su identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para eliminar productos"),
            @ApiResponse(responseCode = "404", description = "No se encontró un producto con ese ID")
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "Identificador del producto que se eliminará", example = "1")
            @PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    //EXAMEN
    //3.
    @GetMapping("/filtroNombre")
    @Operation(
            summary = "Buscar productos por nombre",
            description = "Busca productos cuyo nombre coincida con el texto proporcionado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Búsqueda realizada correctamente"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar productos")
    })
    public ResponseEntity<List<ProductoDtoResponse>> buscarNombre(
            @Parameter(description = "Texto que se desea buscar en el nombre del producto", example = "mate")
            @RequestParam String texto) {
        return ResponseEntity.ok(productoService.buscarNombre(texto));
    }


}
