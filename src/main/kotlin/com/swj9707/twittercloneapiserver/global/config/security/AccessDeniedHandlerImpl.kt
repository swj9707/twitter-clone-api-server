package com.swj9707.twittercloneapiserver.global.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.swj9707.twittercloneapiserver.global.common.dto.BaseResponse
import com.swj9707.twittercloneapiserver.global.common.enum.ResCode
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler as AccessDeniedHandler

class AccessDeniedHandlerImpl : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpStatus.UNAUTHORIZED.value()
        val objectMapper = ObjectMapper()
        objectMapper.writeValue(
            response.outputStream,
            BaseResponse.failure(ResCode.FORBIDDEN.status, accessDeniedException.message.toString())
        )
        response.outputStream.flush()
    }
}