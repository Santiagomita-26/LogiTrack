package com.s2.LogiTrack.config;

import com.s2.LogiTrack.model.Usuario;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

// Servicio encargado de generar y validar tokens JWT.
@Service
public class JwtService {

    // Clave secreta obtenida desde application.properties.
    @Value("${jwt.secret}")
    private String SECRET;

    // Tiempo de expiración del token en milisegundos.
    @Value("${jwt.expiration}")
    private long EXPIRATION;

    // Convierte la clave secreta en una clave criptográfica.
    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // Genera un token JWT con la información del usuario.
    public String generateToken(Usuario usuario) {

        return Jwts.builder()

                // Correo del usuario que se autentica.
                .setSubject(usuario.getCorreo())

                // ID del usuario.
                .claim("id", usuario.getId())

                // Rol del usuario: ADMIN o USUARIO.
                .claim("rol", usuario.getRol().name())

                // Fecha de creación del token.
                .setIssuedAt(new Date())

                // Fecha de expiración del token.
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))

                // Firma el token utilizando el algoritmo HS256.
                .signWith(getKey(), SignatureAlgorithm.HS256)

                // Construye y devuelve el token.
                .compact();
    }

    // Valida el token y devuelve la información que contiene.
    public Claims validateToken(String token) {

        try {

            return Jwts.parserBuilder()

                    // Clave utilizada para verificar la firma.
                    .setSigningKey(getKey())
                    // Construye el analizador del token.
                    .build()

                    // Verifica la firma y la expiración.
                    .parseClaimsJws(token)
                    // Obtiene la información del token.
                    .getBody();

        } catch (JwtException | IllegalArgumentException e) {

            // Si el token es inválido o ha expirado, devuelve null.
            return null;
        }
    }
}