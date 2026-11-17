package com.DannaS2.LogiTrack.controller;

import com.DannaS2.LogiTrack.dto.request.MarcaDtoRequest;
import com.DannaS2.LogiTrack.dto.response.MarcaDtoResponse;
import com.DannaS2.LogiTrack.service.MarcaService;
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
@RequestMapping("/api/marca")
@RequiredArgsConstructor
@Tag(
        name = "Marcas",
        description = "Operaciones para gestionar y consultar las marcas de los productos"
)
public class MarcaController {

    private final MarcaService marcaService;

    // POST http://localhost:8080/api/marca
    @PostMapping
    @Operation(
            summary = "Registrar una marca",
            description = "Registra una nueva marca de productos."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Marca creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Los datos enviados no cumplen las validaciones"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para registrar marcas"),
            @ApiResponse(responseCode = "409", description = "Ya existe una marca con ese nombre")
    })
    public ResponseEntity<MarcaDtoResponse> guardar(@Valid @RequestBody MarcaDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(marcaService.guardar(dto));
    }

    // GET http://localhost:8080/api/marca
    @GetMapping
    @Operation(
            summary = "Listar marcas",
            description = "Obtiene la lista de marcas registradas en el sistema."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de marcas obtenida correctamente"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar marcas")
    })
    public ResponseEntity<List<MarcaDtoResponse>> listar() {
        return ResponseEntity.ok(marcaService.listar());
    }

    // GET http://localhost:8080/api/marca/1
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar marca por ID",
            description = "Obtiene los datos de una marca utilizando su identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Marca encontrada"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar marcas"),
            @ApiResponse(responseCode = "404", description = "No se encontró una marca con ese ID")
    })
    public ResponseEntity<MarcaDtoResponse> buscar(
            @Parameter(description = "Identificador de la marca", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(marcaService.buscarId(id));
    }

    // PUT http://localhost:8080/api/marca/1
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar una marca",
            description = "Actualiza los datos de una marca existente utilizando su identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Marca actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Los datos enviados no cumplen las validaciones"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para actualizar marcas"),
            @ApiResponse(responseCode = "404", description = "No se encontró una marca con ese ID"),
            @ApiResponse(responseCode = "409", description = "Ya existe una marca con ese nombre")
    })
    public ResponseEntity<MarcaDtoResponse> actualizar(@Valid @RequestBody MarcaDtoRequest dto,
                                                        @Parameter(description = "Identificador de la marca que se actualizará", example = "1")
                                                        @PathVariable Long id) {
        return ResponseEntity.ok(marcaService.actualizar(dto, id));
    }

    // DELETE http://localhost:8080/api/marca/1
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar una marca",
            description = "Elimina una marca utilizando su identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Marca eliminada correctamente"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para eliminar marcas"),
            @ApiResponse(responseCode = "404", description = "No se encontró una marca con ese ID"),
            @ApiResponse(responseCode = "409", description = "La marca está relacionada con otros datos y no se puede eliminar")
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "Identificador de la marca que se eliminará", example = "1")
            @PathVariable Long id) {
        marcaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
