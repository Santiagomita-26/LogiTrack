package com.s2.LogiTrack.dto.response;

import java.math.BigDecimal;

public record ProductoInventarioReporteDto(Long productoId, String productoNombre, String categoria, BigDecimal precioUnitario, Integer stock,
                                           BigDecimal valorTotal) {
}
