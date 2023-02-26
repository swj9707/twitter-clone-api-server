package com.swj9707.twittercloneapiserver.v1.user.dto

import com.swj9707.twittercloneapiserver.common.dto.ImageDTO
import com.swj9707.twittercloneapiserver.common.enum.Authority
import com.swj9707.twittercloneapiserver.common.enum.Provider
import com.swj9707.twittercloneapiserver.v1.tweet.entity.repository.projection.TweetProjection
import com.swj9707.twittercloneapiserver.v1.user.entity.TwitterUser
import java.util.*

class UserDTO {
    companion object Dto {
        data class TwitterUserAuthInfo(
            val userId: UUID,
            val provider: Provider,
        ) {
            companion object Util {
                fun fromEntity(entity: TwitterUser): TwitterUserAuthInfo {
                    return TwitterUserAuthInfo(
                        userId = entity.userId,
                        provider = entity.provider,
                    )
                }
            }
        }

        data class TwitterUserProfile(
            val userName: String,
            val userNickname: String,
            val profileImage: ImageDTO.Dto.ImageInfo?,
            val backgroundImage: ImageDTO.Dto.ImageInfo?,
            val followerCount: Int,
            val followingCount: Int
        ) {
            companion object Util {
                fun fromEntity(entity: TwitterUser): TwitterUserProfile {
                    return TwitterUserProfile(
                        userName = entity.userName,
                        userNickname = entity.userNickname,
                        profileImage = entity.profileImage?.let { ImageDTO.Dto.ImageInfo.entityToDTO(it) },
                        backgroundImage = entity.backgroundImage?.let { ImageDTO.Dto.ImageInfo.entityToDTO(it) },
                        followerCount = entity.followers.size,
                        followingCount = entity.followers.size
                    )
                }
            }
        }

        data class TwitterUserInfo(
            val userId : UUID,
            val email: String,
            val userName: String,
            val userNickname: String,
            val userRole: Authority,
            val profileImage: ImageDTO.Dto.ImageInfo?,
            val backgroundImage: ImageDTO.Dto.ImageInfo?,
            val provider: Provider,
            val lastLogin: String?,

            ) {
            companion object Util {
                fun fromEntity(entity: TwitterUser): TwitterUserInfo {
                    return TwitterUserInfo(
                        userId = entity.userId,
                        email = entity.email,
                        userName = entity.userName,
                        userNickname = entity.userNickname,
                        userRole = entity.userRole,
                        profileImage = entity.profileImage?.let { ImageDTO.Dto.ImageInfo.entityToDTO(it) },
                        backgroundImage = entity.backgroundImage?.let { ImageDTO.Dto.ImageInfo.entityToDTO(it) },
                        provider = entity.provider,
                        lastLogin = entity.lastLogin.toString()
                    )
                }
            }
        }

        data class TweetOwnerInfo(
            val userName: String, val userNickname: String, val profileImage: ImageDTO.Dto.ImageInfo?
        ) {
            companion object Util {
                fun fromEntity(entity: TwitterUser): TweetOwnerInfo {
                    return TweetOwnerInfo(userName = entity.userName,
                        userNickname = entity.userNickname,
                        profileImage = entity.profileImage?.let { ImageDTO.Dto.ImageInfo.entityToDTO(it) })
                }

                fun fromProjection(projection: TweetProjection.UserProjection): TweetOwnerInfo {
                    return TweetOwnerInfo(userName = projection.getUserName(),
                        userNickname = projection.getUserNickname(),
                        profileImage = projection.getProfileImage()?.let { ImageDTO.Dto.ImageInfo.projectionToDTO(it) })
                }
            }
        }
    }
}