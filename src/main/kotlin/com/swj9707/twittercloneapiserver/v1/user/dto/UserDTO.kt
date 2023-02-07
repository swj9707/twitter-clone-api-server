package com.swj9707.twittercloneapiserver.v1.user.dto

import com.swj9707.twittercloneapiserver.constant.dto.ImageDTO
import com.swj9707.twittercloneapiserver.constant.enum.Authority
import com.swj9707.twittercloneapiserver.constant.enum.Provider
import com.swj9707.twittercloneapiserver.constant.enum.UserStatus
import com.swj9707.twittercloneapiserver.v1.user.entity.TwitterUser
import java.time.LocalDateTime
import java.util.*

class UserDTO {
    companion object Dto {
        data class TwitterUserAuthInfo (
            val userId : UUID,
            val provider : Provider,
        ) {
            companion object Util {
                fun entityToDTO(entity : TwitterUser) : TwitterUserAuthInfo {
                    return TwitterUserAuthInfo(
                        userId = entity.userId,
                        provider = entity.provider,
                    )
                }
            }
        }

        data class TwitterUserProfile (
            val userName : String,
            val userNickname: String,
            val profileImage: ImageDTO?,
            val backgroundImage: ImageDTO?
        ) {
            companion object util {
                fun entityToDTO(entity: TwitterUser): TwitterUserProfile {
                    return TwitterUserProfile(
                        userName = entity.userName,
                        userNickname = entity.userNickname,
                        profileImage = entity.profileImage?.let { ImageDTO.entityToDTO(it) },
                        backgroundImage = entity.backgroundImage?.let { ImageDTO.entityToDTO(it) }
                    )
                }
            }
        }
        data class TwitterUserInfo (
            val userId : UUID,
            val email : String,
            val userName : String,
            val userNickname : String,
            val userRole : Authority,
            val provider : Provider,
            val userStatus : UserStatus,
            val lastLogin : LocalDateTime?,
            val profileImage : ImageDTO?,
            val backgroundImage : ImageDTO?
        ) {
            companion object util{
                fun entityToDTO(entity : TwitterUser) : TwitterUserInfo {
                    return TwitterUserInfo(
                        userId = entity.userId,
                        email = entity.email,
                        userName = entity.userName,
                        userNickname = entity.userNickname,
                        userRole = entity.userRole,
                        provider = entity.provider,
                        userStatus = entity.userStatus,
                        lastLogin = entity.lastLogin,
                        profileImage = entity.profileImage?.let { ImageDTO.entityToDTO(it) },
                        backgroundImage = entity.backgroundImage?.let { ImageDTO.entityToDTO(it) }
                    )
                }
            }

        }
    }
}