
package com.DannaS2.LogiTrack.controller;

import com.DannaS2.LogiTrack.dto.response.ReporteGeneralDtoResponse;
import com.DannaS2.LogiTrack.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
@Tag(
        name = "Reportes",
        description = "Consultas y reportes generales del inventario"
)
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping("/resumen")
    @Operation(
            summary = "Obtener resumen general",
            description = "Obtiene el stock total por bodega y los cinco productos más movidos."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reporte obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No se ha iniciado sesión"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para consultar el reporte")
    })
    public ResponseEntity<ReporteGeneralDtoResponse> obtenerResumenGeneral() {
        return ResponseEntity.ok(reporteService.obtenerResumenGeneral());
    }
}
