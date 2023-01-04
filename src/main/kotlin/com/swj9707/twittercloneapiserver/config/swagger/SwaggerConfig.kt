package com.swj9707.twittercloneapiserver.config.swagger

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.ApiKey
import springfox.documentation.service.AuthorizationScope
import springfox.documentation.service.SecurityReference
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import java.util.*

@Configuration
class SwaggerConfig {

    @Bean
    fun api() : Docket {
        return Docket(DocumentationType.OAS_30)
                    .securityContexts(listOf(securityContext()))
                    .securitySchemes(listOf(apiKey()))
                    .useDefaultResponseMessages(false)
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("com.example.swagger.controller"))
                    .paths(PathSelectors.any())
                    .build()
                    .apiInfo(apiInfo());
    }

    fun securityContext() : SecurityContext {
        return SecurityContext.builder()
            .securityReferences(defaultAuth())
            .build();
    }

    fun defaultAuth() : List<SecurityReference> {
        val authorizationScope = AuthorizationScope("global", "accessEverything")
        val authorizationScopes = arrayOfNulls<AuthorizationScope>(1)
        authorizationScopes[0] = authorizationScope
        return listOf(SecurityReference("Authorization", authorizationScopes))
    }

    fun apiKey() : ApiKey {
        return ApiKey("Authorization", "Authorization", "header")
    }

    fun apiInfo() : ApiInfo {
        return ApiInfoBuilder()
            .title("Twitter Clone Project API Server")
            .description("Twitter Clone Project Backend API 문서")
            .version("1.0")
            .build()
    }
}