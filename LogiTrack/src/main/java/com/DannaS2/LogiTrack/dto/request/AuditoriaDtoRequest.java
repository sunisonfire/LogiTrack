package com.DannaS2.LogiTrack.dto.request;

import com.DannaS2.LogiTrack.model.Persona;
import com.DannaS2.LogiTrack.model.TipoOperacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;


public record AuditoriaDtoRequest(
        @NotNull(message = "La categoría es obligatoria")
        TipoOperacion tipoOperacion,
        @NotNull(message = "La persona es obligatoria")
        Persona persona,
        @Size(min=10, max=50)
        @NotBlank(message="el texto no puede ser null ni solo espacios")
        String entidad,
        @NotNull(message = "La entidad_id es obligatorio")
        @Positive(message = "El ID de la entidad debe ser positivo")
        Long entidadId,
        @Size(max = 10000, message = "Los valores anteriores no pueden superar 10000 caracteres")
        String valoresAnteriores,
        @Size(max = 10000, message = "Los valores nuevos no pueden superar 10000 caracteres")
        String valoresNuevos

) {

}
