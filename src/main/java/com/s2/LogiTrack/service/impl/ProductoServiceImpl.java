package com.s2.LogiTrack.service.impl;

import com.s2.LogiTrack.dto.request.ProductoDtoRequest;
import com.s2.LogiTrack.dto.response.ProductoDtoResponse;
import com.s2.LogiTrack.exception.BusinessRuleException;
import com.s2.LogiTrack.mapper.ProductoMapper;
import com.s2.LogiTrack.model.Marca;
import com.s2.LogiTrack.model.Producto;
import com.s2.LogiTrack.repository.MarcaRepository;
import com.s2.LogiTrack.repository.ProductoRepository;
import com.s2.LogiTrack.service.interfaces.ProductoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoMapper productoMapper;
    private final ProductoRepository productoRepository;
    private final MarcaRepository marcaRepository;

    @Override
    public ProductoDtoResponse guardar(ProductoDtoRequest dto) {

        if (productoRepository.existsByNombreAndMarcaId(dto.nombre(), dto.marcaId())) {throw new BusinessRuleException("Error, el producto ya se encuentra registrado para esta marca");
        }
        Marca marca = marcaRepository.findById(dto.marcaId()).orElseThrow(() -> new EntityNotFoundException("Error, la marca no existe"));
        Producto producto = productoMapper.dtoToEntity(dto, marca);
        Producto productoRespuesta = productoRepository.save(producto);
        return productoMapper.entityToDto(productoRespuesta);
    }

    @Override
    public ProductoDtoResponse actualizar(ProductoDtoRequest dto, Long id) {

        Producto producto = productoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Error, producto a actualizar no encontrado"));
        if (productoRepository.existsByNombreAndMarcaId(dto.nombre(), dto.marcaId())) {
            if (!producto.getNombre().equalsIgnoreCase(dto.nombre()) || !producto.getMarca().getId().equals(dto.marcaId())) {
                throw new BusinessRuleException("Error, ya existe otro producto con ese nombre para esta marca");
            }
        }
        Marca marca = marcaRepository.findById(dto.marcaId()).orElseThrow(() -> new EntityNotFoundException("Error, la marca no existe"));
        productoMapper.updateEntityToDto(producto, dto, marca);
        Producto productoActualizado = productoRepository.save(producto);
        return productoMapper.entityToDto(productoActualizado);
    }

    @Override
    public void eliminar(Long id) {
        Producto producto = productoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Error, producto a eliminar no existe"));
        productoRepository.delete(producto);
    }

    @Override
    public List<ProductoDtoResponse> listar() {
        return productoRepository.findAll().stream().map(productoMapper::entityToDto).toList();
    }

    @Override
    public ProductoDtoResponse buscarId(Long id) {
        Producto producto = productoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Error, no existe un producto con ese ID"));
        return productoMapper.entityToDto(producto);
    }

    @Override
    public List<ProductoDtoResponse> buscarMarcaId(Long id) {

        if (!marcaRepository.existsById(id)) {
            throw new EntityNotFoundException("Error, no existe una marca con ese ID");
        }
        return productoRepository.findByMarcaId(id).stream().map(productoMapper::entityToDto).toList();
    }

    @Override
    public List<ProductoDtoResponse> buscarNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre).stream().map(productoMapper::entityToDto).toList();
    }

    @Override
    public List<ProductoDtoResponse> buscarCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria).stream().map(productoMapper::entityToDto).toList();
    }
}