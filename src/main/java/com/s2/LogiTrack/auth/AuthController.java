
package com.s2.LogiTrack.auth;

import com.s2.LogiTrack.dto.request.UsuarioLoginDtoRequest;
import com.s2.LogiTrack.dto.request.UsuarioRegistroDtoRequest;
import com.s2.LogiTrack.dto.response.LoginResponse;
import com.s2.LogiTrack.dto.response.UsuarioDtoResponse;
import com.s2.LogiTrack.service.interfaces.AuthService;
import com.s2.LogiTrack.service.interfaces.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UsuarioLoginDtoRequest dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioDtoResponse> register(@Valid @RequestBody UsuarioRegistroDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(dto));
    }
}
