package com.swj9707.twittercloneapiserver.constant.dto

import org.springframework.http.HttpStatus

data class BaseResponse<T> (
    val status : HttpStatus,
    val data : T,
    val message : String
    ){
    companion object {
        fun <T> success(data : T) = BaseResponse<T> (
                status = HttpStatus.OK,
                data = data,
                message = "SUCCESS"
        )

        fun failure(status : HttpStatus, message : String) = BaseResponse<String> (
            status = status,
            data = message,
            message = "FAILURE"
        )
    }

}