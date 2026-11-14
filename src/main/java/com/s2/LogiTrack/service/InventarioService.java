package com.s2.LogiTrack.service;

import com.s2.LogiTrack.dto.response.InventarioDtoResponse;

import java.util.List;

public interface InventarioService {

    List<InventarioDtoResponse> listar();
    InventarioDtoResponse buscarId(Long id);
    InventarioDtoResponse buscarBodegaProducto(Long bodegaId, Long productoId);
    List<InventarioDtoResponse> buscarBodegaId(Long id);
    List<InventarioDtoResponse> buscarProductoId(Long id);
    List<InventarioDtoResponse> buscarStockMenor(Integer stock);
    List<InventarioDtoResponse> buscarStockMayor(Integer stock);

}