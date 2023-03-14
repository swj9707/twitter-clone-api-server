package com.swj9707.twittercloneapiserver.global.config.security

import com.swj9707.twittercloneapiserver.global.common.enum.ResCode
import com.swj9707.twittercloneapiserver.global.utils.JwtUtils
import com.swj9707.twittercloneapiserver.global.utils.RedisUtils
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.ObjectUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException


class JwtAuthenticationFilter(
    private val jwtUtils: JwtUtils, private val redisUtils: RedisUtils
) : OncePerRequestFilter() {
    private val logger = LoggerFactory.getLogger(javaClass)
    @Throws(
        IOException::class,
        ServletException::class,
        Exception::class,
        ExpiredJwtException::class
    )
    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
    ) {
        val path = request.servletPath
        val token: String = jwtUtils.resolveToken((request))
        val tokenStatus = jwtUtils.validateToken(token)
        if (!(path.startsWith("/api/auth/v1/reissue") || path.startsWith("/api/auth/v1/login"))) {
            if (tokenStatus == ResCode.OK) {
                val isLogout = redisUtils.getData(token)
                if (ObjectUtils.isEmpty(isLogout)) {
                    val authentication = jwtUtils.getAuthentication(token)
                    SecurityContextHolder.getContext().authentication = authentication
                }
            } else {
                request.setAttribute("exception", tokenStatus.errorCode)
            }
        }

        filterChain.doFilter(request, response)
    }

}