package com.swj9707.twittercloneapiserver.Auth.dto.signin

import org.springframework.http.HttpStatus

data class TwitterUserLoginRes (
        val status : HttpStatus,
        val token : String
        )