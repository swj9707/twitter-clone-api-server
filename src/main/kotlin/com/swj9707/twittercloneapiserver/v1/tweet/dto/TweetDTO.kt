package com.swj9707.twittercloneapiserver.v1.tweet.dto

import com.swj9707.twittercloneapiserver.constant.dto.ImageDTO
import com.swj9707.twittercloneapiserver.constant.enum.TweetStatus
import com.swj9707.twittercloneapiserver.v1.tweet.entity.Tweet
import com.swj9707.twittercloneapiserver.v1.user.dto.UserDTO
import com.swj9707.twittercloneapiserver.v1.user.entity.TwitterUser
import org.springframework.data.domain.Slice

class TweetDTO {
    companion object Dto {
        data class TweetInfo(
            val tweetId : Long?,

            var tweetContent : String,

            var images : MutableList<ImageDTO.Dto.ImageInfo>?,

            var modified : Boolean,

            var createdAt : String,

            var status : TweetStatus,

            var userInfo: UserDTO.Dto.TweetOwnerInfo
        ) {
            companion object Util {
                fun entityToDTO(entity : Tweet) : TweetInfo{
                    return TweetInfo(
                        tweetId = entity.tweetId,
                        tweetContent = entity.tweetContent,
                        images = ImageDTO.Dto.ImageInfo.entitysToListDTO(entity.images),
                        modified = entity.modified,
                        createdAt = entity.createAt.toString(),
                        status = entity.status,
                        userInfo = UserDTO.Dto.TweetOwnerInfo.entityToDTO(entity.user)
                    )
                }

                fun pageEntityToDTO(pageEntity : Slice<Tweet>) : Slice<TweetInfo> {
                    return pageEntity.map { entityToDTO(it) }
                }
            }
        }

        data class TweetData (
            val tweetInfo : TweetInfo,
            val userInfo : UserDTO.Dto.TweetOwnerInfo
        ) {
            companion object Util {
                fun entityToDTO(tweet: Tweet, user : TwitterUser) : TweetData {
                    return TweetData(tweetInfo = TweetInfo.entityToDTO(tweet),
                        userInfo = UserDTO.Dto.TweetOwnerInfo.entityToDTO(user))
                }
            }
        }
    }
}



