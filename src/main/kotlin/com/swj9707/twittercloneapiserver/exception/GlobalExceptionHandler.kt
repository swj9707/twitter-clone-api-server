package com.swj9707.twittercloneapiserver.exception

import com.swj9707.twittercloneapiserver.constant.dto.BaseResponse
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    private val logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(BaseException::class)
    protected fun handleBaseException(e: BaseException): ResponseEntity<BaseResponse<String>> {
        return ResponseEntity.status(e.baseResponseCode.status)
            .body(BaseResponse.failure( e.baseResponseCode.status, e.baseResponseCode.message))
    }
}