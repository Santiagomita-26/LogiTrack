package com.s2.LogiTrack.controller;

import com.s2.LogiTrack.dto.response.InventarioBodegaReporteDto;
import com.s2.LogiTrack.dto.response.InventarioDtoResponse;
import com.s2.LogiTrack.dto.response.MovimientoDtoResponse;
import com.s2.LogiTrack.dto.response.ProductoMasMovidoDto;
import com.s2.LogiTrack.service.interfaces.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reportes")
@Tag(name = "Reportes", description = "Consultas y reportes para el análisis del inventario y los movimientos de LogiTrack")
public class ReporteController {

    private final ReporteService reporteService;

    // [GET] http://localhost:8080/api/reportes/stock-bajo
    @GetMapping("/stock-bajo")
    @Operation(
            summary = "Consultar productos con stock bajo",
            description = "Obtiene los registros de inventario cuyo stock es menor o igual a 10 unidades, permitiendo identificar productos que requieren reposición."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte de productos con stock bajo generado correctamente"),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso"),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para consultar los reportes")
    })
    public ResponseEntity<List<InventarioDtoResponse>> productosStockBajo() {
        return ResponseEntity.ok(reporteService.productosStockBajo());
    }

    // [GET] http://localhost:8080/api/reportes/movimientos/fecha?fechaInicio=2026-11-01T00:00:00&fechaFin=2026-11-16T23:59:59
    @GetMapping("/movimientos/fecha")
    @Operation(summary = "Consultar movimientos por rango de fechas",
            description = "Genera un reporte con los movimientos registrados entre una fecha y hora de inicio y una fecha y hora de finalización. Utiliza el formato ISO 8601."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte de movimientos generado correctamente"),
            @ApiResponse(responseCode = "400", description = "El formato de las fechas es inválido"),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso"),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para consultar los reportes")
    })
    public ResponseEntity<List<MovimientoDtoResponse>> movimientosPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(reporteService.movimientosPorFecha(fechaInicio, fechaFin)
        );
    }

    // [GET] http://localhost:8080/api/reportes/inventario/bodegas
    @GetMapping("/inventario/bodegas")
    @Operation(summary = "Consultar resumen de inventario por bodega",
            description = "Genera un reporte del inventario de cada bodega, incluyendo sus productos, cantidades disponibles y valor total del inventario."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resumen de inventario por bodega generado correctamente"),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso"),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para consultar los reportes")
    })
    public ResponseEntity<List<InventarioBodegaReporteDto>> resumenInventarioPorBodega() {
        return ResponseEntity.ok(reporteService.resumenInventarioPorBodega());
    }

    // http://localhost:8080/api/reportes/productos-mas-movidos
    @GetMapping("/productos-mas-movidos")
    @Operation(summary = "Consultar productos más movidos",
            description = "Genera un reporte de los productos con mayor actividad, ordenados de mayor a menor según las unidades involucradas en sus movimientos. Incluye el número de movimientos y el total de unidades."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte de productos más movidos generado correctamente"),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso"),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para consultar los reportes")
    })
    public ResponseEntity<List<ProductoMasMovidoDto>> productosMasMovidos() {
        return ResponseEntity.ok(reporteService.productosMasMovidos());
    }

}
