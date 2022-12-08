package com.swj9707.twittercloneapiserver.constant.dto

import org.springframework.http.HttpStatus

 open class BaseResponse (
    val status : HttpStatus,
    val message : String
    )