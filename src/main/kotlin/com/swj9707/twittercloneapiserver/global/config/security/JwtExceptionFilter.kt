package com.swj9707.twittercloneapiserver.global.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.swj9707.twittercloneapiserver.global.common.dto.BaseResponse
import com.swj9707.twittercloneapiserver.global.common.enum.ResCode
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter

class JwtExceptionFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try{
            filterChain.doFilter(request, response)
        } catch (ex : Exception) {
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.status = HttpStatus.UNAUTHORIZED.value()
            val objectMapper = ObjectMapper()
            objectMapper.writeValue(response.outputStream, createExceptionResponse(ex.message.toString()))
            response.outputStream.flush()

        }
    }
    fun createExceptionResponse(message : String) : BaseResponse<String> {
        return BaseResponse.failure(ResCode.UNAUTHORIZED.status, message)
    }
}