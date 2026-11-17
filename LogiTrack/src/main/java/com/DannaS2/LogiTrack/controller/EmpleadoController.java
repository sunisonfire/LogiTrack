package com.DannaS2.LogiTrack.controller;

import com.DannaS2.LogiTrack.dto.request.EmpleadoDtoRequest;
import com.DannaS2.LogiTrack.dto.response.EmpleadoDtoResponse;
import com.DannaS2.LogiTrack.service.EmpleadoService;
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
@RequestMapping("/api/empleado")
@RequiredArgsConstructor
@Tag(
        name = "Empleados",
        description = "Operaciones para gestionar y consultar los empleados de las bodegas"
)
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    // POST http://localhost:8080/api/empleado
    @PostMapping
    @Operation(
            summary = "Registrar un empleado",
            description = "Registra un nuevo empleado con sus datos personales, sus credenciales de acceso y la bodega a la que queda asignado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Empleado creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Los datos enviados no cumplen las validaciones"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para registrar empleados"),
            @ApiResponse(responseCode = "404", description = "No se encontró la bodega indicada"),
            @ApiResponse(responseCode = "409", description = "Ya existe una persona con ese documento o el username ya está en uso")
    })
    public ResponseEntity<EmpleadoDtoResponse> guardar(@Valid @RequestBody EmpleadoDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(empleadoService.guardar(dto));
    }

    // GET http://localhost:8080/api/empleado
    @GetMapping
    @Operation(
            summary = "Listar empleados",
            description = "Obtiene la lista de empleados registrados en el sistema."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de empleados obtenida correctamente"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar empleados")
    })
    public ResponseEntity<List<EmpleadoDtoResponse>> listar() {
        return ResponseEntity.ok(empleadoService.listar());
    }

    // GET http://localhost:8080/api/empleado/1
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar empleado por ID",
            description = "Obtiene los datos de un empleado utilizando su identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Empleado encontrado"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar empleados"),
            @ApiResponse(responseCode = "404", description = "No se encontró un empleado con ese ID")
    })
    public ResponseEntity<EmpleadoDtoResponse> buscar(
            @Parameter(description = "Identificador del empleado", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(empleadoService.buscarId(id));
    }

    // PUT http://localhost:8080/api/empleado/1
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar un empleado",
            description = "Actualiza los datos de un empleado existente utilizando su identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Empleado actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Los datos enviados no cumplen las validaciones"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para actualizar empleados"),
            @ApiResponse(responseCode = "404", description = "No se encontró el empleado o la bodega indicada"),
            @ApiResponse(responseCode = "409", description = "Ya existe una persona con ese documento o el username ya está en uso")
    })
    public ResponseEntity<EmpleadoDtoResponse> actualizar(@Valid @RequestBody EmpleadoDtoRequest dto,
                                                               @Parameter(description = "Identificador del empleado que se actualizará", example = "1")
                                                               @PathVariable Long id) {
        return ResponseEntity.ok(empleadoService.actualizar(dto, id));
    }

    // DELETE http://localhost:8080/api/empleado/1
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar un empleado",
            description = "Elimina un empleado utilizando su identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Empleado eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para eliminar empleados"),
            @ApiResponse(responseCode = "404", description = "No se encontró un empleado con ese ID"),
            @ApiResponse(responseCode = "409", description = "El empleado está relacionado con otros datos y no se puede eliminar")
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "Identificador del empleado que se eliminará", example = "1")
            @PathVariable Long id) {
        empleadoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // EmpleadoController
    // GET /api/empleado/filtroBodega?bodegaId=1
    @GetMapping("/filtroBodega")
    @Operation(
            summary = "Filtrar empleados por bodega",
            description = "Obtiene los empleados que están asignados a la bodega indicada."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Empleados filtrados correctamente"),
            @ApiResponse(responseCode = "400", description = "El ID de la bodega no es válido o no se envió"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar empleados")
    })
    public ResponseEntity<List<EmpleadoDtoResponse>> buscarBodega(
            @Parameter(description = "Identificador de la bodega por la que se desea filtrar", example = "1")
            @RequestParam Long bodegaId) {
        return ResponseEntity.ok(empleadoService.buscarBodega(bodegaId));
    }
}
