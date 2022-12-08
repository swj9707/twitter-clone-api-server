package com.swj9707.twittercloneapiserver.config.security

import com.swj9707.twittercloneapiserver.utils.JwtTokenProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(private val jwtTokenProvider: JwtTokenProvider) {
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
            .requestMatchers("/api/v1/user/register","/api/v1/user/login").permitAll()
            .and()
            .addFilterBefore(JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}