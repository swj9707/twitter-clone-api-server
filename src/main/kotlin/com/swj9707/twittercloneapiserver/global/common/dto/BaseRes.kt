package com.swj9707.twittercloneapiserver.global.common.dto

import com.swj9707.twittercloneapiserver.global.common.enum.ResCode
import org.springframework.http.HttpStatus

data class BaseRes<T> (
    val status : HttpStatus,
    val data : T,
    val message : String
    ){
    companion object {
        fun <T> success(data : T) = BaseRes(
                status = HttpStatus.OK,
                data = data,
                message = "SUCCESS"
        )

        fun failure(resCode: ResCode) = BaseRes(
            status = resCode.status,
            data = resCode.errorCode,
            message = resCode.message
        )
    }

}