package com.s2.LogiTrack.mapper;

import com.s2.LogiTrack.model.Inventario;
import com.s2.LogiTrack.dto.response.InventarioDtoResponse;

public class InventarioMapper {

    public InventarioDtoResponse entityToDto(Inventario inventario) {
        if (inventario == null) return null;
        return new InventarioDtoResponse(inventario.getId(),inventario.getBodega().getId(),inventario.getBodega().getNombre(),inventario.getProducto().getId(),
                inventario.getProducto().getNombre(),inventario.getStock());
    } // El inventario no se registra ni se actualiza directamente por eso solo tenemos un metodo
        // El stock cambia mediante las operaciones de movimiento SALIDA-ENTRADA-TRANSFERENCIA
}