package com.s2.LogiTrack.controller;

import com.s2.LogiTrack.dto.response.InventarioDtoResponse;
import com.s2.LogiTrack.service.interfaces.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventarios")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;

    // http://localhost:8080/api/inventarios

    @GetMapping
    public ResponseEntity<List<InventarioDtoResponse>> listar() {
        return ResponseEntity.ok(inventarioService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventarioDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.buscarId(id));
    }

    @GetMapping("/bodega/{bodegaId}/producto/{productoId}")
    public ResponseEntity<InventarioDtoResponse> buscarBodegaProducto(
            @PathVariable Long bodegaId,
            @PathVariable Long productoId) {
        return ResponseEntity.ok(inventarioService.buscarBodegaProducto(bodegaId, productoId));
    }

    @GetMapping("/bodega/{id}")
    public ResponseEntity<List<InventarioDtoResponse>> buscarBodega(@PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.buscarBodegaId(id));
    }

    @GetMapping("/producto/{id}")
    public ResponseEntity<List<InventarioDtoResponse>> buscarProducto(@PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.buscarProductoId(id));
    }

    @GetMapping("/stock/menor/{stock}")
    public ResponseEntity<List<InventarioDtoResponse>> buscarStockMenor(@PathVariable Integer stock) {
        return ResponseEntity.ok(inventarioService.buscarStockMenor(stock));
    }

    @GetMapping("/stock/mayor/{stock}")
    public ResponseEntity<List<InventarioDtoResponse>> buscarStockMayor(@PathVariable Integer stock) {
        return ResponseEntity.ok(inventarioService.buscarStockMayor(stock));
    }
}