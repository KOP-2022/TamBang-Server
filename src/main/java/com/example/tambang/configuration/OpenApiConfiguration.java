package com.example.tambang.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
    @Bean
    public OpenAPI openAPI(){
        Info info = new Info()
                .title("tambang API Document")
                .version("v0.0.1")
                .description("tambang back-end project API specification");
        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
