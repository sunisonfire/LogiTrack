package com.DannaS2.LogiTrack.service;

import com.DannaS2.LogiTrack.dto.request.AprobarSolicitudRequest;
import com.DannaS2.LogiTrack.dto.request.RechazarSolicitudRequest;
import com.DannaS2.LogiTrack.dto.response.SolicitudRegistroDtoResponse;

import java.util.List;

public interface SolicitudRegistroService {

    List<SolicitudRegistroDtoResponse> listarPendientes();

    SolicitudRegistroDtoResponse aprobar(
            Long id,
            AprobarSolicitudRequest request
    );

    SolicitudRegistroDtoResponse rechazar(
            Long id,
            RechazarSolicitudRequest request
    );
}
