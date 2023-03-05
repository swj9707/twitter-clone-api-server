package com.swj9707.twittercloneapiserver.global.exception

import com.swj9707.twittercloneapiserver.global.common.dto.BaseResponse
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
    protected fun handleBaseException(e: CustomException): ResponseEntity<BaseResponse<String>> {
        logger.error("Exception : " + e.message)
        return ResponseEntity.status(e.resCode.status)
            .body(BaseResponse.failure( e.resCode.status, e.resCode.message))
    }

    @ExceptionHandler(Exception::class)
    protected  fun handleException (e : Exception) : ResponseEntity<BaseResponse<String>> {
        logger.error("Exception : " + e.message)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                BaseResponse.failure(
                    ResCode.INTERNAL_SERVER_ERROR.status,
                ResCode.INTERNAL_SERVER_ERROR.message))
    }
}