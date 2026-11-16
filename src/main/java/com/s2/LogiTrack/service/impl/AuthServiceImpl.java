
package com.s2.LogiTrack.service.impl;

import com.s2.LogiTrack.config.JwtService;
import com.s2.LogiTrack.dto.request.UsuarioLoginDtoRequest;
import com.s2.LogiTrack.dto.response.LoginResponse;
import com.s2.LogiTrack.exception.BusinessRuleException;
import com.s2.LogiTrack.model.Usuario;
import com.s2.LogiTrack.repository.UsuarioRepository;
import com.s2.LogiTrack.service.interfaces.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // Inicia sesión y genera el token JWT.
    @Override
    public LoginResponse login(UsuarioLoginDtoRequest dto) {

        // Busca el usuario mediante su correo.
        Usuario usuario = usuarioRepository.findByCorreo(dto.correo());

        // Comprueba que el usuario exista y que la contraseña sea correcta.
        if (usuario == null || !passwordEncoder.matches(dto.password(), usuario.getPassword())) {
            throw new BusinessRuleException("Credenciales inválidas");
        }

        // Comprueba que el administrador haya aprobado la cuenta.
        if (usuario.getEstado() != Usuario.Estado.APROBADO) {
            throw new BusinessRuleException("El usuario no está aprobado");
        }

        // Genera el token JWT con los datos del usuario.
        String token = jwtService.generateToken(usuario);
        return new LoginResponse(token);
    }
}
