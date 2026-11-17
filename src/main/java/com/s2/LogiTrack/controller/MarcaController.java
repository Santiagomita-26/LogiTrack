package com.s2.LogiTrack.controller;

import com.s2.LogiTrack.dto.request.MarcaDtoRequest;
import com.s2.LogiTrack.dto.response.MarcaDtoResponse;
import com.s2.LogiTrack.service.interfaces.MarcaService;
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
@RequestMapping("/api/marcas")
@RequiredArgsConstructor
@Tag(name = "Marcas", description = "Gestión y consulta de marcas de productos")
public class MarcaController {

    private final MarcaService marcaService;

    @PostMapping
    @Operation(
            summary = "Registrar marca",
            description = "Registra una nueva marca en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Marca registrada correctamente."),
            @ApiResponse(responseCode = "400", description = "Los datos enviados no cumplen las validaciones."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso."),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para realizar esta operación."),
            @ApiResponse(responseCode = "409", description = "La marca ya se encuentra registrada.")
    })
    public ResponseEntity<MarcaDtoResponse> guardar(@Valid @RequestBody MarcaDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(marcaService.guardar(dto));
    }

    @GetMapping
    @Operation(summary = "Listar marcas", description = "Obtiene todas las marcas registradas en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de marcas obtenida correctamente."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso.")
    })
    public ResponseEntity<List<MarcaDtoResponse>> listar() {
        return ResponseEntity.ok(marcaService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar marca por ID", description = "Obtiene la información de una marca mediante su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marca encontrada."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso."),
            @ApiResponse(responseCode = "404", description = "No existe una marca con el ID proporcionado.")
    })
    public ResponseEntity<MarcaDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(marcaService.buscarId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar marca", description = "Actualiza la información de una marca existente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marca actualizada correctamente."),
            @ApiResponse(responseCode = "400", description = "Los datos enviados no cumplen las validaciones."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso."),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para realizar esta operación."),
            @ApiResponse(responseCode = "404", description = "No existe una marca con el ID proporcionado."),
            @ApiResponse(responseCode = "409", description = "La actualización genera un conflicto con los datos existentes.")
    })
    public ResponseEntity<MarcaDtoResponse> actualizar(@Valid @RequestBody MarcaDtoRequest dto, @PathVariable Long id) {
        return ResponseEntity.ok(marcaService.actualizar(dto, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar marca", description = "Elimina una marca mediante su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Marca eliminada correctamente."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso."),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para realizar esta operación."),
            @ApiResponse(responseCode = "404", description = "No existe una marca con el ID proporcionado."),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar la marca debido a restricciones de integridad.")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        marcaService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
