package com.DannaS2.LogiTrack.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Respuesta de un inicio de sesión exitoso")
public record LoginResponse(
        @Schema(description = "Token JWT para usar en el encabezado Authorization como Bearer", example = "eyJhbGciOiJIUzI1NiJ9...")
        String token,

        @Schema(description = "Rol del usuario autenticado", example = "EMPLEADO")
        String rol,

        @Schema(description = "Identificador de la persona autenticada", example = "1")
        Long personaId,

        @Schema(description = "Nombre de la persona autenticada", example = "Juan Pérez")
        String nombre,

        @Schema(description = "Identificador de la bodega asignada. Solo aplica a empleados; nulo en otro caso", example = "1")
        Long bodegaId
) {}
