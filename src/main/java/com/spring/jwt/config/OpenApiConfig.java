package com.spring.jwt.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenApiCustomiser openApiCustomiser() {
        return openApi -> {
            Server localServer = new Server();
            localServer.setUrl("http://localhost:8086");
            localServer.setDescription("Local Server");

            Server productionServer = new Server();
            productionServer.setUrl("https://cffffftasting-production.up.railway.app/");
            productionServer.setDescription("Production Server");

            openApi.setServers(List.of(localServer, productionServer));
        };
    }
}
