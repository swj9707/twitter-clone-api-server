package com.swj9707.twittercloneapiserver.global.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.swj9707.twittercloneapiserver.global.common.dto.BaseResponse
import com.swj9707.twittercloneapiserver.global.common.enum.ResCode
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException
import kotlin.jvm.Throws

@Component
class CustomAuthenticationEntryPoint : AuthenticationEntryPoint {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val expiredExceptionResponse = BaseResponse.failure(
        ResCode.EXPIRED_TOKEN.status
        , ResCode.EXPIRED_TOKEN.message)

    @Throws(IOException::class, ServletException::class)
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        logger.error("Unauthorized Error : " + authException.message)
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpStatus.UNAUTHORIZED.value()
        val objectMapper = ObjectMapper()
        objectMapper.writeValue(response.outputStream, expiredExceptionResponse)
        response.outputStream.flush()
    }
}