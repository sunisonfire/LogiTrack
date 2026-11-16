package com.DannaS2.LogiTrack.dto.response;

public record LoginResponse(
        String token,
        String rol,
        Long personaId,
        String nombre,
        Long bodegaId
) {}