package com.s2.LogiTrack.dto.response;

import java.math.BigDecimal;

public record ProductoDtoResponse(Long id, String nombre, String categoria, BigDecimal precio,Long marcaId, String marcaNombre) {
}
