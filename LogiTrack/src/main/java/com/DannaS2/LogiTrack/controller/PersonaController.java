package com.DannaS2.LogiTrack.controller;

import com.DannaS2.LogiTrack.dto.response.PersonaDtoResponse;
import com.DannaS2.LogiTrack.service.PersonaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persona")
@RequiredArgsConstructor
@Tag(
        name = "Personas",
        description = "Operaciones para consultar las personas registradas (administradores y empleados)"
)
public class PersonaController {

    private final PersonaService personaService;

    // GET http://localhost:8080/api/persona
    @GetMapping
    @Operation(
            summary = "Listar personas",
            description = "Obtiene la lista de personas registradas en el sistema."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de personas obtenida correctamente"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar personas")
    })
    public ResponseEntity<List<PersonaDtoResponse>> listar() {
        return ResponseEntity.ok(personaService.listar());
    }

    // GET http://localhost:8080/api/persona/1
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar persona por ID",
            description = "Obtiene los datos de una persona utilizando su identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Persona encontrada"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar personas"),
            @ApiResponse(responseCode = "404", description = "No se encontró una persona con ese ID")
    })
    public ResponseEntity<PersonaDtoResponse> buscar(
            @Parameter(description = "Identificador de la persona", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(personaService.buscarId(id));
    }
}
