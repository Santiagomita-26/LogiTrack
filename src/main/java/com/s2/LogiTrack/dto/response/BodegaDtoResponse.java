package com.s2.LogiTrack.dto.response;

import java.math.BigDecimal;

public record BodegaDtoResponse(Long id, String nombre, String direccion, BigDecimal capacidad, Long ciudadId, String ciudadNombre) {
}
