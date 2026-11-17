
package com.s2.LogiTrack.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi() {

        return new OpenAPI()
                .info(new Info()
                        .title("LogiTrack API")
                        .version("1.0")
                        .description("Documentación oficial de la API REST de LogiTrack S.A. Sistema de gestión de inventarios, bodegas y trazabilidad de movimientos."))

                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))

                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }

    // Proporciona el objeto encargado de convertir objetos Java a JSON.
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

}
