package com.DannaS2.LogiTrack.controller;

import com.DannaS2.LogiTrack.dto.request.InventarioBodegaDtoRequest;
import com.DannaS2.LogiTrack.dto.response.InventarioBodegaDtoResponse;
import com.DannaS2.LogiTrack.service.InventarioBodegaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
@Tag(
        name = "Inventario de bodegas",
        description = "Operaciones para gestionar y consultar el stock de cada producto en cada bodega"
)
public class InventarioBodegaController {

    private final InventarioBodegaService inventarioService;

    // POST http://localhost:8080/api/inventario
    @PostMapping
    @Operation(
            summary = "Registrar un inventario",
            description = "Registra cuántas unidades de un producto hay en una bodega."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Inventario creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Los datos enviados no cumplen las validaciones"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para registrar inventarios"),
            @ApiResponse(responseCode = "404", description = "No se encontró la bodega o el producto indicado"),
            @ApiResponse(responseCode = "409", description = "Ese producto ya tiene inventario en esa bodega")
    })
    public ResponseEntity<InventarioBodegaDtoResponse> guardar(@Valid @RequestBody InventarioBodegaDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioService.guardar(dto));
    }

    // GET http://localhost:8080/api/inventario
    @GetMapping
    @Operation(
            summary = "Listar inventarios",
            description = "Obtiene la lista de inventarios registrados en todas las bodegas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de inventarios obtenida correctamente"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar inventarios")
    })
    public ResponseEntity<List<InventarioBodegaDtoResponse>> listar() {
        return ResponseEntity.ok(inventarioService.listar());
    }

    // GET http://localhost:8080/api/inventario/1
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar inventario por ID",
            description = "Obtiene los datos de un inventario utilizando su identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inventario encontrado"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar inventarios"),
            @ApiResponse(responseCode = "404", description = "No se encontró un inventario con ese ID")
    })
    public ResponseEntity<InventarioBodegaDtoResponse> buscar(
            @Parameter(description = "Identificador del inventario", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.buscarId(id));
    }


    // GET http://localhost:8080/api/inventario/filtroProducto?productoId=1
    @GetMapping("/filtroProducto")
    @Operation(
            summary = "Filtrar inventarios por producto",
            description = "Obtiene los inventarios del producto indicado en todas las bodegas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inventarios filtrados correctamente"),
            @ApiResponse(responseCode = "400", description = "El ID del producto no es válido o no se envió"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar inventarios")
    })
    public ResponseEntity<List<InventarioBodegaDtoResponse>> buscarProducto(
            @Parameter(description = "Identificador del producto por el que se desea filtrar", example = "1")
            @RequestParam Long productoId) {
        return ResponseEntity.ok(inventarioService.buscarProducto(productoId));
    }

    // GET http://localhost:8080/api/inventario/stockBajo?limite=10
    @GetMapping("/stockBajo")
    @Operation(
            summary = "Listar inventarios con stock bajo",
            description = "Obtiene los inventarios cuya cantidad es menor que el límite indicado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inventarios con stock bajo obtenidos correctamente"),
            @ApiResponse(responseCode = "400", description = "El límite no es válido o no se envió"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar inventarios")
    })
    public ResponseEntity<List<InventarioBodegaDtoResponse>> buscarStockBajo(
            @Parameter(description = "Cantidad de referencia: se listan los inventarios con menos unidades que este valor", example = "10")
            @RequestParam Integer limite) {
        return ResponseEntity.ok(inventarioService.buscarStockBajo(limite));
    }

    // PUT http://localhost:8080/api/inventario/1
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar un inventario",
            description = "Actualiza la cantidad de un inventario existente. No se puede cambiar su bodega ni su producto."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inventario actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Los datos enviados no cumplen las validaciones o se intentó cambiar la bodega o el producto"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para actualizar inventarios"),
            @ApiResponse(responseCode = "404", description = "No se encontró el inventario, la bodega o el producto indicado")
    })
    public ResponseEntity<InventarioBodegaDtoResponse> actualizar(@Valid @RequestBody InventarioBodegaDtoRequest dto,
                                                                  @Parameter(description = "Identificador del inventario que se actualizará", example = "1")
                                                                  @PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.actualizar(dto, id));
    }

    // DELETE http://localhost:8080/api/inventario/1
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar un inventario",
            description = "Elimina un inventario utilizando su identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Inventario eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para eliminar inventarios"),
            @ApiResponse(responseCode = "404", description = "No se encontró un inventario con ese ID")
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "Identificador del inventario que se eliminará", example = "1")
            @PathVariable Long id) {
        inventarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    //EXAMEN
    //2.
    // GET http://localhost:8080/api/inventario/filtroBodega?bodegaId=1
    @GetMapping("/filtroBodega")
    @Operation(
            summary = "Filtrar inventarios por bodega",
            description = "Obtiene los inventarios de la bodega indicada."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inventarios filtrados correctamente"),
            @ApiResponse(responseCode = "400", description = "El ID de la bodega no es válido o no se envió"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar inventarios")
    })
    public ResponseEntity<List<InventarioBodegaDtoResponse>> buscarBodega(
            @Parameter(description = "Identificador de la bodega por la que se desea filtrar", example = "1")
            @RequestParam Long bodegaId) {
        return ResponseEntity.ok(inventarioService.buscarBodega(bodegaId));
    }
}
