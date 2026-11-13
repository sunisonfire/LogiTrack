package com.DannaS2.LogiTrack.dto.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record AdministradorDtoRequest(
        @NotNull(message = "Los datos de la persona son obligatorios")
        @Valid
        PersonaDtoRequest persona,

        @NotNull(message = "El username es obligatorio")
        String username,

        @NotNull(message = "La password/contraseña es obligatoria")
        String password) {


}
