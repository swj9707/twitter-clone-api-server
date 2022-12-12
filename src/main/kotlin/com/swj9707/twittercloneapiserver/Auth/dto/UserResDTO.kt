package com.swj9707.twittercloneapiserver.Auth.dto

import org.springframework.http.HttpStatus

class UserResDTO {
    companion object res{
        data class Register(
            val userEmail : String,
            val userName : String,
        )
        data class Login (
            val status : HttpStatus,
            val tokenInfo : TokenInfo
            )

        data class TokenInfo (
            val accessToken : String?,
            var refreshToken : String?
            )
        data class Logout (
            val status : HttpStatus,
            val userEmail : String
            )
    }
}