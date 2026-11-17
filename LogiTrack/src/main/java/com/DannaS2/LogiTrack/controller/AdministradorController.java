package com.DannaS2.LogiTrack.controller;

import com.DannaS2.LogiTrack.dto.request.AdministradorDtoRequest;
import com.DannaS2.LogiTrack.dto.response.AdministradorDtoResponse;
import com.DannaS2.LogiTrack.service.AdministradorService;
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
@RequestMapping("/api/administrador")
@RequiredArgsConstructor
@Tag(
        name = "Administradores",
        description = "Operaciones para gestionar y consultar los administradores del sistema"
)
public class AdministradorController {

    private final AdministradorService administradorService;

    // POST http://localhost:8080/api/administrador
    @PostMapping
    @Operation(
            summary = "Registrar un administrador",
            description = "Registra un nuevo administrador con sus datos personales y sus credenciales de acceso."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Administrador creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Los datos enviados no cumplen las validaciones"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para registrar administradores"),
            @ApiResponse(responseCode = "409", description = "Ya existe una persona con ese documento o el username ya está en uso")
    })
    public ResponseEntity<AdministradorDtoResponse> guardar(@Valid @RequestBody AdministradorDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(administradorService.guardar(dto));
    }

    // GET http://localhost:8080/api/administrador
    @GetMapping
    @Operation(
            summary = "Listar administradores",
            description = "Obtiene la lista de administradores registrados en el sistema."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de administradores obtenida correctamente"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar administradores")
    })
    public ResponseEntity<List<AdministradorDtoResponse>> listar() {
        return ResponseEntity.ok(administradorService.listar());
    }

    // GET http://localhost:8080/api/administrador/1
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar administrador por ID",
            description = "Obtiene los datos de un administrador utilizando su identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Administrador encontrado"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar administradores"),
            @ApiResponse(responseCode = "404", description = "No se encontró un administrador con ese ID")
    })
    public ResponseEntity<AdministradorDtoResponse> buscar(
            @Parameter(description = "Identificador del administrador", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(administradorService.buscarId(id));
    }

    // PUT http://localhost:8080/api/administrador/1
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar un administrador",
            description = "Actualiza los datos de un administrador existente utilizando su identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Administrador actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Los datos enviados no cumplen las validaciones"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para actualizar administradores"),
            @ApiResponse(responseCode = "404", description = "No se encontró un administrador con ese ID"),
            @ApiResponse(responseCode = "409", description = "Ya existe una persona con ese documento o el username ya está en uso")
    })
    public ResponseEntity<AdministradorDtoResponse> actualizar(@Valid @RequestBody AdministradorDtoRequest dto,
                                                               @Parameter(description = "Identificador del administrador que se actualizará", example = "1")
                                                               @PathVariable Long id) {
        return ResponseEntity.ok(administradorService.actualizar(dto, id));
    }

    // DELETE http://localhost:8080/api/administrador/1
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar un administrador",
            description = "Elimina un administrador utilizando su identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Administrador eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para eliminar administradores"),
            @ApiResponse(responseCode = "404", description = "No se encontró un administrador con ese ID"),
            @ApiResponse(responseCode = "409", description = "El administrador está relacionado con otros datos y no se puede eliminar")
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "Identificador del administrador que se eliminará", example = "1")
            @PathVariable Long id) {
        administradorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
