
package com.s2.LogiTrack.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// Filtro encargado de validar los tokens JWT de las peticiones.
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    // Servicio encargado de validar y extraer la información del token.
    private final JwtService jwtService;

    // Intercepta cada petición HTTP antes de llegar al controlador.
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Obtiene el encabezado Authorization de la petición.
        String header = request.getHeader("Authorization");

        // Comprueba que exista el encabezado y tenga el formato Bearer.
        if (header != null && header.startsWith("Bearer ")) {

            // Extrae el token eliminando el prefijo Bearer.
            String token = header.substring(7);

            try {

                // Valida el token y obtiene la información que contiene.
                Claims claims = jwtService.validateToken(token);

                // Comprueba que el token sea válido.
                if (claims != null) {

                    // Obtiene el correo del usuario.
                    String correo = claims.getSubject();

                    // Obtiene el rol almacenado en el token.
                    String rol = claims.get("rol", String.class);

                    // Crea la autoridad correspondiente al rol del usuario.
                    SimpleGrantedAuthority authority =
                            new SimpleGrantedAuthority("ROLE_" + rol);

                    // Construye la autenticación con el correo y sus permisos.
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    correo,
                                    null,
                                    List.of(authority)
                            );

                    // Guarda la autenticación en el contexto de seguridad.
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }

            } catch (JwtException | IllegalArgumentException e) {

                // Limpia el contexto cuando el token no es válido.
                SecurityContextHolder.clearContext();
            }
        }

        // Continúa con el siguiente filtro de la cadena.
        filterChain.doFilter(request, response);
    }
}
