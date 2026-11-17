package com.s2.LogiTrack.controller;

import com.s2.LogiTrack.dto.request.ProductoDtoRequest;
import com.s2.LogiTrack.dto.response.ProductoDtoResponse;
import com.s2.LogiTrack.service.interfaces.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;


@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Gestión de productos y consultas por marca, nombre y categoría")
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    @Operation(summary = "Registrar producto", description = "Registra un nuevo producto y lo asocia con una marca existente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto registrado correctamente."),
            @ApiResponse(responseCode = "400", description = "Los datos enviados no cumplen las validaciones."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso."),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para realizar esta operación."),
            @ApiResponse(responseCode = "404", description = "No existe la marca indicada."),
            @ApiResponse(responseCode = "409", description = "El producto ya se encuentra registrado o existe un conflicto de integridad.")
    })
    public ResponseEntity<ProductoDtoResponse> guardar(@Valid @RequestBody ProductoDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.guardar(dto));
    }

    @GetMapping
    @Operation(summary = "Listar productos", description = "Obtiene todos los productos registrados en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos obtenida correctamente."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso.")
    })
    public ResponseEntity<List<ProductoDtoResponse>> listar() {
        return ResponseEntity.ok(productoService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar producto por ID", description = "Obtiene la información de un producto mediante su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso."),
            @ApiResponse(responseCode = "404", description = "No existe un producto con el ID proporcionado.")
    })
    public ResponseEntity<ProductoDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.buscarId(id));
    }

    @GetMapping("/marca/{id}")
    @Operation(summary = "Buscar productos por marca", description = "Obtiene los productos asociados con una marca específica."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta de productos por marca realizada correctamente."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso."),
            @ApiResponse(responseCode = "404", description = "No existe la marca indicada.")
    })
    public ResponseEntity<List<ProductoDtoResponse>> buscarMarca(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.buscarMarcaId(id));
    }

    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Buscar productos por nombre", description = "Obtiene los productos cuyo nombre coincide con el criterio de búsqueda."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta por nombre realizada correctamente."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso.")
    })
    public ResponseEntity<List<ProductoDtoResponse>> buscarNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(productoService.buscarNombre(nombre));
    }

    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Buscar productos por categoría", description = "Obtiene los productos pertenecientes a una categoría específica."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta por categoría realizada correctamente."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso.")
    })
    public ResponseEntity<List<ProductoDtoResponse>> buscarCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(productoService.buscarCategoria(categoria));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto", description = "Actualiza la información de un producto existente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente."),
            @ApiResponse(responseCode = "400", description = "Los datos enviados no cumplen las validaciones."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso."),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para realizar esta operación."),
            @ApiResponse(responseCode = "404", description = "No existe el producto o la marca indicada."),
            @ApiResponse(responseCode = "409", description = "La actualización genera un conflicto con los datos existentes.")
    })
    public ResponseEntity<ProductoDtoResponse> actualizar(@Valid @RequestBody ProductoDtoRequest dto, @PathVariable Long id) {
        return ResponseEntity.ok(productoService.actualizar(dto, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto mediante su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso."),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para realizar esta operación."),
            @ApiResponse(responseCode = "404", description = "No existe un producto con el ID proporcionado."),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar el producto debido a restricciones de integridad.")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
