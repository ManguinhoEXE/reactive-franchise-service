package com.pruebadev.reactiveapi.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI reactiveApiOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Reactive API - Prueba Tecnica")
                        .description("Documentacion interactiva de la API REST reactiva")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Prueba Dev")
                                .email("dev@example.com"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
