package com.DannaS2.LogiTrack.controller;

import com.DannaS2.LogiTrack.dto.request.AprobarSolicitudRequest;
import com.DannaS2.LogiTrack.dto.request.RechazarSolicitudRequest;
import com.DannaS2.LogiTrack.dto.response.SolicitudRegistroDtoResponse;
import com.DannaS2.LogiTrack.service.SolicitudRegistroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes-registro")
@RequiredArgsConstructor
@Tag(
        name = "Solicitudes de registro",
        description = "Administración de solicitudes de registro de empleados"
)
public class SolicitudRegistroController {

    private final SolicitudRegistroService solicitudRegistroService;

    @GetMapping("/pendientes")
    @Operation(
            summary = "Listar solicitudes pendientes",
            description = "Obtiene las solicitudes que todavía no han sido revisadas"
    )
    public ResponseEntity<List<SolicitudRegistroDtoResponse>> listarPendientes() {
        return ResponseEntity.ok(
                solicitudRegistroService.listarPendientes()
        );
    }

    @PatchMapping("/{id}/aprobar")
    @Operation(
            summary = "Aprobar solicitud",
            description = "Aprueba una solicitud y registra al empleado en la bodega indicada"
    )
    public ResponseEntity<SolicitudRegistroDtoResponse> aprobar(
            @PathVariable Long id,
            @Valid @RequestBody AprobarSolicitudRequest request
    ) {
        return ResponseEntity.ok(
                solicitudRegistroService.aprobar(id, request)
        );
    }

    @PatchMapping("/{id}/rechazar")
    @Operation(
            summary = "Rechazar solicitud",
            description = "Rechaza una solicitud pendiente y guarda el motivo opcional"
    )
    public ResponseEntity<SolicitudRegistroDtoResponse> rechazar(
            @PathVariable Long id,
            @Valid @RequestBody RechazarSolicitudRequest request
    ) {
        return ResponseEntity.ok(
                solicitudRegistroService.rechazar(id, request)
        );
    }
}
