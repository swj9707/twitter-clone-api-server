package com.swj9707.twittercloneapiserver.v1.user.dto.vo

import com.swj9707.twittercloneapiserver.v1.user.dto.UserDTO
import java.util.*

class UserResDTO {
    companion object Res {
        data class Register(
            val userEmail: String,
            val userName: String,
        )

        data class Login(
            val userInfo: UserDTO.Dto.TwitterUserAuthInfo,
            val tokenInfo: TokenInfo
        )

        data class TokenInfo(
            val accessToken: String,
            var refreshToken: String
        )

        data class Logout(
            val userEmail: String
        )

        data class UserInfo(
            val userInfo: UserDTO.Dto.TwitterUserInfo
        )

        data class UserProfile(
            val userProfile: UserDTO.Dto.TwitterUserProfile,
            val countOfTweet: Int
        )

        data class EditProfile(
            val userInfo: UserDTO.Dto.TwitterUserProfile
        )

        data class EditPassword(
            val userId: UUID
        )

        data class FollowRes(
            val userId : UUID
        )
    }
}