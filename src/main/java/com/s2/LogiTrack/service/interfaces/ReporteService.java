package com.s2.LogiTrack.service.interfaces;

import com.s2.LogiTrack.dto.response.InventarioBodegaReporteDto;
import com.s2.LogiTrack.dto.response.InventarioDtoResponse;
import com.s2.LogiTrack.dto.response.MovimientoDtoResponse;
import com.s2.LogiTrack.dto.response.ProductoMasMovidoDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ReporteService {

    List<InventarioDtoResponse> productosStockBajo();
    List<MovimientoDtoResponse> movimientosPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<InventarioBodegaReporteDto> resumenInventarioPorBodega();
    List<ProductoMasMovidoDto> productosMasMovidos();
}