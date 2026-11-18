
package com.DannaS2.LogiTrack.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta al enviar una solicitud de registro")
public record RegistroResponse(
        @Schema(example = "Solicitud de registro enviada. Espera la aprobación de un administrador.")
        String mensaje
) {}
