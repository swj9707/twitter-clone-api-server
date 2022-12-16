package com.swj9707.twittercloneapiserver.auth.dto

import com.swj9707.twittercloneapiserver.auth.entity.TwitterUser
import com.swj9707.twittercloneapiserver.constant.enum.Authority
import com.swj9707.twittercloneapiserver.constant.enum.Provider
import com.swj9707.twittercloneapiserver.constant.enum.UserStatus
import java.time.LocalDateTime
import java.util.*

data class TwitterUserDTO (
    val userId : UUID,
    val email : String,
    val userName : String,
    val userRole : Authority,
    val provider : Provider,
    val userStatus : UserStatus,
    val lastLogin : LocalDateTime?,
) {
    companion object Util {
        fun entityToDTO(entity : TwitterUser) : TwitterUserDTO{
            return TwitterUserDTO(
                userId = entity.userId,
                email = entity.email,
                userName = entity.userName,
                userRole = entity.userRole,
                provider = entity.provider,
                userStatus = entity.userStatus,
                lastLogin = entity.lastLogin
            )
        }
    }

}