package com.swj9707.twittercloneapiserver.Auth.dto.register

data class TwitterUserRegistReq (
        val userEmail : String,
        val userName : String,
        val password : String
        )