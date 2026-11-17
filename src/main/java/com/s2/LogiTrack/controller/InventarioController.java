package com.s2.LogiTrack.controller;

import com.s2.LogiTrack.dto.response.InventarioDtoResponse;
import com.s2.LogiTrack.service.interfaces.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/inventarios")
@RequiredArgsConstructor
@Tag(name = "Inventario", description = "Consultas de existencias de productos por bodega y niveles de stock")
public class InventarioController {

    private final InventarioService inventarioService;

    @GetMapping
    @Operation(summary = "Listar inventario", description = "Obtiene todos los registros de inventario de las bodegas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de inventario obtenida correctamente."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso.")
    })
    public ResponseEntity<List<InventarioDtoResponse>> listar() {
        return ResponseEntity.ok(inventarioService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar inventario por ID", description = "Obtiene un registro de inventario mediante su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro de inventario encontrado."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso."),
            @ApiResponse(responseCode = "404", description = "No existe un registro de inventario con el ID proporcionado.")
    })
    public ResponseEntity<InventarioDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.buscarId(id));
    }

    @GetMapping("/bodega/{bodegaId}/producto/{productoId}")
    @Operation(summary = "Consultar inventario por bodega y producto", description = "Obtiene las existencias de un producto específico dentro de una bodega determinada."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro de inventario encontrado."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso."),
            @ApiResponse(responseCode = "404", description = "No existe un registro de inventario para la combinación indicada.")
    })
    public ResponseEntity<InventarioDtoResponse> buscarBodegaProducto(
            @PathVariable Long bodegaId,
            @PathVariable Long productoId) {
        return ResponseEntity.ok(inventarioService.buscarBodegaProducto(bodegaId, productoId));
    }

    @GetMapping("/bodega/{id}")
    @Operation(summary = "Consultar inventario por bodega", description = "Obtiene todos los registros de inventario pertenecientes a una bodega."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventario de la bodega obtenido correctamente."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso."),
            @ApiResponse(responseCode = "404", description = "No existe la bodega indicada.")
    })
    public ResponseEntity<List<InventarioDtoResponse>> buscarBodega(@PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.buscarBodegaId(id));
    }

    @GetMapping("/producto/{id}")
    @Operation(summary = "Consultar inventario por producto", description = "Obtiene los registros de inventario de un producto en las diferentes bodegas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventario del producto obtenido correctamente."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso."),
            @ApiResponse(responseCode = "404", description = "No existe el producto indicado.")
    })
    public ResponseEntity<List<InventarioDtoResponse>> buscarProducto(@PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.buscarProductoId(id));
    }

    @GetMapping("/stock/menor/{stock}")
    @Operation(summary = "Consultar inventario con stock menor", description = "Obtiene los registros de inventario cuyo stock es inferior al valor indicado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta de inventario por stock realizada correctamente."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso.")
    })
    public ResponseEntity<List<InventarioDtoResponse>> buscarStockMenor(@PathVariable Integer stock) {
        return ResponseEntity.ok(inventarioService.buscarStockMenor(stock));
    }

    @GetMapping("/stock/mayor/{stock}")
    @Operation(summary = "Consultar inventario con stock mayor", description = "Obtiene los registros de inventario cuyo stock es superior al valor indicado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta de inventario por stock realizada correctamente."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso.")
    })
    public ResponseEntity<List<InventarioDtoResponse>> buscarStockMayor(@PathVariable Integer stock) {
        return ResponseEntity.ok(inventarioService.buscarStockMayor(stock));
    }
}
