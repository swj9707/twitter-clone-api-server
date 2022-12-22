package com.swj9707.twittercloneapiserver.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.swj9707.twittercloneapiserver.constant.dto.BaseResponse
import com.swj9707.twittercloneapiserver.constant.enum.BaseResponseCode
import com.swj9707.twittercloneapiserver.utils.JwtUtil
import com.swj9707.twittercloneapiserver.utils.RedisUtil
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
    private val jwtUtil: JwtUtil,
    private val redisUtil: RedisUtil): OncePerRequestFilter() {
    @Throws(IOException::class, ServletException::class, Exception::class, ExpiredJwtException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val path = request.servletPath
            val token: String? = jwtUtil.resolveToken((request))

            when {
                !path.startsWith("/api/auth/v1/reissue") && token != null && jwtUtil.validateToken(token) -> {
                    val isLogout = redisUtil.getData(token)
                    if (ObjectUtils.isEmpty(isLogout)) {
                        val authentication = jwtUtil.getAuthentication(token)
                        SecurityContextHolder.getContext().authentication = authentication
                    }
                }
            }
            filterChain.doFilter(request, response)

        } catch (e : ExpiredJwtException){
            val res = BaseResponse.failure(BaseResponseCode.EXPIRED_TOKEN.status, BaseResponseCode.EXPIRED_TOKEN.message)
            response.status = BaseResponseCode.EXPIRED_TOKEN.status.value()
            response.writer.write(ObjectMapper().writeValueAsString(res))
            response.writer.flush()
        }
    }

}