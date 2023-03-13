package com.swj9707.twittercloneapiserver.global.config.security

import com.swj9707.twittercloneapiserver.global.utils.JwtUtils
import com.swj9707.twittercloneapiserver.global.utils.RedisUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsUtils

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig(
    private val jwtUtils: JwtUtils,
    private val redisUtils: RedisUtils,
) {
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun AuthenticationManager(authenticationConfiguration: AuthenticationConfiguration) =
        authenticationConfiguration.authenticationManager

    @Bean
    fun filterChain(http: HttpSecurity) : SecurityFilterChain {
        http
            .httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(JwtAuthenticationFilter(jwtUtils, redisUtils), UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling().authenticationEntryPoint(CustomAuthenticationEntryPoint())
            .accessDeniedHandler(AccessDeniedHandlerImpl())
            .and()
            .authorizeHttpRequests()
            .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
            .requestMatchers("/api/auth/v1/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()
            .requestMatchers("/api/v1/**").authenticated()

        return http.build()
    }

}