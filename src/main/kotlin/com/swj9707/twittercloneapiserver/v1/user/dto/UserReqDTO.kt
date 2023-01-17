package com.swj9707.twittercloneapiserver.v1.user.dto

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import java.util.*

class UserReqDTO {
    companion object Req{
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

        data class EditProfile(
            val userId : UUID,
            val newUserName : String
        )

        data class EditPassword(
            val userId : UUID,
            val currentPassword : String,
            val newPassword : String
        )
    }
}