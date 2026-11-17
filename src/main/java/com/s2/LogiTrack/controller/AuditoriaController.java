package com.s2.LogiTrack.controller;



import com.s2.LogiTrack.dto.response.AuditoriaDtoResponse;
import com.s2.LogiTrack.model.Auditoria;
import com.s2.LogiTrack.service.interfaces.AuditoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/auditorias")
@RequiredArgsConstructor
@Tag(name = "Auditoría", description = "Consulta del historial de operaciones realizadas en el sistema")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    // http://localhost:8080/api/auditorias
    @GetMapping
    @Operation(summary = "Listar auditorías", description = "Obtiene todos los registros de auditoría almacenados en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registros de auditoría obtenidos correctamente"),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso"),
            @ApiResponse(responseCode = "403", description = "No tienes permisos de administrador para consultar las auditorías")
    })
    public ResponseEntity<List<AuditoriaDtoResponse>> listar() {
        return ResponseEntity.ok(auditoriaService.listar());
    }

    // http://localhost:8080/api/auditorias/1
    @GetMapping("/{id}")
    @Operation(summary = "Buscar auditoría por ID", description = "Obtiene un registro de auditoría específico mediante su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro de auditoría encontrado correctamente"),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso"),
            @ApiResponse(responseCode = "403", description = "No tienes permisos de administrador para consultar las auditorías"),
            @ApiResponse(responseCode = "404", description = "No se encontró un registro de auditoría con el ID indicado")
    })
    public ResponseEntity<AuditoriaDtoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(auditoriaService.buscarId(id));
    }

    // http://localhost:8080/api/auditorias/usuario/1
    @GetMapping("/usuario/{id}")
    @Operation(summary = "Buscar auditorías por usuario", description = "Obtiene todos los registros de auditoría asociados a un usuario específico."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Auditorías del usuario obtenidas correctamente"),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso"),
            @ApiResponse(responseCode = "403", description = "No tienes permisos de administrador para consultar las auditorías")
    })
    public ResponseEntity<List<AuditoriaDtoResponse>> buscarUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(auditoriaService.buscarUsuarioId(id));
    }

    // http://localhost:8080/api/auditorias/operacion/INSERT
    @GetMapping("/operacion/{operacion}")
    @Operation(summary = "Buscar auditorías por operación", description = "Filtra los registros según el tipo de operación realizada: INSERT, UPDATE o DELETE."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Auditorías filtradas por operación correctamente"),
            @ApiResponse(responseCode = "400", description = "El tipo de operación indicado no es válido"),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso"),
            @ApiResponse(responseCode = "403", description = "No tienes permisos de administrador para consultar las auditorías")
    })
    public ResponseEntity<List<AuditoriaDtoResponse>> buscarOperacion(@PathVariable Auditoria.TipoOperacion operacion) {
        return ResponseEntity.ok(auditoriaService.buscarOperacion(operacion));
    }

    // http://localhost:8080/api/auditorias/entidad/Producto
    @GetMapping("/entidad/{entidad}")
    @Operation(summary = "Buscar auditorías por entidad",
            description = "Obtiene los registros de auditoría correspondientes a una entidad del sistema, por ejemplo Producto, Bodega o Movimiento."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Auditorías de la entidad obtenidas correctamente"),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso"),
            @ApiResponse(responseCode = "403", description = "No tienes permisos de administrador para consultar las auditorías")
    })
    public ResponseEntity<List<AuditoriaDtoResponse>> buscarEntidad(@PathVariable String entidad) {
        return ResponseEntity.ok(auditoriaService.buscarEntidad(entidad));
    }

    // http://localhost:8080/api/auditorias/fecha?fechaInicio=2026-11-01T00:00:00&fechaFin=2026-11-30T23:59:59
    @GetMapping("/fecha")
    @Operation(summary = "Buscar auditorías por rango de fechas",
            description = "Obtiene los registros de auditoría generados entre una fecha y hora de inicio y una fecha y hora de finalización. Utiliza el formato ISO 8601."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Auditorías del rango de fechas obtenidas correctamente"),
            @ApiResponse(responseCode = "400", description = "El formato de las fechas es inválido"),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso"),
            @ApiResponse(responseCode = "403", description = "No tienes permisos de administrador para consultar las auditorías")
    })
    public ResponseEntity<List<AuditoriaDtoResponse>> buscarFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(auditoriaService.buscarFecha(fechaInicio, fechaFin));
    }

    // http://localhost:8080/api/auditorias/usuario/1/operacion/INSERT
    @GetMapping("/usuario/{id}/operacion/{operacion}")
    @Operation(summary = "Buscar auditorías por usuario y operación", description = "Filtra los registros de auditoría de un usuario específico según el tipo de operación realizada: INSERT, UPDATE o DELETE."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Auditorías filtradas por usuario y operación correctamente"),
            @ApiResponse(responseCode = "400", description = "El tipo de operación indicado no es válido"),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso"),
            @ApiResponse(responseCode = "403", description = "No tienes permisos de administrador para consultar las auditorías")
    })
    public ResponseEntity<List<AuditoriaDtoResponse>> buscarUsuarioOperacion(@PathVariable Long id, @PathVariable Auditoria.TipoOperacion operacion) {
        return ResponseEntity.ok(auditoriaService.buscarUsuarioOperacion(id, operacion));
    }

    // http://localhost:8080/api/auditorias/usuario/1/fecha?fechaInicio=2026-11-01T00:00:00&fechaFin=2026-11-30T23:59:59
    @GetMapping("/usuario/{id}/fecha")
    @Operation(summary = "Buscar auditorías de un usuario por fechas", description = "Obtiene los registros de auditoría de un usuario específico dentro de un rango de fechas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Auditorías del usuario en el rango de fechas obtenidas correctamente"),
            @ApiResponse(responseCode = "400", description = "El formato de las fechas es inválido"),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso"),
            @ApiResponse(responseCode = "403", description = "No tienes permisos de administrador para consultar las auditorías")
    })
    public ResponseEntity<List<AuditoriaDtoResponse>> buscarUsuarioFecha(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {

        return ResponseEntity.ok(
                auditoriaService.buscarUsuarioFecha(id, fechaInicio, fechaFin));
    }
}
