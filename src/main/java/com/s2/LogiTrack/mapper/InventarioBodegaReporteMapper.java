package com.s2.LogiTrack.mapper;

import com.s2.LogiTrack.dto.response.InventarioBodegaReporteDto;
import com.s2.LogiTrack.dto.response.ProductoInventarioReporteDto;
import com.s2.LogiTrack.model.Bodega;
import com.s2.LogiTrack.model.Inventario;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class InventarioBodegaReporteMapper {

    public ProductoInventarioReporteDto entityToProductoDto(Inventario inventario) {
        BigDecimal precio = inventario.getProducto().getPrecio();
        BigDecimal valorTotal = precio.multiply(BigDecimal.valueOf(inventario.getStock())
        );
        return new ProductoInventarioReporteDto(
                inventario.getProducto().getId(),
                inventario.getProducto().getNombre(),
                inventario.getProducto().getCategoria(), precio,
                inventario.getStock(), valorTotal
        );
    }

    public InventarioBodegaReporteDto entityToBodegaDto(Bodega bodega, List<Inventario> inventarios) {
        List<ProductoInventarioReporteDto> productos = inventarios.stream().map(this::entityToProductoDto).toList();
        BigDecimal valorTotalInventario = productos.stream().map(ProductoInventarioReporteDto::valorTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new InventarioBodegaReporteDto(bodega.getId(), bodega.getNombre(), productos, valorTotalInventario
        );
    }
}