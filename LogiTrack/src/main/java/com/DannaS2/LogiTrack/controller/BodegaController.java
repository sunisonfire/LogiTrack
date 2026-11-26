package com.DannaS2.LogiTrack.controller;

import com.DannaS2.LogiTrack.dto.request.BodegaDtoRequest;
import com.DannaS2.LogiTrack.dto.response.BodegaDtoResponse;
import com.DannaS2.LogiTrack.service.BodegaService;
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
@RequestMapping("/api/bodega")
@RequiredArgsConstructor
@Tag(
        name = "Bodegas",
        description = "Operaciones para gestionar y consultar las bodegas"
)
public class BodegaController {

    private final BodegaService bodegaService;

    // POST http://localhost:8080/api/bodega
    @PostMapping
    @Operation(
            summary = "Registrar una bodega",
            description = "Registra una nueva bodega con su nombre, ubicación, ciudad, capacidad y encargado opcional."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Bodega creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Los datos enviados no cumplen las validaciones"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para registrar bodegas"),
            @ApiResponse(responseCode = "404", description = "No se encontró la ciudad o el encargado indicado"),
            @ApiResponse(responseCode = "409", description = "Ya existe una bodega con ese nombre en esa ciudad")
    })
    public ResponseEntity<BodegaDtoResponse> guardar(@Valid @RequestBody BodegaDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bodegaService.guardar(dto));
    }

    // GET http://localhost:8080/api/bodega
    @GetMapping
    @Operation(
            summary = "Listar bodegas",
            description = "Obtiene la lista de bodegas registradas en el sistema."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de bodegas obtenida correctamente"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar bodegas")
    })
    public ResponseEntity<List<BodegaDtoResponse>> listar() {
        return ResponseEntity.ok(bodegaService.listar());
    }

    // GET http://localhost:8080/api/bodega/1
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar bodega por ID",
            description = "Obtiene los datos de una bodega utilizando su identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bodega encontrada"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar bodegas"),
            @ApiResponse(responseCode = "404", description = "No se encontró una bodega con ese ID")
    })
    public ResponseEntity<BodegaDtoResponse> buscar(
            @Parameter(description = "Identificador de la bodega", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(bodegaService.buscarId(id));
    }

    // PUT http://localhost:8080/api/bodega/1
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar una bodega",
            description = "Actualiza los datos de una bodega existente utilizando su identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bodega actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Los datos enviados no cumplen las validaciones"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para actualizar bodegas"),
            @ApiResponse(responseCode = "404", description = "No se encontró la bodega, la ciudad o el encargado indicado"),
            @ApiResponse(responseCode = "409", description = "Ya existe una bodega con ese nombre en esa ciudad")
    })
    public ResponseEntity<BodegaDtoResponse> actualizar(@Valid @RequestBody BodegaDtoRequest dto,
                                                          @Parameter(description = "Identificador de la bodega que se actualizará", example = "1")
                                                          @PathVariable Long id) {
        return ResponseEntity.ok(bodegaService.actualizar(dto, id));
    }

    // DELETE http://localhost:8080/api/bodega/1
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar una bodega",
            description = "Elimina una bodega utilizando su identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Bodega eliminada correctamente"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para eliminar bodegas"),
            @ApiResponse(responseCode = "404", description = "No se encontró una bodega con ese ID"),
            @ApiResponse(responseCode = "409", description = "La bodega está relacionada con otros datos y no se puede eliminar")
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "Identificador de la bodega que se eliminará", example = "1")
            @PathVariable Long id) {
        bodegaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // BodegaController
    // GET /api/bodega/filtroCiudad?ciudadId=1
    @GetMapping("/filtroCiudad")
    @Operation(
            summary = "Filtrar bodegas por ciudad",
            description = "Obtiene las bodegas que están ubicadas en la ciudad indicada."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bodegas filtradas correctamente"),
            @ApiResponse(responseCode = "400", description = "El ID de la ciudad no es válido o no se envió"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar bodegas")
    })
    public ResponseEntity<List<BodegaDtoResponse>> buscarCiudad(
            @Parameter(description = "Identificador de la ciudad por la que se desea filtrar", example = "1")
            @RequestParam Long ciudadId) {
        return ResponseEntity.ok(bodegaService.buscarCiudad(ciudadId));
    }

    //EXAMEN
    //1.
    // GET /api/bodega/filtroEncargado?encargadoId=1
    @GetMapping("/filtroEncargado")
    @Operation(
            summary = "Filtrar bodegas por encargado",
            description = "Obtiene las bodegas que fueron asignadas a un empleado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bodegas filtradas correctamente"),
            @ApiResponse(responseCode = "400", description = "El ID del empleado no es válido o no se envió"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar bodegas")
    })
    public ResponseEntity<List<BodegaDtoResponse>> buscarEncargado(
            @Parameter(description = "Identificador de l encargado por la que se desea filtrar", example = "1")
            @RequestParam Long encargadoId) {
        return ResponseEntity.ok(bodegaService.buscarEncargado(encargadoId));
    }

    //2.
}
