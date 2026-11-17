package com.s2.LogiTrack.dto.response;

public record ProductoMasMovidoDto(Long productoId, String productoNombre, String categoria, Long totalMovimientos, Long totalUnidades) {
}
