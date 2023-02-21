package com.swj9707.twittercloneapiserver.v1.user.dto

import com.swj9707.twittercloneapiserver.constant.dto.ImageDTO
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import java.util.*

class UserReqDTO {
    companion object Req {
        data class Register(
            val userEmail: String,
            val userName: String,
            val userNickname: String,
            val password: String
        )

        data class Login(
            val userEmail: String,
            val password: String
        ) {
            fun toAuthentication(): UsernamePasswordAuthenticationToken {
                return UsernamePasswordAuthenticationToken(userEmail, password)
            }
        }

        data class EditUserProfile(
            val userId: UUID,
            val newUserNickname: String,
            val profileImage: ImageDTO.Dto.ImageInfo,
            val backgroundImage: ImageDTO.Dto.ImageInfo
        )

        data class EditProfile(
            val userId: UUID, val newUserNickname: String
        )

        data class EditPassword(
            val userId: UUID, val currentPassword: String, val newPassword: String
        )
    }
}