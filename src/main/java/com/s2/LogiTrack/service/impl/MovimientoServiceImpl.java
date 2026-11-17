package com.s2.LogiTrack.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.s2.LogiTrack.dto.request.MovimientoDtoRequest;
import com.s2.LogiTrack.dto.response.MovimientoDtoResponse;
import com.s2.LogiTrack.exception.BusinessRuleException;
import com.s2.LogiTrack.mapper.InventarioMapper;
import com.s2.LogiTrack.mapper.MovimientoMapper;
import com.s2.LogiTrack.model.*;
import com.s2.LogiTrack.repository.*;
import com.s2.LogiTrack.service.interfaces.AuditoriaService;
import com.s2.LogiTrack.service.interfaces.MovimientoService;
import jakarta.persistence.EntityNotFoundException;
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
    private final AuditoriaService auditoriaService;
    private final ObjectMapper objectMapper;
    private final InventarioMapper inventarioMapper;


    @Transactional
    @Override
    public MovimientoDtoResponse guardar(MovimientoDtoRequest dto, Long usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new EntityNotFoundException("Error, usuario no encontrado"));
        Producto producto = productoRepository.findById(dto.productoId()).orElseThrow(() -> new EntityNotFoundException("Error, producto no encontrado"));
        if (dto.cantidad() == null || dto.cantidad() <= 0) {
            throw new BusinessRuleException("Error, la cantidad debe ser mayor que cero");}
        Bodega bodegaOrigen = null;
        Bodega bodegaDestino = null;
        Inventario inventarioOrigen = null;
        Inventario inventarioDestino = null;
        JsonNode inventarioOrigenAnterior = null;
        JsonNode inventarioDestinoAnterior = null;
        boolean inventarioDestinoNuevo = false;
        switch (dto.tipo()) {

            case ENTRADA -> {
                if (dto.bodegaOrigenId() != null || dto.bodegaDestinoId() == null) {
                    throw new BusinessRuleException("Error, una entrada requiere únicamente una bodega destino");
                }
                bodegaDestino = bodegaRepository.findById(dto.bodegaDestinoId()).orElseThrow(() -> new EntityNotFoundException("Error, bodega destino no encontrada"));
                inventarioDestino = inventarioRepository.findByBodegaIdAndProductoId(bodegaDestino.getId(), producto.getId());
                if (inventarioDestino == null) {
                    inventarioDestino = new Inventario();
                    inventarioDestino.setBodega(bodegaDestino);
                    inventarioDestino.setProducto(producto);
                    inventarioDestino.setStock(0);
                    inventarioDestinoNuevo = true;
                } else {inventarioDestinoAnterior = objectMapper.valueToTree(inventarioMapper.entityToDto(inventarioDestino));}
                inventarioDestino.setStock(inventarioDestino.getStock() + dto.cantidad());
                inventarioRepository.save(inventarioDestino);
            }

            case SALIDA -> {

                if (dto.bodegaOrigenId() == null || dto.bodegaDestinoId() != null) {
                    throw new BusinessRuleException("Error, una salida requiere únicamente una bodega origen");
                }

                bodegaOrigen = bodegaRepository.findById(dto.bodegaOrigenId()).orElseThrow(() -> new EntityNotFoundException("Error, bodega origen no encontrada"));

                inventarioOrigen = inventarioRepository.findByBodegaIdAndProductoId(bodegaOrigen.getId(), producto.getId());
                validarStock(inventarioOrigen, dto.cantidad());
                inventarioOrigenAnterior = objectMapper.valueToTree(inventarioMapper.entityToDto(inventarioOrigen)
                );
                inventarioOrigen.setStock(inventarioOrigen.getStock() - dto.cantidad());
                inventarioRepository.save(inventarioOrigen);
            }

            case TRANSFERENCIA -> {

                if (dto.bodegaOrigenId() == null || dto.bodegaDestinoId() == null) {
                    throw new BusinessRuleException("Error, la transferencia requiere ambas bodegas");
                }
                if (dto.bodegaOrigenId().equals(dto.bodegaDestinoId())) {
                    throw new BusinessRuleException("Error, las bodegas deben ser diferentes");
                }
                bodegaOrigen = bodegaRepository.findById(dto.bodegaOrigenId()).orElseThrow(() -> new EntityNotFoundException("Error, bodega origen no encontrada"));
                bodegaDestino = bodegaRepository.findById(dto.bodegaDestinoId()).orElseThrow(() -> new EntityNotFoundException("Error, bodega destino no encontrada"));
                inventarioOrigen = inventarioRepository.findByBodegaIdAndProductoId(bodegaOrigen.getId(), producto.getId());
                validarStock(inventarioOrigen, dto.cantidad());
                inventarioOrigenAnterior = objectMapper.valueToTree(inventarioMapper.entityToDto(inventarioOrigen));
                inventarioDestino = inventarioRepository.findByBodegaIdAndProductoId(bodegaDestino.getId(), producto.getId());

                if (inventarioDestino == null) {
                    inventarioDestino = new Inventario();
                    inventarioDestino.setBodega(bodegaDestino);
                    inventarioDestino.setProducto(producto);
                    inventarioDestino.setStock(0);
                    inventarioDestinoNuevo = true;
                } else {
                    inventarioDestinoAnterior = objectMapper.valueToTree(inventarioMapper.entityToDto(inventarioDestino));
                }
                inventarioOrigen.setStock(inventarioOrigen.getStock() - dto.cantidad());
                inventarioDestino.setStock(inventarioDestino.getStock() + dto.cantidad());
                inventarioRepository.save(inventarioOrigen);
                inventarioRepository.save(inventarioDestino);
            }
        }

        // Registra el movimiento.
        Movimiento movimiento = movimientoMapper.dtoToEntity(dto, usuario, producto, bodegaOrigen, bodegaDestino);
        movimiento.setFecha(LocalDateTime.now());
        Movimiento movimientoRespuesta = movimientoRepository.save(movimiento);

        // Audita el inventario de origen, si fue afectado.
        if (inventarioOrigen != null) {
            JsonNode valoresNuevos = objectMapper.valueToTree(inventarioMapper.entityToDto(inventarioOrigen));
            auditoriaService.registrar(Auditoria.TipoOperacion.UPDATE, "Inventario", inventarioOrigenAnterior, valoresNuevos);
        }
        // Audita el inventario de destino, si fue afectado.
        if (inventarioDestino != null) {
            JsonNode valoresNuevos = objectMapper.valueToTree(inventarioMapper.entityToDto(inventarioDestino));
            auditoriaService.registrar(inventarioDestinoNuevo ? Auditoria.TipoOperacion.INSERT : Auditoria.TipoOperacion.UPDATE, "Inventario", inventarioDestinoAnterior, valoresNuevos);
        }
        // Audita la creación del movimiento.
        MovimientoDtoResponse respuesta = movimientoMapper.entityToDto(movimientoRespuesta);
        JsonNode movimientoNuevo = objectMapper.valueToTree(respuesta);
        auditoriaService.registrar(Auditoria.TipoOperacion.INSERT, "Movimiento", null, movimientoNuevo);
        return respuesta;
    }

    private Inventario obtenerOCrearInventario(Bodega bodega, Producto producto) {

        Inventario inventario = inventarioRepository.findByBodegaIdAndProductoId( bodega.getId(), producto.getId());
        if (inventario == null) {
            inventario = new Inventario();
            inventario.setBodega(bodega);
            inventario.setProducto(producto);
            inventario.setStock(0);
        }
        return inventario;
    }

    private void validarStock(Inventario inventario, Integer cantidad) {
        if (inventario == null) {throw new BusinessRuleException("Error, no existe inventario para este producto en la bodega");}
        if (inventario.getStock() < cantidad) {throw new BusinessRuleException("Error, stock insuficiente para realizar el movimiento");}
    }

    @Override
    public List<MovimientoDtoResponse> listar() {
        return movimientoRepository.findAll().stream().map(movimientoMapper::entityToDto).toList();
    }

    @Override
    public MovimientoDtoResponse buscarId(Long id) {
        Movimiento movimiento = movimientoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Error, movimiento no encontrado"));
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
    public List<MovimientoDtoResponse> buscarFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
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