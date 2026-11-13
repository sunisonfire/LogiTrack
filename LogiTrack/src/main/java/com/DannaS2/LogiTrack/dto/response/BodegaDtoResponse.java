package com.DannaS2.LogiTrack.dto.response;

public record BodegaDtoResponse(
        Long id,
        String nombre,
        String ubicacion,
        Long ciudadId,
        String ciudad,
        Integer capacidad,
        Long encargadoId,
        String encargado
) {
}
