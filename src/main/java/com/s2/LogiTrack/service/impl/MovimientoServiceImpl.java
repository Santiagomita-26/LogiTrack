package com.s2.LogiTrack.service.impl;

import com.s2.LogiTrack.dto.request.MovimientoDtoRequest;
import com.s2.LogiTrack.dto.response.MovimientoDtoResponse;
import com.s2.LogiTrack.mapper.MovimientoMapper;
import com.s2.LogiTrack.model.*;
import com.s2.LogiTrack.repository.*;
import com.s2.LogiTrack.service.interfaces.MovimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoMapper movimientoMapper;
    private final MovimientoRepository movimientoRepository;
    private final InventarioRepository inventarioRepository;
    private final ProductoRepository productoRepository;
    private final BodegaRepository bodegaRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public MovimientoDtoResponse guardar(MovimientoDtoRequest dto, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new RuntimeException("Error, usuario no encontrado"));
        Producto producto = productoRepository.findById(dto.productoId()).orElseThrow(() -> new RuntimeException("Error, producto no encontrado"));
        Bodega bodegaOrigen = null;
        Bodega bodegaDestino = null;

        switch (dto.tipo()) {
            case ENTRADA -> {
                if (dto.bodegaOrigenId() != null || dto.bodegaDestinoId() == null) {
                    throw new RuntimeException("Error, una entrada requiere únicamente una bodega destino");
                }
                bodegaDestino = bodegaRepository.findById(dto.bodegaDestinoId()).orElseThrow(() -> new RuntimeException("Error, bodega destino no encontrada"));
                Inventario inventario = obtenerOCrearInventario(bodegaDestino, producto);
                inventario.setStock(inventario.getStock() + dto.cantidad());
                inventarioRepository.save(inventario);
            }

            case SALIDA -> {
                if (dto.bodegaOrigenId() == null || dto.bodegaDestinoId() != null) {
                    throw new RuntimeException("Error, una salida requiere únicamente una bodega origen");
                }
                bodegaOrigen = bodegaRepository.findById(dto.bodegaOrigenId()).orElseThrow(() -> new RuntimeException("Error, bodega origen no encontrada"));
                Inventario inventario = inventarioRepository.findByBodegaIdAndProductoId(bodegaOrigen.getId(), producto.getId());
                validarStock(inventario, dto.cantidad());
                inventario.setStock(inventario.getStock() - dto.cantidad());
                inventarioRepository.save(inventario);
            }
            case TRANSFERENCIA -> {
                if (dto.bodegaOrigenId() == null || dto.bodegaDestinoId() == null) {
                    throw new RuntimeException("Error, la transferencia requiere ambas bodegas");
                }
                if (dto.bodegaOrigenId().equals(dto.bodegaDestinoId())) {
                    throw new RuntimeException("Error, las bodegas deben ser diferentes");
                }
                bodegaOrigen = bodegaRepository.findById(dto.bodegaOrigenId()).orElseThrow(() -> new RuntimeException("Error, bodega origen no encontrada"));
                bodegaDestino = bodegaRepository.findById(dto.bodegaDestinoId()).orElseThrow(() -> new RuntimeException("Error, bodega destino no encontrada"));
                Inventario inventarioOrigen = inventarioRepository.findByBodegaIdAndProductoId(bodegaOrigen.getId(), producto.getId());
                validarStock(inventarioOrigen, dto.cantidad());
                Inventario inventarioDestino = obtenerOCrearInventario(bodegaDestino, producto);
                inventarioOrigen.setStock(inventarioOrigen.getStock() - dto.cantidad());
                inventarioDestino.setStock(inventarioDestino.getStock() + dto.cantidad());
                inventarioRepository.save(inventarioOrigen);
                inventarioRepository.save(inventarioDestino);
            }
        }

        Movimiento movimiento = movimientoMapper.dtoToEntity(dto, usuario, producto, bodegaOrigen, bodegaDestino);
        movimiento.setFecha(LocalDateTime.now());
        Movimiento movimientoRespuesta = movimientoRepository.save(movimiento);
        return movimientoMapper.entityToDto(movimientoRespuesta);
    }

    private Inventario obtenerOCrearInventario(Bodega bodega, Producto producto) {
        Inventario inventario = inventarioRepository.findByBodegaIdAndProductoId(bodega.getId(), producto.getId());
        if (inventario == null) {
            inventario = new Inventario();
            inventario.setBodega(bodega);
            inventario.setProducto(producto);
            inventario.setStock(0);
        }
        return inventario;
    }

    private void validarStock(Inventario inventario, Integer cantidad) {
        if (inventario == null) {throw new RuntimeException("Error, no existe inventario para este producto en la bodega");}
        if (inventario.getStock() < cantidad) {throw new RuntimeException("Error, stock insuficiente para realizar el movimiento");}
    }

    @Override
    public List<MovimientoDtoResponse> listar() {
        return movimientoRepository.findAll().stream().map(movimientoMapper::entityToDto).toList();
    }

    @Override
    public MovimientoDtoResponse buscarId(Long id) {
        Movimiento movimiento = movimientoRepository.findById(id).orElseThrow(() -> new RuntimeException("Error, movimiento no encontrado"));
        return movimientoMapper.entityToDto(movimiento);
    }

    @Override
    public List<MovimientoDtoResponse> buscarTipo(Movimiento.TipoMovimiento tipo) {
        return movimientoRepository.findByTipo(tipo).stream().map(movimientoMapper::entityToDto).toList();
    }

    @Override
    public List<MovimientoDtoResponse> buscarUsuarioId(Long id) {
        return movimientoRepository.findByUsuarioId(id).stream().map(movimientoMapper::entityToDto).toList();
    }
    @Override
    public List<MovimientoDtoResponse> buscarProductoId(Long id) {
        return movimientoRepository.findByProductoId(id).stream().map(movimientoMapper::entityToDto).toList();
    }

    @Override
    public List<MovimientoDtoResponse> buscarFecha(
            LocalDateTime fechaInicio, LocalDateTime fechaFin) {
            return movimientoRepository.findByFechaBetween(fechaInicio, fechaFin).stream().map(movimientoMapper::entityToDto).toList();
    }
    @Override
    public List<MovimientoDtoResponse> buscarBodegaOrigenId(Long id) {
        return movimientoRepository.findByBodegaOrigenId(id).stream().map(movimientoMapper::entityToDto).toList();
    }

    @Override
    public List<MovimientoDtoResponse> buscarBodegaDestinoId(Long id) {
        return movimientoRepository.findByBodegaDestinoId(id).stream().map(movimientoMapper::entityToDto).toList();
    }

    @Override
    public List<MovimientoDtoResponse> buscarUsuarioFecha(Long usuarioId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return movimientoRepository.findByUsuarioIdAndFechaBetween(usuarioId, fechaInicio, fechaFin).stream().map(movimientoMapper::entityToDto).toList();
    }
}