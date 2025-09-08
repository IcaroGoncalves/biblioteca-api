package com.biblio.biblioteca_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bibliotecaOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Biblioteca API")
                .version("v1")
                .description("""
            API do catálogo (livros, categorias, assuntos) e circulação.
            Filtros por título/ISBN/categoria/assunto com paginação.
            """)
                .contact(new Contact().name("Time Biblioteca").email("contato@exemplo.com"))
        );
    }
}
