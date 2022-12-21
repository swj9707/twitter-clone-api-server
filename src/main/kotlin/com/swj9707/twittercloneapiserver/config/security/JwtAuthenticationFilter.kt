package com.swj9707.twittercloneapiserver.config.security

import com.swj9707.twittercloneapiserver.utils.JwtUtil
import com.swj9707.twittercloneapiserver.utils.RedisUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.ObjectUtils
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException
import kotlin.jvm.Throws

class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
    private val redisUtil: RedisUtil): GenericFilterBean() {
    @Throws(IOException::class, ServletException::class, Exception::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        var token: String? = jwtUtil.resolveToken((request as HttpServletRequest))

        if (token != null && jwtUtil.validateToken(token)) {
            val isLogout = redisUtil.getData(token)
            if(ObjectUtils.isEmpty(isLogout)){
                val authentication = jwtUtil.getAuthentication(token)
                SecurityContextHolder.getContext().authentication = authentication
            }
        }

        chain.doFilter(request, response)
    }

}