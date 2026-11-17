package com.s2.LogiTrack.controller;

import com.s2.LogiTrack.dto.request.MovimientoDtoRequest;
import com.s2.LogiTrack.dto.response.MovimientoDtoResponse;
import com.s2.LogiTrack.model.Movimiento;
import com.s2.LogiTrack.service.interfaces.MovimientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
@Tag(
        name = "Movimientos",
        description = "Registro y consulta de entradas, salidas y transferencias de inventario"
)
public class MovimientoController {

    private final MovimientoService movimientoService;

    @PostMapping
    @Operation(summary = "Registrar movimiento", description = "Registra una entrada, salida o transferencia de inventario. El usuario responsable se obtiene automáticamente del token JWT."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Movimiento registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o movimiento no permitido, por ejemplo, cantidad incorrecta o stock insuficiente"),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para realizar esta operación"),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para registrar movimientos"),
            @ApiResponse(responseCode = "404", description = "No se encontró el producto o alguna de las bodegas indicadas")
    })
    public ResponseEntity<MovimientoDtoResponse> guardar(@Valid @RequestBody MovimientoDtoRequest dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long usuarioId = (Long) authentication.getDetails();
        return ResponseEntity.status(HttpStatus.CREATED).body(movimientoService.guardar(dto, usuarioId));
    }

    // http://localhost:8080/api/movimientos
    @GetMapping
    @Operation(summary = "Listar movimientos", description = "Obtiene todos los movimientos registrados en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de movimientos obtenida correctamente"),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso"),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para consultar los movimientos")
    })
    public ResponseEntity<List<MovimientoDtoResponse>> listar() {
        return ResponseEntity.ok(movimientoService.listar());
    }

    // http://localhost:8080/api/movimientos/1
    @GetMapping("/{id}")
    @Operation(summary = "Buscar movimiento por ID", description = "Obtiene la información de un movimiento específico mediante su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimiento encontrado correctamente"),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso"),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para consultar los movimientos"),
            @ApiResponse(responseCode = "404", description = "No se encontró un movimiento con el ID indicado")
    })
    public ResponseEntity<MovimientoDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(movimientoService.buscarId(id));
    }

    // http://localhost:8080/api/movimientos/tipo/ENTRADA
    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Buscar movimientos por tipo", description = "Filtra los movimientos según su tipo: ENTRADA, SALIDA o TRANSFERENCIA."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimientos filtrados correctamente"),
            @ApiResponse(responseCode = "400", description = "El tipo de movimiento indicado no es válido"),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso"),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para consultar los movimientos")
    })
    public ResponseEntity<List<MovimientoDtoResponse>> buscarTipo(
            @PathVariable Movimiento.TipoMovimiento tipo) {

        return ResponseEntity.ok(movimientoService.buscarTipo(tipo));
    }

    // http://localhost:8080/api/movimientos/usuario/1
    @GetMapping("/usuario/{id}")
    @Operation(summary = "Buscar movimientos por usuario", description = "Obtiene todos los movimientos registrados por un usuario específico."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimientos del usuario obtenidos correctamente"),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso"),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para consultar los movimientos")
    })
    public ResponseEntity<List<MovimientoDtoResponse>> buscarUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(movimientoService.buscarUsuarioId(id));
    }

    // http://localhost:8080/api/movimientos/producto/1
    @GetMapping("/producto/{id}")
    @Operation(summary = "Buscar movimientos por producto", description = "Obtiene el historial de movimientos asociados a un producto específico."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimientos del producto obtenidos correctamente"),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso"),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para consultar los movimientos")
    })
    public ResponseEntity<List<MovimientoDtoResponse>> buscarProducto(@PathVariable Long id) {
        return ResponseEntity.ok(movimientoService.buscarProductoId(id));
    }

    // http://localhost:8080/api/movimientos/fecha?inicio=2026-11-01T00:00:00&fin=2026-11-30T23:59:59
    @GetMapping("/fecha")
    @Operation(summary = "Buscar movimientos por rango de fechas", description = "Obtiene los movimientos registrados entre una fecha y hora de inicio y una fecha y hora de finalización. Utiliza el formato ISO 8601."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimientos del rango de fechas obtenidos correctamente"),
            @ApiResponse(responseCode = "400", description = "El formato de las fechas es inválido"),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso"),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para consultar los movimientos")
    })
    public ResponseEntity<List<MovimientoDtoResponse>> buscarFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(movimientoService.buscarFecha(inicio, fin));
    }

    // http://localhost:8080/api/movimientos/bodega/origen/1
    @GetMapping("/bodega/origen/{id}")
    @Operation(summary = "Buscar movimientos por bodega de origen", description = "Obtiene los movimientos en los que la bodega indicada aparece como bodega de origen."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimientos de la bodega de origen obtenidos correctamente"),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso"),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para consultar los movimientos")
    })
    public ResponseEntity<List<MovimientoDtoResponse>> buscarBodegaOrigen(@PathVariable Long id) {
        return ResponseEntity.ok(movimientoService.buscarBodegaOrigenId(id));
    }

    // http://localhost:8080/api/movimientos/bodega/destino/1
    @GetMapping("/bodega/destino/{id}")
    @Operation(summary = "Buscar movimientos por bodega de destino", description = "Obtiene los movimientos en los que la bodega indicada aparece como bodega de destino."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimientos de la bodega de destino obtenidos correctamente"),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso"),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para consultar los movimientos")
    })
    public ResponseEntity<List<MovimientoDtoResponse>> buscarBodegaDestino(@PathVariable Long id) {
        return ResponseEntity.ok(movimientoService.buscarBodegaDestinoId(id));
    }

    // http://localhost:8080/api/movimientos/usuario/1/fecha?inicio=2026-11-01T00:00:00&fin=2026-11-30T23:59:59
    @GetMapping("/usuario/{id}/fecha")
    @Operation(summary = "Buscar movimientos de un usuario por fechas", description = "Filtra los movimientos registrados por un usuario específico dentro de un rango de fechas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimientos del usuario en el rango indicado obtenidos correctamente"),
            @ApiResponse(responseCode = "400", description = "El formato de las fechas es inválido"),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso"),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para consultar los movimientos")
    })
    public ResponseEntity<List<MovimientoDtoResponse>> buscarUsuarioFecha(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(movimientoService.buscarUsuarioFecha(id, inicio, fin));
    }
}
