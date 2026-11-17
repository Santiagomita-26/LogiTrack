package com.s2.LogiTrack.controller;

import com.s2.LogiTrack.dto.request.CiudadDtoRequest;
import com.s2.LogiTrack.dto.response.CiudadDtoResponse;
import com.s2.LogiTrack.service.interfaces.CiudadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ciudades")
@Tag(name = "Ciudades", description = "Gestión y consulta de ciudades registradas en el sistema")
public class CiudadController {

    private final CiudadService ciudadService;

    @PostMapping
    @Operation(summary = "Registrar ciudad", description = "Registra una nueva ciudad en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ciudad registrada correctamente."),
            @ApiResponse(responseCode = "400", description = "Los datos enviados son inválidos."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso."),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para realizar esta operación."),
            @ApiResponse(responseCode = "409", description = "La ciudad ya se encuentra registrada.")
    })
    public ResponseEntity<CiudadDtoResponse> guardar(@RequestBody CiudadDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ciudadService.guardar(dto));
    }

    @GetMapping
    @Operation(summary = "Listar ciudades", description = "Obtiene todas las ciudades registradas en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de ciudades obtenida correctamente."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso.")
    })
    public ResponseEntity<List<CiudadDtoResponse>> listar() {
        return ResponseEntity.ok(ciudadService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar ciudad por ID", description = "Obtiene la información de una ciudad mediante su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ciudad encontrada."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso."),
            @ApiResponse(responseCode = "404", description = "No existe una ciudad con el ID proporcionado.")
    })
    public ResponseEntity<CiudadDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(ciudadService.buscarId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar ciudad", description = "Actualiza la información de una ciudad existente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ciudad actualizada correctamente."),
            @ApiResponse(responseCode = "400", description = "Los datos enviados son inválidos."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso."),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para realizar esta operación."),
            @ApiResponse(responseCode = "404", description = "No existe una ciudad con el ID proporcionado."),
            @ApiResponse(responseCode = "409", description = "La actualización genera un conflicto con los datos existentes.")
    })
    public ResponseEntity<CiudadDtoResponse> actualizar(
            @RequestBody CiudadDtoRequest dto, @PathVariable Long id) {
        return ResponseEntity.ok(ciudadService.actualizar(dto, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar ciudad", description = "Elimina una ciudad mediante su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ciudad eliminada correctamente."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso."),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para realizar esta operación."),
            @ApiResponse(responseCode = "404", description = "No existe una ciudad con el ID proporcionado."),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar la ciudad debido a restricciones de integridad.")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ciudadService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
