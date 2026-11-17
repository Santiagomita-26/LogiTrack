package com.s2.LogiTrack.service.impl;

import com.s2.LogiTrack.dto.response.InventarioBodegaReporteDto;
import com.s2.LogiTrack.dto.response.InventarioDtoResponse;
import com.s2.LogiTrack.dto.response.MovimientoDtoResponse;
import com.s2.LogiTrack.dto.response.ProductoMasMovidoDto;
import com.s2.LogiTrack.mapper.InventarioBodegaReporteMapper;
import com.s2.LogiTrack.mapper.InventarioMapper;
import com.s2.LogiTrack.mapper.MovimientoMapper;
import com.s2.LogiTrack.model.Inventario;
import com.s2.LogiTrack.model.Movimiento;
import com.s2.LogiTrack.repository.BodegaRepository;
import com.s2.LogiTrack.repository.InventarioRepository;
import com.s2.LogiTrack.repository.MovimientoRepository;
import com.s2.LogiTrack.service.interfaces.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final InventarioRepository inventarioRepository;
    private final InventarioMapper inventarioMapper;
    private final MovimientoRepository movimientoRepository;
    private final MovimientoMapper movimientoMapper;
    private final BodegaRepository bodegaRepository;
    private final InventarioBodegaReporteMapper inventarioBodegaReporteMapper;

    @Override
    public List<InventarioDtoResponse> productosStockBajo() {
        return inventarioRepository.findByStockLessThanEqual(10).stream().map(inventarioMapper::entityToDto).toList();
    }

    @Override
    public List<MovimientoDtoResponse> movimientosPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return movimientoRepository.findByFechaBetween(fechaInicio, fechaFin).stream().map(movimientoMapper::entityToDto).toList();
    }

    @Override
    public List<InventarioBodegaReporteDto> resumenInventarioPorBodega() {
        return bodegaRepository.findAll().stream().map(bodega -> {List<Inventario> inventarios = inventarioRepository.findByBodegaId(bodega.getId());
                    return inventarioBodegaReporteMapper.entityToBodegaDto(bodega, inventarios);}).toList();
    }

    @Override
    public List<ProductoMasMovidoDto> productosMasMovidos() {
        Map<Long, List<Movimiento>> movimientosPorProducto = movimientoRepository.findAll().stream().collect(Collectors.groupingBy(movimiento -> movimiento.getProducto().getId()));
        return movimientosPorProducto.values().stream().map(movimientos -> {Movimiento movimiento = movimientos.get(0);
            Long totalMovimientos = (long) movimientos.size();
            Long totalUnidades = movimientos.stream().mapToLong(Movimiento::getCantidad).sum();

                    return new ProductoMasMovidoDto(
                            movimiento.getProducto().getId(),
                            movimiento.getProducto().getNombre(),
                            movimiento.getProducto().getCategoria(),
                            totalMovimientos,
                            totalUnidades
                    );
                }).sorted(Comparator.comparing(ProductoMasMovidoDto::totalUnidades).reversed()).toList();
    }

}