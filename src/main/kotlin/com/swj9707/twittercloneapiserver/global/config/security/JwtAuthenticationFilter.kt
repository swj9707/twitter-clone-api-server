package com.swj9707.twittercloneapiserver.global.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.swj9707.twittercloneapiserver.global.common.dto.BaseRes
import com.swj9707.twittercloneapiserver.global.common.enum.JwtTokenStatus
import com.swj9707.twittercloneapiserver.global.utils.JwtUtils
import com.swj9707.twittercloneapiserver.global.utils.RedisUtils
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.ObjectUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException


class JwtAuthenticationFilter(
    private val jwtUtils: JwtUtils, private val redisUtils: RedisUtils
) : OncePerRequestFilter() {
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
            if (tokenStatus == JwtTokenStatus.VALID) {
                val isLogout = redisUtils.getData(token)
                if (ObjectUtils.isEmpty(isLogout)) {
                    val authentication = jwtUtils.getAuthentication(token)
                    SecurityContextHolder.getContext().authentication = authentication
                }
            } else {
                setErrorResponse(response, tokenStatus)
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun setErrorResponse(
        res: HttpServletResponse,
        tokenStatus: JwtTokenStatus
    ) {
        logger.error("Unauthorized Error : " + tokenStatus.message)
        res.contentType = MediaType.APPLICATION_JSON_VALUE
        res.status = HttpStatus.UNAUTHORIZED.value()
        val objectMapper = ObjectMapper()
        objectMapper.writeValue(res.outputStream, BaseRes.failure(tokenStatus))
        res.outputStream.flush()
    }

}