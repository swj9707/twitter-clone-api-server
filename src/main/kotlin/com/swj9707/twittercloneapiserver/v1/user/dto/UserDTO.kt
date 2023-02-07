package com.swj9707.twittercloneapiserver.v1.user.dto

import com.swj9707.twittercloneapiserver.constant.dto.ImageDTO
import com.swj9707.twittercloneapiserver.constant.enum.Authority
import com.swj9707.twittercloneapiserver.constant.enum.Provider
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
            companion object Util {
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
            val email : String,
            val userName : String,
            val userNickname : String,
            val userRole : Authority,
            val profileImage : ImageDTO?,
            val backgroundImage: ImageDTO?,
            val provider : Provider,
            val lastLogin : LocalDateTime?,
        ) {
            companion object Util{
                fun entityToDTO(entity : TwitterUser) : TwitterUserInfo {
                    return TwitterUserInfo(
                        email = entity.email,
                        userName = entity.userName,
                        userNickname = entity.userNickname,
                        userRole = entity.userRole,
                        profileImage = entity.profileImage?.let {ImageDTO.entityToDTO(it)},
                        backgroundImage = entity.backgroundImage?.let { ImageDTO.entityToDTO(it) },
                        provider = entity.provider,
                        lastLogin = entity.lastLogin,
                    )
                }
            }

        }
    }
}