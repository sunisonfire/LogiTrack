package com.DannaS2.LogiTrack.controller;

import com.DannaS2.LogiTrack.dto.response.AuditoriaDtoResponse;
import com.DannaS2.LogiTrack.model.TipoOperacion;
import com.DannaS2.LogiTrack.service.AuditoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
@RequiredArgsConstructor
@Tag(
        name = "Auditoría",
        description = "Operaciones para consultar el historial de cambios realizados en el sistema"
)
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    // GET http://localhost:8080/api/auditoria
    @GetMapping
    @Operation(
            summary = "Listar registros de auditoría",
            description = "Obtiene la lista de registros de auditoría del sistema."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de registros de auditoría obtenida correctamente"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar la auditoría")
    })
    public ResponseEntity<List<AuditoriaDtoResponse>> listar() {
        return ResponseEntity.ok(auditoriaService.listar());
    }

    // GET http://localhost:8080/api/auditoria/1
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar registro de auditoría por ID",
            description = "Obtiene un registro de auditoría utilizando su identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro de auditoría encontrado"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar la auditoría"),
            @ApiResponse(responseCode = "404", description = "No se encontró un registro de auditoría con ese ID")
    })
    public ResponseEntity<AuditoriaDtoResponse> buscar(
            @Parameter(description = "Identificador del registro de auditoría", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(auditoriaService.buscarId(id));
    }

    // GET http://localhost:8080/api/auditoria/filtroPersona?personaId=1
    @GetMapping("/filtroPersona")
    @Operation(
            summary = "Filtrar auditoría por persona",
            description = "Obtiene los registros de auditoría de las operaciones realizadas por la persona indicada."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registros filtrados correctamente"),
            @ApiResponse(responseCode = "400", description = "El ID de la persona no es válido o no se envió"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar la auditoría")
    })
    public ResponseEntity<List<AuditoriaDtoResponse>> buscarPersona(
            @Parameter(description = "Identificador de la persona que realizó las operaciones", example = "1")
            @RequestParam Long personaId) {
        return ResponseEntity.ok(auditoriaService.buscarPersona(personaId));
    }

    // GET http://localhost:8080/api/auditoria/filtroTipo?tipo=DELETE
    @GetMapping("/filtroTipo")
    @Operation(
            summary = "Filtrar auditoría por tipo de operación",
            description = "Obtiene los registros de auditoría del tipo de operación indicado: INSERT, UPDATE o DELETE."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registros filtrados correctamente"),
            @ApiResponse(responseCode = "400", description = "El tipo de operación indicado no es válido"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar la auditoría")
    })
    public ResponseEntity<List<AuditoriaDtoResponse>> buscarTipo(
            @Parameter(description = "Tipo de operación por el que se desea filtrar", example = "DELETE")
            @RequestParam TipoOperacion tipo) {
        return ResponseEntity.ok(auditoriaService.buscarTipo(tipo));
    }

    // GET http://localhost:8080/api/auditoria/filtroEntidad?entidad=Producto&entidadId=1
    @GetMapping("/filtroEntidad")
    @Operation(
            summary = "Filtrar auditoría por entidad",
            description = "Obtiene los registros de auditoría de un registro específico, indicando el nombre de la entidad y su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registros filtrados correctamente"),
            @ApiResponse(responseCode = "400", description = "La entidad o su ID no son válidos o no se enviaron"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar la auditoría")
    })
    public ResponseEntity<List<AuditoriaDtoResponse>> buscarEntidad(
            @Parameter(description = "Nombre de la entidad afectada", example = "Producto")
            @RequestParam String entidad,
            @Parameter(description = "Identificador del registro afectado dentro de la entidad", example = "1")
            @RequestParam Long entidadId) {
        return ResponseEntity.ok(auditoriaService.buscarEntidad(entidad, entidadId));
    }

    // GET http://localhost:8080/api/auditoria/filtroFechas?desde=2026-11-01T00:00:00&hasta=2026-11-30T23:59:59
    @GetMapping("/filtroFechas")
    @Operation(
            summary = "Filtrar auditoría por rango de fechas",
            description = "Obtiene los registros de auditoría entre las dos fechas indicadas, en formato ISO (yyyy-MM-ddTHH:mm:ss)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registros filtrados correctamente"),
            @ApiResponse(responseCode = "400", description = "Las fechas no son válidas o no se enviaron"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar la auditoría")
    })
    public ResponseEntity<List<AuditoriaDtoResponse>> buscarFechas(
            @Parameter(description = "Fecha y hora de inicio del rango", example = "2026-11-01T00:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @Parameter(description = "Fecha y hora de fin del rango", example = "2026-11-30T23:59:59")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {
        return ResponseEntity.ok(auditoriaService.buscarFechas(desde, hasta));
    }
}
