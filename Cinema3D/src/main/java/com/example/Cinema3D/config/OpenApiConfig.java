package com.example.Cinema3D.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI cinemaOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cinema3D API")
                        .description("REST API for cinema management and ticket booking")
                        .version("v1"));
    }
}
