package com.s2.LogiTrack.controller;

import com.s2.LogiTrack.dto.request.BodegaDtoRequest;
import com.s2.LogiTrack.dto.response.BodegaDtoResponse;
import com.s2.LogiTrack.service.interfaces.BodegaService;
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
@RequestMapping("/api/bodegas")
@RequiredArgsConstructor
@Tag(name = "Bodegas", description = "Gestión de bodegas y consultas por ciudad")
public class BodegaController {

    private final BodegaService bodegaService;

    @PostMapping
    @Operation(
            summary = "Registrar bodega",
            description = "Registra una nueva bodega y la asocia con una ciudad existente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bodega registrada correctamente."),
            @ApiResponse(responseCode = "400", description = "Los datos enviados no cumplen las validaciones."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso."),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para realizar esta operación."),
            @ApiResponse(responseCode = "404", description = "No existe la ciudad indicada."),
            @ApiResponse(responseCode = "409", description = "La bodega ya se encuentra registrada o existe un conflicto de integridad.")
    })
    public ResponseEntity<BodegaDtoResponse> guardar(@Valid @RequestBody BodegaDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bodegaService.guardar(dto));
    }

    @GetMapping
    @Operation(summary = "Listar bodegas", description = "Obtiene todas las bodegas registradas en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de bodegas obtenida correctamente."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso.")
    })
    public ResponseEntity<List<BodegaDtoResponse>> listar() {
        return ResponseEntity.ok(bodegaService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar bodega por ID", description = "Obtiene la información de una bodega mediante su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bodega encontrada."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso."),
            @ApiResponse(responseCode = "404", description = "No existe una bodega con el ID proporcionado.")
    })
    public ResponseEntity<BodegaDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(bodegaService.buscarId(id));
    }

    @GetMapping("/ciudad/{id}")
    @Operation(summary = "Buscar bodegas por ciudad", description = "Obtiene todas las bodegas asociadas con el identificador de una ciudad."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta de bodegas por ciudad realizada correctamente."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso."),
            @ApiResponse(responseCode = "404", description = "No existe la ciudad indicada.")
    })
    public ResponseEntity<List<BodegaDtoResponse>> buscarCiudad(@PathVariable Long id) {
        return ResponseEntity.ok(bodegaService.buscarCiudadId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar bodega", description = "Actualiza la información de una bodega existente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bodega actualizada correctamente."),
            @ApiResponse(responseCode = "400", description = "Los datos enviados no cumplen las validaciones."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso."),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para realizar esta operación."),
            @ApiResponse(responseCode = "404", description = "No existe la bodega o ciudad indicada."),
            @ApiResponse(responseCode = "409", description = "La actualización genera un conflicto con los datos existentes.")
    })
    public ResponseEntity<BodegaDtoResponse> actualizar(@Valid @RequestBody BodegaDtoRequest dto, @PathVariable Long id) {
        return ResponseEntity.ok(bodegaService.actualizar(dto, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar bodega", description = "Elimina una bodega mediante su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Bodega eliminada correctamente."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso."),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para realizar esta operación."),
            @ApiResponse(responseCode = "404", description = "No existe una bodega con el ID proporcionado."),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar la bodega debido a restricciones de integridad.")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        bodegaService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
