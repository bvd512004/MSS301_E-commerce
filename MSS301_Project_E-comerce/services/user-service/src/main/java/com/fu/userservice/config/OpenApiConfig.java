package com.fu.userservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("user-service")
                        .description("Tài khoản và hồ sơ khách hàng")
                        .version("v1"))
                .servers(List.of(new Server().url("/").description("API Gateway")));
    }

}
