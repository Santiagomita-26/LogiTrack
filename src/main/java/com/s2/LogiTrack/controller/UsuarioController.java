package com.s2.LogiTrack.controller;

import com.s2.LogiTrack.dto.request.UsuarioActualizarDtoRequest;
import com.s2.LogiTrack.dto.response.UsuarioDtoResponse;
import com.s2.LogiTrack.service.interfaces.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    // Lista todos los usuarios.
    @GetMapping
    public ResponseEntity<List<UsuarioDtoResponse>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    // Busca un usuario por su ID.
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDtoResponse> buscarId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarId(id));
    }

    // Busca un usuario por su correo.
    @GetMapping("/correo/{correo}")
    public ResponseEntity<UsuarioDtoResponse> buscarCorreo(@PathVariable String correo) {
        return ResponseEntity.ok(usuarioService.buscarCorreo(correo));
    }

    // Lista los usuarios pendientes de aprobación.
    @GetMapping("/pendientes")
    public ResponseEntity<List<UsuarioDtoResponse>> listarPendientes() {
        return ResponseEntity.ok(usuarioService.listarPendientes());
    }

    // Actualiza los datos de un usuario.
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDtoResponse> actualizar(@Valid @RequestBody UsuarioActualizarDtoRequest dto, @PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.actualizar(dto, id));
    }

    // Elimina un usuario.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Aprueba una solicitud de registro.

    @PatchMapping("/{id}/aprobar")
    public ResponseEntity<UsuarioDtoResponse> aprobar(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.aprobar(id));
    }

    // Rechaza una solicitud de registro.
    @PatchMapping("/{id}/rechazar")
    public ResponseEntity<UsuarioDtoResponse> rechazar(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.rechazar(id));
    }
}
