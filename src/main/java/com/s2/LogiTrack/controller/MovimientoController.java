package com.s2.LogiTrack.controller;

import com.s2.LogiTrack.dto.request.MovimientoDtoRequest;
import com.s2.LogiTrack.dto.response.MovimientoDtoResponse;
import com.s2.LogiTrack.model.Movimiento;
import com.s2.LogiTrack.service.interfaces.MovimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    // http://localhost:8080/api/movimientos?usuarioId=1
    @PostMapping
    public ResponseEntity<MovimientoDtoResponse> guardar(
            @Valid @RequestBody MovimientoDtoRequest dto,
            @RequestParam Long usuarioId) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(movimientoService.guardar(dto, usuarioId));
    }

    // http://localhost:8080/api/movimientos
    @GetMapping
    public ResponseEntity<List<MovimientoDtoResponse>> listar() {
        return ResponseEntity.ok(movimientoService.listar());
    }

    // http://localhost:8080/api/movimientos/1
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(movimientoService.buscarId(id));
    }

    // http://localhost:8080/api/movimientos/tipo/ENTRADA
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<MovimientoDtoResponse>> buscarTipo(
            @PathVariable Movimiento.TipoMovimiento tipo) {

        return ResponseEntity.ok(movimientoService.buscarTipo(tipo));
    }

    // http://localhost:8080/api/movimientos/usuario/1
    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<MovimientoDtoResponse>> buscarUsuario(
            @PathVariable Long id) {

        return ResponseEntity.ok(movimientoService.buscarUsuarioId(id));
    }

    // http://localhost:8080/api/movimientos/producto/1
    @GetMapping("/producto/{id}")
    public ResponseEntity<List<MovimientoDtoResponse>> buscarProducto(
            @PathVariable Long id) {

        return ResponseEntity.ok(movimientoService.buscarProductoId(id));
    }

    // http://localhost:8080/api/movimientos/fecha?inicio=2026-11-01T00:00:00&fin=2026-11-30T23:59:59
    @GetMapping("/fecha")
    public ResponseEntity<List<MovimientoDtoResponse>> buscarFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

        return ResponseEntity.ok(movimientoService.buscarFecha(inicio, fin));
    }

    // http://localhost:8080/api/movimientos/bodega/origen/1
    @GetMapping("/bodega/origen/{id}")
    public ResponseEntity<List<MovimientoDtoResponse>> buscarBodegaOrigen(
            @PathVariable Long id) {

        return ResponseEntity.ok(movimientoService.buscarBodegaOrigenId(id));
    }

    // http://localhost:8080/api/movimientos/bodega/destino/1
    @GetMapping("/bodega/destino/{id}")
    public ResponseEntity<List<MovimientoDtoResponse>> buscarBodegaDestino(
            @PathVariable Long id) {

        return ResponseEntity.ok(movimientoService.buscarBodegaDestinoId(id));
    }

    // http://localhost:8080/api/movimientos/usuario/1/fecha?inicio=2026-11-01T00:00:00&fin=2026-11-30T23:59:59
    @GetMapping("/usuario/{id}/fecha")
    public ResponseEntity<List<MovimientoDtoResponse>> buscarUsuarioFecha(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(movimientoService.buscarUsuarioFecha(id, inicio, fin));
    }
}