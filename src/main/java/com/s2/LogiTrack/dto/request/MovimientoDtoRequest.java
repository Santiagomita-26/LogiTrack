package com.s2.LogiTrack.dto.request;

import com.s2.LogiTrack.model.Movimiento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MovimientoDtoRequest(

        @NotNull Movimiento.TipoMovimiento tipo,
        @NotNull @Positive Integer cantidad,
        @NotNull Long productoId,
        Long bodegaOrigenId,
        Long bodegaDestinoId

) {
}
