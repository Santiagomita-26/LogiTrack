package com.s2.LogiTrack.controller;

import com.s2.LogiTrack.dto.request.UsuarioActualizarDtoRequest;
import com.s2.LogiTrack.dto.response.UsuarioDtoResponse;
import com.s2.LogiTrack.service.interfaces.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor

@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Gestión de usuarios, consultas y aprobación de registros")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Obtiene todos los usuarios registrados en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida correctamente."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso."),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para realizar esta operación.")
    })
    public ResponseEntity<List<UsuarioDtoResponse>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID", description = "Obtiene la información de un usuario mediante su identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso."),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para realizar esta operación."),
            @ApiResponse(responseCode = "404", description = "No existe un usuario con el ID proporcionado.")
    })
    public ResponseEntity<UsuarioDtoResponse> buscarId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarId(id));
    }

    @GetMapping("/correo/{correo}")
    @Operation(summary = "Buscar usuario por correo", description = "Obtiene la información de un usuario mediante su correo electrónico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso."),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para realizar esta operación."),
            @ApiResponse(responseCode = "404", description = "No existe un usuario con el correo proporcionado.")
    })
    public ResponseEntity<UsuarioDtoResponse> buscarCorreo(@PathVariable String correo) {
        return ResponseEntity.ok(usuarioService.buscarCorreo(correo));
    }

    @GetMapping("/pendientes")
    @Operation(summary = "Listar usuarios pendientes", description = "Obtiene los usuarios que se encuentran pendientes de aprobación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios pendientes obtenida correctamente."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso."),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para realizar esta operación.")
    })
    public ResponseEntity<List<UsuarioDtoResponse>> listarPendientes() {
        return ResponseEntity.ok(usuarioService.listarPendientes());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos personales de un usuario existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente."),
            @ApiResponse(responseCode = "400", description = "Los datos enviados no cumplen las validaciones."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso."),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para realizar esta operación."),
            @ApiResponse(responseCode = "404", description = "No existe un usuario con el ID proporcionado.")
    })
    public ResponseEntity<UsuarioDtoResponse> actualizar(
            @Valid @RequestBody UsuarioActualizarDtoRequest dto,
            @PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.actualizar(dto, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario mediante su identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso."),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para realizar esta operación."),
            @ApiResponse(responseCode = "404", description = "No existe un usuario con el ID proporcionado."),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar el usuario debido a restricciones de integridad.")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/aprobar")
    @Operation(summary = "Aprobar usuario", description = "Aprueba la solicitud de registro de un usuario pendiente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario aprobado correctamente."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso."),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para realizar esta operación."),
            @ApiResponse(responseCode = "404", description = "No existe un usuario con el ID proporcionado."),
            @ApiResponse(responseCode = "409", description = "El usuario no se encuentra en estado pendiente.")
    })
    public ResponseEntity<UsuarioDtoResponse> aprobar(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.aprobar(id));
    }

    @PatchMapping("/{id}/rechazar")
    @Operation(summary = "Rechazar usuario", description = "Rechaza la solicitud de registro de un usuario pendiente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario rechazado correctamente."),
            @ApiResponse(responseCode = "401", description = "No estás autenticado para acceder a este recurso."),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para realizar esta operación."),
            @ApiResponse(responseCode = "404", description = "No existe un usuario con el ID proporcionado."),
            @ApiResponse(responseCode = "409", description = "El usuario no se encuentra en estado pendiente.")
    })
    public ResponseEntity<UsuarioDtoResponse> rechazar(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.rechazar(id));
    }
}

