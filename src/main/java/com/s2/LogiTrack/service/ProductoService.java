package com.s2.LogiTrack.service;

import com.s2.LogiTrack.dto.request.ProductoDtoRequest;
import com.s2.LogiTrack.dto.response.ProductoDtoResponse;

import java.util.List;

public interface ProductoService {

    ProductoDtoResponse guardar(ProductoDtoRequest dto);
    ProductoDtoResponse actualizar(ProductoDtoRequest dto, Long id);
    void eliminar(Long id);
    List<ProductoDtoResponse> listar();
    ProductoDtoResponse buscarId(Long id);
    List<ProductoDtoResponse> buscarMarcaId(Long id);
    List<ProductoDtoResponse> buscarNombre(String nombre);
    List<ProductoDtoResponse> buscarCategoria(String categoria);

}