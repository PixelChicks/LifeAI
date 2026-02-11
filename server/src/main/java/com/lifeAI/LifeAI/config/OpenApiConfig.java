package com.lifeAI.LifeAI.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server().url("https://lifeai-production.up.railway.app").description("Production"),
                        new Server().url("http://localhost:8080").description("Local")
                ))
                .info(new Info()
                        .title("LifeAI API")
                        .description("API for LifeAI application")
                        .version("1.0"));
    }
}