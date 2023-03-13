package com.swj9707.twittercloneapiserver.global.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.swj9707.twittercloneapiserver.global.common.dto.BaseResponse
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
import kotlin.jvm.Throws

class JwtAuthenticationFilter(
    private val jwtUtils: JwtUtils, private val redisUtils: RedisUtils
) : OncePerRequestFilter() {
    @Throws(IOException::class, ServletException::class, Exception::class, ExpiredJwtException::class)
    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
    ) {
        val path = request.servletPath
        val token: String? = jwtUtils.resolveToken((request))
        try {
            if(!path.startsWith("/api/auth/v1/reissue") && (token != null) && jwtUtils.validateToken(token)){
                val isLogout = redisUtils.getData(token)
                if (ObjectUtils.isEmpty(isLogout)) {
                    val authentication = jwtUtils.getAuthentication(token)
                    SecurityContextHolder.getContext().authentication = authentication
                }
            }
        } catch(e : Exception) {
            sendErrorResponse(response, e.message.toString())
        }
        filterChain.doFilter(request, response)
    }

    @Throws(IOException::class)
    fun sendErrorResponse(response: HttpServletResponse, message : String){
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpStatus.UNAUTHORIZED.value()
        val objectMapper = ObjectMapper()
        objectMapper.writeValue(response.outputStream, BaseResponse.failure(HttpStatus.UNAUTHORIZED, message))
        response.outputStream.flush()
    }

}