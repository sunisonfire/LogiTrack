package com.DannaS2.LogiTrack.dto.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AdministradorDtoRequest(
        @NotNull(message = "Los datos de la persona son obligatorios")
        @Valid
        PersonaDtoRequest persona,

        @NotNull(message = "El username es obligatorio")
        String username,


        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
        String password) {


}
