package com.DannaS2.LogiTrack.controller;

import com.DannaS2.LogiTrack.dto.response.AuditoriaDtoResponse;
import com.DannaS2.LogiTrack.model.TipoOperacion;
import com.DannaS2.LogiTrack.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    // GET http://localhost:8080/api/auditoria
    @GetMapping
    public ResponseEntity<List<AuditoriaDtoResponse>> listar() {
        return ResponseEntity.ok(auditoriaService.listar());
    }

    // GET http://localhost:8080/api/auditoria/1
    @GetMapping("/{id}")
    public ResponseEntity<AuditoriaDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(auditoriaService.buscarId(id));
    }

    // GET http://localhost:8080/api/auditoria/filtroPersona?personaId=1
    @GetMapping("/filtroPersona")
    public ResponseEntity<List<AuditoriaDtoResponse>> buscarPersona(@RequestParam Long personaId) {
        return ResponseEntity.ok(auditoriaService.buscarPersona(personaId));
    }

    // GET http://localhost:8080/api/auditoria/filtroTipo?tipo=DELETE
    @GetMapping("/filtroTipo")
    public ResponseEntity<List<AuditoriaDtoResponse>> buscarTipo(@RequestParam TipoOperacion tipo) {
        return ResponseEntity.ok(auditoriaService.buscarTipo(tipo));
    }

    // GET http://localhost:8080/api/auditoria/filtroEntidad?entidad=Producto&entidadId=1
    @GetMapping("/filtroEntidad")
    public ResponseEntity<List<AuditoriaDtoResponse>> buscarEntidad(@RequestParam String entidad,
                                                                    @RequestParam Long entidadId) {
        return ResponseEntity.ok(auditoriaService.buscarEntidad(entidad, entidadId));
    }

    // GET http://localhost:8080/api/auditoria/filtroFechas?desde=2026-11-01T00:00:00&hasta=2026-11-30T23:59:59
    @GetMapping("/filtroFechas")
    public ResponseEntity<List<AuditoriaDtoResponse>> buscarFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {
        return ResponseEntity.ok(auditoriaService.buscarFechas(desde, hasta));
    }
}