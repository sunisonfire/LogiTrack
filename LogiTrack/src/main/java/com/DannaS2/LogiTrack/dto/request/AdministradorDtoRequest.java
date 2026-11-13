package com.DannaS2.LogiTrack.dto.request;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AdministradorDtoRequest(
        @NotNull(message = "El username es obligatorio")
        String username,

        @NotNull(message = "La password/contraseña es obligatoria")
        String password) {


}
