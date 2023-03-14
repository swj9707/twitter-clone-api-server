package com.swj9707.twittercloneapiserver.global.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.swj9707.twittercloneapiserver.global.common.dto.BaseRes
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

    @Throws(IOException::class, ServletException::class)
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {

        logger.error("UNAUTHORIZED : " + request.getAttribute("exception").toString())

        when (request.getAttribute("exception").toString()) {
            ResCode.TOKEN_INVALID_SIGNATURE.errorCode -> {
                setErrorResponse(response, ResCode.TOKEN_INVALID_SIGNATURE)
            }

            ResCode.TOKEN_MALFORMED.errorCode -> {
                setErrorResponse(response, ResCode.TOKEN_MALFORMED)
            }

            ResCode.TOKEN_UNSUPPORTED.errorCode -> {
                setErrorResponse(response, ResCode.TOKEN_UNSUPPORTED)
            }

            ResCode.TOKEN_ILLEGAL_ARGUMENT.errorCode -> {
                setErrorResponse(response, ResCode.TOKEN_ILLEGAL_ARGUMENT)
            }

            ResCode.EXPIRED_TOKEN.errorCode -> {
                setErrorResponse(response, ResCode.EXPIRED_TOKEN)
            }

            else -> {
                setErrorResponse(response, ResCode.UNAUTHORIZED)
            }
        }
    }

    private fun setErrorResponse(
        res: HttpServletResponse,
        tokenStatus: ResCode
    ) {
        res.contentType = MediaType.APPLICATION_JSON_VALUE
        res.status = HttpStatus.UNAUTHORIZED.value()
        val objectMapper = ObjectMapper()
        objectMapper.writeValue(res.outputStream, BaseRes.failure(tokenStatus))
    }
}