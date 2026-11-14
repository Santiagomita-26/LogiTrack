package com.s2.LogiTrack.mapper;

import com.s2.LogiTrack.model.Producto;
import com.s2.LogiTrack.model.Marca;
import com.s2.LogiTrack.dto.request.ProductoDtoRequest;
import com.s2.LogiTrack.dto.response.ProductoDtoResponse;

public class ProductoMapper {

    public ProductoDtoResponse entityToDto(Producto producto) {
        if (producto == null || producto.getMarca() == null) return null;
        return new ProductoDtoResponse(producto.getId(), producto.getNombre(), producto.getCategoria(), producto.getPrecio(), producto.getMarca().getId(),
                   producto.getMarca().getNombre());
    }

    public Producto dtoToEntity(ProductoDtoRequest dto, Marca marca) {
        if (dto == null || marca == null) return null;
        Producto producto = new Producto();
        producto.setNombre(dto.nombre());
        producto.setCategoria(dto.categoria());
        producto.setPrecio(dto.precio());
        producto.setMarca(marca);

        return producto;
    }

    public void updateEntityToDto(Producto producto, ProductoDtoRequest dto, Marca marca) {
        if (producto == null || dto == null || marca == null) return;
        producto.setNombre(dto.nombre());
        producto.setCategoria(dto.categoria());
        producto.setPrecio(dto.precio());
        producto.setMarca(marca);
    }
}