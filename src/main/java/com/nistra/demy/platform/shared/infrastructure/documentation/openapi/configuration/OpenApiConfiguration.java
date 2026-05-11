package com.nistra.demy.platform.shared.infrastructure.documentation.openapi.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class OpenApiConfiguration {

    // General Info
    @Value("${documentation.application.title}")
    private String applicationTitle;

    @Value("${documentation.application.description}")
    private String applicationDescription;

    @Value("${documentation.application.version}")
    private String applicationVersion;

    // Contact
    @Value("${documentation.application.contact.name}")
    private String contactName;

    @Value("${documentation.application.contact.email}")
    private String contactEmail;

    @Bean
    public OpenAPI demyPlatformOpenAPI() {

        // Define the security scheme for JWT
        var jwtSecurityScheme = new  SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        // Define the security requirement
        var securityRequirement =  new SecurityRequirement()
                .addList("Bearer Authentication");

        // Configure contact information
        var contact  = new Contact()
                .name(contactName)
                .email(contactEmail);

        // Configure API information
        var info = new Info()
                .title(applicationTitle)
                .description(applicationDescription)
                .version(applicationVersion)
                .contact(contact);

        return new OpenAPI()
                .openapi("3.0.1")
                .info(info)
                .servers(List.of(new Server().url("https://nistra-demy.up.railway.app")))
                .addSecurityItem(securityRequirement)
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", jwtSecurityScheme));
    }
}