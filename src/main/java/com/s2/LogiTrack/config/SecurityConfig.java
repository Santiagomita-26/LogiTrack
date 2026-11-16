
package com.s2.LogiTrack.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.s2.LogiTrack.exception.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;

// Clase encargada de configurar la seguridad de LogiTrack.
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // Filtro encargado de validar los tokens JWT.
    private final JwtFilter jwtFilter;

    // Objeto encargado de convertir las respuestas a JSON.
    private final ObjectMapper objectMapper;

    // Configuración principal de seguridad.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http

                // Configura las políticas CORS.
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Deshabilita CSRF porque utilizamos autenticación mediante JWT.
                .csrf(csrf -> csrf.disable())

                // Configura la aplicación para trabajar sin sesiones.
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Define los permisos de acceso a los endpoints.
                .authorizeHttpRequests(auth -> auth

                        // Permite las peticiones OPTIONS utilizadas por CORS.
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Permite el registro y el inicio de sesión.
                        .requestMatchers("/auth/register", "/auth/login").permitAll()

                        // Permite acceder a la documentación Swagger.
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // Solo ADMIN puede consultar la auditoría.
                        .requestMatchers("/api/auditorias/**").hasRole("ADMIN")

                        // Solo ADMIN puede gestionar usuarios.
                        .requestMatchers(
                                "/api/usuarios/**",
                                "/usuarios/**"
                        ).hasRole("ADMIN")

                        // Solo ADMIN puede crear registros maestros.
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/ciudades/**",
                                "/api/marcas/**",
                                "/api/bodegas/**",
                                "/api/productos/**"
                        ).hasRole("ADMIN")

                        // Solo ADMIN puede actualizar registros maestros.
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/ciudades/**",
                                "/api/marcas/**",
                                "/api/bodegas/**",
                                "/api/productos/**"
                        ).hasRole("ADMIN")

                        // Solo ADMIN puede eliminar registros maestros.
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/ciudades/**",
                                "/api/marcas/**",
                                "/api/bodegas/**",
                                "/api/productos/**"
                        ).hasRole("ADMIN")

                        // Cualquier otra petición requiere autenticación.
                        .anyRequest().authenticated()
                )

                // Configura las respuestas de errores de seguridad.
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint())
                        .accessDeniedHandler(accessDeniedHandler())
                )

                // Ejecuta el filtro JWT antes del filtro de autenticación.
                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                // Construye la configuración de seguridad.
                .build();
    }

    // Configura la respuesta cuando el usuario no está autenticado (401).
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {

        return (request, response, exception) -> {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            ErrorResponse error = new ErrorResponse(
                    LocalDateTime.now(),
                    HttpStatus.UNAUTHORIZED.value(),
                    "No estás autenticado para acceder a este recurso",
                    "UNAUTHORIZED"
            );

            objectMapper.writeValue(response.getOutputStream(), error);
        };
    }

    // Configura la respuesta cuando el usuario no tiene permisos (403).
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {

        return (request, response, exception) -> {

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            ErrorResponse error = new ErrorResponse(
                    LocalDateTime.now(),
                    HttpStatus.FORBIDDEN.value(),
                    "No tienes permisos para realizar esta operación",
                    "ACCESS_DENIED"
            );

            objectMapper.writeValue(response.getOutputStream(), error);
        };
    }

    // Configura los orígenes y métodos permitidos para el frontend.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // Direcciones desde las cuales se permite acceder al backend.
        config.setAllowedOrigins(List.of(
                "http://127.0.0.1:5500",
                "http://localhost:5500"
        ));

        // Métodos HTTP permitidos.
        config.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));

        // Permite recibir encabezados como Authorization.
        config.setAllowedHeaders(List.of("*"));

        // Permite el envío de credenciales.
        config.setAllowCredentials(true);

        // Aplica la configuración CORS a todas las rutas.
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }

    // Proporciona el codificador BCrypt para proteger las contraseñas.
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }


}
