package com.s2.LogiTrack.controller;



import com.s2.LogiTrack.dto.response.AuditoriaDtoResponse;
import com.s2.LogiTrack.model.Auditoria;
import com.s2.LogiTrack.service.interfaces.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/auditorias")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    // http://localhost:8080/api/auditorias
    @GetMapping
    public ResponseEntity<List<AuditoriaDtoResponse>> listar() {
        return ResponseEntity.ok(auditoriaService.listar());
    }

    // http://localhost:8080/api/auditorias/1
    @GetMapping("/{id}")
    public ResponseEntity<AuditoriaDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(auditoriaService.buscarId(id));
    }

    // http://localhost:8080/api/auditorias/usuario/1
    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<AuditoriaDtoResponse>> buscarUsuario(
            @PathVariable Long id) {
        return ResponseEntity.ok(auditoriaService.buscarUsuarioId(id));
    }

    // http://localhost:8080/api/auditorias/operacion/INSERT
    @GetMapping("/operacion/{operacion}")
    public ResponseEntity<List<AuditoriaDtoResponse>> buscarOperacion(
            @PathVariable Auditoria.TipoOperacion operacion) {
        return ResponseEntity.ok(auditoriaService.buscarOperacion(operacion));
    }

    // http://localhost:8080/api/auditorias/entidad/Producto
    @GetMapping("/entidad/{entidad}")
    public ResponseEntity<List<AuditoriaDtoResponse>> buscarEntidad(
            @PathVariable String entidad) {
        return ResponseEntity.ok(auditoriaService.buscarEntidad(entidad));
    }

    // http://localhost:8080/api/auditorias/fecha?fechaInicio=2026-11-01T00:00:00&fechaFin=2026-11-30T23:59:59
    @GetMapping("/fecha")
    public ResponseEntity<List<AuditoriaDtoResponse>> buscarFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {

        return ResponseEntity.ok(
                auditoriaService.buscarFecha(fechaInicio, fechaFin));
    }

    // http://localhost:8080/api/auditorias/usuario/1/operacion/INSERT
    @GetMapping("/usuario/{id}/operacion/{operacion}")
    public ResponseEntity<List<AuditoriaDtoResponse>> buscarUsuarioOperacion(
            @PathVariable Long id,
            @PathVariable Auditoria.TipoOperacion operacion) {

        return ResponseEntity.ok(
                auditoriaService.buscarUsuarioOperacion(id, operacion));
    }

    // http://localhost:8080/api/auditorias/usuario/1/fecha?fechaInicio=2026-11-01T00:00:00&fechaFin=2026-11-30T23:59:59
    @GetMapping("/usuario/{id}/fecha")
    public ResponseEntity<List<AuditoriaDtoResponse>> buscarUsuarioFecha(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {

        return ResponseEntity.ok(
                auditoriaService.buscarUsuarioFecha(id, fechaInicio, fechaFin));
    }
}