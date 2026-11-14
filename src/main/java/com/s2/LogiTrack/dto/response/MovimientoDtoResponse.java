package com.s2.LogiTrack.dto.response;

import com.s2.LogiTrack.model.Movimiento;

import java.time.LocalDateTime;

public record MovimientoDtoResponse(Long id, LocalDateTime fecha, Movimiento.TipoMovimiento tipo, Integer cantidad, // MOVIMIENTO
                                    Long productoId, String productoNombre, Long usuarioId, String usuarioNombre, // PRODUCTO Y USUARIO
                                    Long bodegaOrigenId, String bodegaOrigenNombre, Long bodegaDestinoId, String bodegaDestinoNombre) { // BODEGAS
}
