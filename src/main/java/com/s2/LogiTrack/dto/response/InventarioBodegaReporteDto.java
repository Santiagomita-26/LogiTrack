package com.s2.LogiTrack.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record InventarioBodegaReporteDto(Long bodegaId, String bodegaNombre,
                                         List<ProductoInventarioReporteDto> productos, BigDecimal valorTotalInventario) {
}
