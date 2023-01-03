package com.swj9707.twittercloneapiserver.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.swj9707.twittercloneapiserver.constant.dto.BaseResponse
import com.swj9707.twittercloneapiserver.constant.enum.BaseResponseCode
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

    private val expiredExceptionResponse = BaseResponse.failure(BaseResponseCode.EXPIRED_TOKEN.status
        , BaseResponseCode.EXPIRED_TOKEN.message)

    @Throws(IOException::class, ServletException::class)
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        logger.error("Unauthorized Error : " + authException.message.toString())
        //CORS 에러 방지용 (개선 필요)
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"))
        response.setHeader("Access-Control-Allow-Credentials", "true")
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");
        response.status = HttpStatus.UNAUTHORIZED.value()
        val objectMapper = ObjectMapper()
        objectMapper.writeValue(response.outputStream, expiredExceptionResponse)
        response.outputStream.flush()
    }
}