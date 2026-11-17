package com.DannaS2.LogiTrack.controller;

import com.DannaS2.LogiTrack.dto.request.CiudadDtoRequest;
import com.DannaS2.LogiTrack.dto.response.CiudadDtoResponse;
import com.DannaS2.LogiTrack.service.CiudadService;
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
@RequestMapping("/api/ciudad")
@RequiredArgsConstructor
@Tag(
        name = "Ciudades",
        description = "Operaciones para gestionar y consultar las ciudades donde hay bodegas"
)
public class CiudadController {

    private final CiudadService ciudadService;

    // POST http://localhost:8080/api/ciudad
    @PostMapping
    @Operation(
            summary = "Registrar una ciudad",
            description = "Registra una nueva ciudad."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ciudad creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Los datos enviados no cumplen las validaciones"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para registrar ciudades"),
            @ApiResponse(responseCode = "409", description = "Ya existe una ciudad con ese nombre")
    })
    public ResponseEntity<CiudadDtoResponse> guardar(@Valid @RequestBody CiudadDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ciudadService.guardar(dto));
    }

    // GET http://localhost:8080/api/ciudad
    @GetMapping
    @Operation(
            summary = "Listar ciudades",
            description = "Obtiene la lista de ciudades registradas en el sistema."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de ciudades obtenida correctamente"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar ciudades")
    })
    public ResponseEntity<List<CiudadDtoResponse>> listar() {
        return ResponseEntity.ok(ciudadService.listar());
    }

    // GET http://localhost:8080/api/ciudad/1
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar ciudad por ID",
            description = "Obtiene los datos de una ciudad utilizando su identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ciudad encontrada"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar ciudades"),
            @ApiResponse(responseCode = "404", description = "No se encontró una ciudad con ese ID")
    })
    public ResponseEntity<CiudadDtoResponse> buscar(
            @Parameter(description = "Identificador de la ciudad", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(ciudadService.buscarId(id));
    }

    // PUT http://localhost:8080/api/ciudad/1
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar una ciudad",
            description = "Actualiza los datos de una ciudad existente utilizando su identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ciudad actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Los datos enviados no cumplen las validaciones"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para actualizar ciudades"),
            @ApiResponse(responseCode = "404", description = "No se encontró una ciudad con ese ID"),
            @ApiResponse(responseCode = "409", description = "Ya existe una ciudad con ese nombre")
    })
    public ResponseEntity<CiudadDtoResponse> actualizar(@Valid @RequestBody CiudadDtoRequest dto,
                                                        @Parameter(description = "Identificador de la ciudad que se actualizará", example = "1")
                                                        @PathVariable Long id) {
        return ResponseEntity.ok(ciudadService.actualizar(dto, id));
    }

    // DELETE http://localhost:8080/api/ciudad/1
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar una ciudad",
            description = "Elimina una ciudad utilizando su identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Ciudad eliminada correctamente"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para eliminar ciudades"),
            @ApiResponse(responseCode = "404", description = "No se encontró una ciudad con ese ID"),
            @ApiResponse(responseCode = "409", description = "La ciudad está relacionada con otros datos y no se puede eliminar")
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "Identificador de la ciudad que se eliminará", example = "1")
            @PathVariable Long id) {
        ciudadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
