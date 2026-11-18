package com.DannaS2.LogiTrack.dto.request;

import jakarta.validation.constraints.Size;

public record RechazarSolicitudRequest(

        @Size(max = 255, message = "El motivo no puede superar 255 caracteres")
        String motivo
) {}
