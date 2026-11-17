
package com.DannaS2.LogiTrack.dto.request;

import com.DannaS2.LogiTrack.model.Categoria;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;


@Schema(description = "Datos necesarios para registrar o actualizar un producto")
public record ProductoDtoRequest(

        // Nombre del producto
        @Schema(
                description = "Nombre del producto",
                example = "Lip Gloss",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 50, message = "El nombre no puede superar 50 caracteres")
        String nombre,

        // Descripción opcional: no lleva @NotBlank
        @Schema(
                description = "Descripción del producto",
                example = "Brillo labial de larga duración"
        )
        @Size(max = 100, message = "La descripción no puede superar 100 caracteres")
        String descripcion,

        // Precio: BigDecimal para evitar errores de redondeo con dinero
        @Schema(
                description = "Precio del producto. Debe ser mayor que cero y admite hasta dos decimales",
                example = "15000.00",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "El precio es obligatorio")
        @Positive(message = "El precio debe ser mayor que 0")
        @Digits(integer = 10, fraction = 2, message = "El precio admite máximo 10 enteros y 2 decimales")
        BigDecimal precio,

        // Categoría del producto (enum)
        @Schema(
                description = "Categoría a la que pertenece el producto",
                example = "LABIALES",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "La categoría es obligatoria")
        Categoria categoria,

        // Llave foránea hacia marca
        @Schema(
                description = "Identificador de la marca asociada al producto",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "La marca es obligatoria")
        @Positive(message = "El ID de la marca debe ser positivo")
        Long marcaId

) {}
