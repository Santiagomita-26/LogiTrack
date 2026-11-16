package com.s2.LogiTrack.service.impl;

import com.s2.LogiTrack.dto.response.InventarioDtoResponse;
import com.s2.LogiTrack.mapper.InventarioMapper;
import com.s2.LogiTrack.model.Inventario;
import com.s2.LogiTrack.repository.InventarioRepository;
import com.s2.LogiTrack.service.interfaces.InventarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {

    private final InventarioMapper inventarioMapper;
    private final InventarioRepository inventarioRepository;

    @Override
    public List<InventarioDtoResponse> listar() {
        return inventarioRepository.findAll().stream().map(inventarioMapper::entityToDto).toList();
    }

    @Override
    public InventarioDtoResponse buscarId(Long id) {
        Inventario inventario = inventarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Error, no existe un inventario con ese ID"));
        return inventarioMapper.entityToDto(inventario);
    }

    @Override
    public InventarioDtoResponse buscarBodegaProducto(Long bodegaId, Long productoId) {
        Inventario inventario = inventarioRepository.findByBodegaIdAndProductoId(bodegaId, productoId);
        if (inventario == null) {throw new EntityNotFoundException("Error, no existe inventario para ese producto en esa bodega");
        }
        return inventarioMapper.entityToDto(inventario);
    }

    @Override
    public List<InventarioDtoResponse> buscarBodegaId(Long id) {
        return inventarioRepository.findByBodegaId(id).stream().map(inventarioMapper::entityToDto).toList();
    }

    @Override
    public List<InventarioDtoResponse> buscarProductoId(Long id) {
        return inventarioRepository.findByProductoId(id).stream().map(inventarioMapper::entityToDto).toList();
    }

    @Override
    public List<InventarioDtoResponse> buscarStockMenor(Integer stock) {
        return inventarioRepository.findByStockLessThan(stock).stream().map(inventarioMapper::entityToDto).toList();
    }

    @Override
    public List<InventarioDtoResponse> buscarStockMayor(Integer stock) {
        return inventarioRepository.findByStockGreaterThan(stock).stream().map(inventarioMapper::entityToDto).toList();
    }
}