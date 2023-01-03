package com.swj9707.twittercloneapiserver.config.security

import com.swj9707.twittercloneapiserver.utils.JwtUtils
import com.swj9707.twittercloneapiserver.utils.RedisUtils
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.ObjectUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import kotlin.jvm.Throws

class JwtAuthenticationFilter(
    private val jwtUtils: JwtUtils,
    private val redisUtils: RedisUtils): OncePerRequestFilter() {
    @Throws(IOException::class, ServletException::class, Exception::class, ExpiredJwtException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val path = request.servletPath
        val token: String? = jwtUtils.resolveToken((request))
        when {
            !path.startsWith("/api/auth/v1/reissue") && token != null && jwtUtils.validateToken(token) -> {
                val isLogout = redisUtils.getData(token)
                if (ObjectUtils.isEmpty(isLogout)) {
                    val authentication = jwtUtils.getAuthentication(token)
                    SecurityContextHolder.getContext().authentication = authentication
                }
            }
        }
        filterChain.doFilter(request, response)
    }

}