package com.s2.LogiTrack.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductoDtoRequest (

        @NotBlank @Size(min = 2, max = 100) String nombre,
        @NotBlank @Size(min = 2, max = 100) String categoria,
        @NotNull @Positive  BigDecimal precio,
        @NotNull Long marcaId

) {
}
