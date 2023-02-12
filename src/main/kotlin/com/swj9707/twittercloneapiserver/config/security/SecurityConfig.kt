package com.swj9707.twittercloneapiserver.config.security

import com.swj9707.twittercloneapiserver.utils.JwtUtils
import com.swj9707.twittercloneapiserver.utils.RedisUtils
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
    private val redisUtils : RedisUtils,
    private val entryPoint: CustomAuthenticationEntryPoint) {
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun AuthenticationManager(authenticationConfiguration: AuthenticationConfiguration)
        = authenticationConfiguration.authenticationManager

    @Bean
    fun filterChain(http: HttpSecurity) : SecurityFilterChain {
        http
            .csrf().disable()
            .httpBasic().disable()
            .authorizeHttpRequests()
            .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
            .requestMatchers(
                "/docs/**",
                "/v3/api-docs",
                "/swagger*/**", "/api/auth/v1/**").permitAll()
            .requestMatchers("/api/v1/**").authenticated()
            .and()
            .addFilterBefore(JwtAuthenticationFilter(jwtUtils, redisUtils), UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling().authenticationEntryPoint(CustomAuthenticationEntryPoint())
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()



        return http.build()
    }

}