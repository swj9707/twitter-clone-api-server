package com.swj9707.twittercloneapiserver.config.security

import com.swj9707.twittercloneapiserver.utils.JwtUtil
import com.swj9707.twittercloneapiserver.utils.RedisUtil
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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig(
    private val jwtUtil: JwtUtil,
    private val redisUtil : RedisUtil,
    private val entryPoint: CustomAuthenticationEntryPoint) {
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun AuthenticationManager(authenticationConfiguration: AuthenticationConfiguration)
        = authenticationConfiguration.authenticationManager

    @Bean
    fun filterChain(http: HttpSecurity) : SecurityFilterChain {
        http
            .httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests()
            .requestMatchers("/api/v1/**").authenticated()
            .requestMatchers("/api/auth/v1/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()
            .and()
            .addFilterBefore(JwtAuthenticationFilter(jwtUtil, redisUtil), UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}