package com.swj9707.twittercloneapiserver.v1.auth.dto

import java.util.*

class UserResDTO {
    companion object Res{
        data class Register(
            val userEmail : String,
            val userName : String,
        )
        data class Login (
            val userInfo : TwitterUserDTO,
            val tokenInfo : TokenInfo
            )
        data class TokenInfo (
            val accessToken : String,
            var refreshToken : String
            )
        data class Logout (
            val userEmail : String
            )
        data class EditProfile(
            val userInfo : TwitterUserDTO
        )

        data class EditPassword(
            val userId : UUID
        )
    }
}