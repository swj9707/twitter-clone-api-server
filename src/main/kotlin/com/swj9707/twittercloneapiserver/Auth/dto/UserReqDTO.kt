package com.swj9707.twittercloneapiserver.Auth.dto

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class UserReqDTO {
    companion object req{
        data class Register(
            val userEmail : String,
            val userName : String,
            val password : String
        )

        data class Login(
            val userEmail : String,
            val password : String
        ){
            fun toAuthentication() : UsernamePasswordAuthenticationToken{
                return UsernamePasswordAuthenticationToken(userEmail, password)
            }
        }
        data class Reissue(
            val accessToken : String = "",
            val refreshToken : String = ""
        )
        data class Logout(
            val accessToken : String,
            val refreshToken : String
        )
    }
}