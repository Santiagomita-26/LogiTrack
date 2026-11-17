
package com.s2.LogiTrack.auth;

import com.s2.LogiTrack.dto.request.UsuarioLoginDtoRequest;
import com.s2.LogiTrack.dto.request.UsuarioRegistroDtoRequest;
import com.s2.LogiTrack.dto.response.LoginResponse;
import com.s2.LogiTrack.dto.response.UsuarioDtoResponse;
import com.s2.LogiTrack.service.interfaces.AuthService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Registro de usuarios e inicio de sesión mediante JWT")
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario aprobado mediante correo y contraseña. Retorna un token JWT."
    )
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso. Retorna el token JWT."),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos."),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas o usuario no aprobado.")
    })
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UsuarioLoginDtoRequest dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/register")
    @Operation( summary = "Registrar usuario",
            description = "Registra un nuevo usuario con rol USUARIO y estado PENDIENTE. Requiere aprobación de un administrador para iniciar sesión."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado correctamente."),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos."),
            @ApiResponse(responseCode = "409", description = "El correo electrónico ya está registrado.")
    })
    public ResponseEntity<UsuarioDtoResponse> register(@Valid @RequestBody UsuarioRegistroDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.guardar(dto));
    }
}
