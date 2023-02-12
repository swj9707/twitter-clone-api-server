package com.swj9707.twittercloneapiserver.config.swagger

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI? {
        val info: Info = Info()
            .title("Twitter Clone Project API Server Docs")
            .version("v1.0.0")
            .description("Twitter Clone Project API Server API 문서 페이지")

        val jwtSchemeName : String = "jwtAuth"
        val securityRequirement: SecurityRequirement = SecurityRequirement().addList(jwtSchemeName)

        val components = Components()
            .addSecuritySchemes(
                jwtSchemeName, SecurityScheme()
                    .name(jwtSchemeName)
                    .type(SecurityScheme.Type.HTTP) // HTTP 방식
                    .scheme("bearer")
                    .bearerFormat("JWT")
            )

        return OpenAPI()
            .info(info)
            .addSecurityItem(securityRequirement)
            .components(components)
    }
}