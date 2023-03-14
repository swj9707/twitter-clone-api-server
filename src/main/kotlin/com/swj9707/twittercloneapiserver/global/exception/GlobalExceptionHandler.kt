package com.swj9707.twittercloneapiserver.global.exception

import com.swj9707.twittercloneapiserver.global.common.dto.BaseRes
import com.swj9707.twittercloneapiserver.global.common.enum.ResCode
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    private val logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(CustomException::class)
    protected fun handleBaseException(e: CustomException): ResponseEntity<BaseRes<String>> {
        logger.error("Exception : " + e.message)
        return ResponseEntity.status(e.resCode.status)
            .body(BaseRes.failure(e.resCode))
    }

    @ExceptionHandler(Exception::class)
    protected fun handleException(e: Exception): ResponseEntity<BaseRes<String>> {
        logger.error("Exception : " + e.message)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                BaseRes.failure(
                    ResCode.INTERNAL_SERVER_ERROR
                )
            )
    }
}