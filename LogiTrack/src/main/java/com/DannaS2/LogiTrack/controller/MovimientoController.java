package com.DannaS2.LogiTrack.controller;

import com.DannaS2.LogiTrack.dto.request.MovimientoDtoRequest;
import com.DannaS2.LogiTrack.dto.response.MovimientoDtoResponse;
import com.DannaS2.LogiTrack.model.TipoMovimiento;
import com.DannaS2.LogiTrack.service.MovimientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/movimiento")
@RequiredArgsConstructor
@Tag(
        name = "Movimientos",
        description = "Operaciones para registrar y consultar los movimientos de inventario (entradas, salidas y transferencias)"
)
public class MovimientoController {

    private final MovimientoService movimientoService;

    // POST http://localhost:8080/api/movimiento
    @PostMapping
    @Operation(
            summary = "Registrar un movimiento",
            description = "Registra una entrada, salida o transferencia de productos entre bodegas, con el detalle de productos y cantidades."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Movimiento creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Los datos enviados no cumplen las validaciones o no hay stock suficiente"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para registrar movimientos"),
            @ApiResponse(responseCode = "404", description = "No se encontró la persona, una bodega o un producto indicado")
    })
    public ResponseEntity<MovimientoDtoResponse> guardar(@Valid @RequestBody MovimientoDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movimientoService.guardar(dto));
    }

    // GET http://localhost:8080/api/movimiento
    @GetMapping
    @Operation(
            summary = "Listar movimientos",
            description = "Obtiene la lista de movimientos registrados en el sistema."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de movimientos obtenida correctamente"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar movimientos")
    })
    public ResponseEntity<List<MovimientoDtoResponse>> listar() {
        return ResponseEntity.ok(movimientoService.listar());
    }

    // GET http://localhost:8080/api/movimiento/1
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar movimiento por ID",
            description = "Obtiene los datos de un movimiento, con sus detalles, utilizando su identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimiento encontrado"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar movimientos"),
            @ApiResponse(responseCode = "404", description = "No se encontró un movimiento con ese ID")
    })
    public ResponseEntity<MovimientoDtoResponse> buscar(
            @Parameter(description = "Identificador del movimiento", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(movimientoService.buscarId(id));
    }

    // GET http://localhost:8080/api/movimiento/filtroTipo?tipo=ENTRADA
    @GetMapping("/filtroTipo")
    @Operation(
            summary = "Filtrar movimientos por tipo",
            description = "Obtiene los movimientos del tipo indicado: ENTRADA, SALIDA o TRANSFERENCIA."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimientos filtrados correctamente"),
            @ApiResponse(responseCode = "400", description = "El tipo indicado no es válido"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar movimientos")
    })
    public ResponseEntity<List<MovimientoDtoResponse>> buscarTipo(
            @Parameter(description = "Tipo de movimiento por el que se desea filtrar", example = "ENTRADA")
            @RequestParam TipoMovimiento tipo) {
        return ResponseEntity.ok(movimientoService.buscarTipo(tipo));
    }

    // GET http://localhost:8080/api/movimiento/filtroPersona?personaId=1
    @GetMapping("/filtroPersona")
    @Operation(
            summary = "Filtrar movimientos por persona",
            description = "Obtiene los movimientos realizados por la persona indicada."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimientos filtrados correctamente"),
            @ApiResponse(responseCode = "400", description = "El ID de la persona no es válido o no se envió"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar movimientos")
    })
    public ResponseEntity<List<MovimientoDtoResponse>> buscarPersona(
            @Parameter(description = "Identificador de la persona que realizó los movimientos", example = "1")
            @RequestParam Long personaId) {
        return ResponseEntity.ok(movimientoService.buscarPersona(personaId));
    }

    // GET http://localhost:8080/api/movimiento/filtroOrigen?bodegaId=1
    @GetMapping("/filtroOrigen")
    @Operation(
            summary = "Filtrar movimientos por bodega de origen",
            description = "Obtiene los movimientos que salieron de la bodega indicada."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimientos filtrados correctamente"),
            @ApiResponse(responseCode = "400", description = "El ID de la bodega no es válido o no se envió"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar movimientos"),
            @ApiResponse(responseCode = "404", description = "No se encontró una bodega con ese ID")
    })
    public ResponseEntity<List<MovimientoDtoResponse>> buscarOrigen(
            @Parameter(description = "Identificador de la bodega de origen", example = "1")
            @RequestParam Long bodegaId) {
        return ResponseEntity.ok(movimientoService.buscarBodegaOrigen(bodegaId));
    }

    // GET http://localhost:8080/api/movimiento/filtroDestino?bodegaId=1
    @GetMapping("/filtroDestino")
    @Operation(
            summary = "Filtrar movimientos por bodega de destino",
            description = "Obtiene los movimientos que llegaron a la bodega indicada."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimientos filtrados correctamente"),
            @ApiResponse(responseCode = "400", description = "El ID de la bodega no es válido o no se envió"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar movimientos"),
            @ApiResponse(responseCode = "404", description = "No se encontró una bodega con ese ID")
    })
    public ResponseEntity<List<MovimientoDtoResponse>> buscarDestino(
            @Parameter(description = "Identificador de la bodega de destino", example = "1")
            @RequestParam Long bodegaId) {
        return ResponseEntity.ok(movimientoService.buscarBodegaDestino(bodegaId));
    }

    // GET http://localhost:8080/api/movimiento/filtroFechas?desde=2026-11-01T00:00:00&hasta=2026-11-30T23:59:59
    @GetMapping("/filtroFechas")
    @Operation(
            summary = "Filtrar movimientos por rango de fechas",
            description = "Obtiene los movimientos registrados entre las dos fechas indicadas, en formato ISO (yyyy-MM-ddTHH:mm:ss)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimientos filtrados correctamente"),
            @ApiResponse(responseCode = "400", description = "Las fechas no son válidas o no se enviaron"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar movimientos")
    })
    public ResponseEntity<List<MovimientoDtoResponse>> buscarFechas(
            @Parameter(description = "Fecha y hora de inicio del rango", example = "2026-11-01T00:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @Parameter(description = "Fecha y hora de fin del rango", example = "2026-11-30T23:59:59")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {
        return ResponseEntity.ok(movimientoService.buscarFechas(desde, hasta));
    }
}
