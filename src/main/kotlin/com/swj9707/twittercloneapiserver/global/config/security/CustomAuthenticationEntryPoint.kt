package com.swj9707.twittercloneapiserver.global.config.security

import com.swj9707.twittercloneapiserver.global.common.dto.BaseRes
import com.swj9707.twittercloneapiserver.global.common.enum.ResCode
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException
import kotlin.jvm.Throws

@Component
class CustomAuthenticationEntryPoint : AuthenticationEntryPoint {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Throws(IOException::class, ServletException::class)
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        response.outputStream.flush()
    }

    fun createExceptionResponse(authException: AuthenticationException) : BaseRes<String>{
        return BaseRes.failure(ResCode.UNAUTHORIZED)
    }
}