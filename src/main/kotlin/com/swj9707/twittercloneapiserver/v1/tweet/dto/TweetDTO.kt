package com.swj9707.twittercloneapiserver.v1.tweet.dto

import com.swj9707.twittercloneapiserver.constant.dto.ImageDTO
import com.swj9707.twittercloneapiserver.constant.enum.TweetStatus
import com.swj9707.twittercloneapiserver.v1.tweet.entity.Tweet
import com.swj9707.twittercloneapiserver.v1.tweet.repository.projection.TweetProjection
import com.swj9707.twittercloneapiserver.v1.user.dto.UserDTO
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
                        images = ImageDTO.Dto.ImageInfo.entitiesToListDTO(entity.images),
                        modified = entity.modified,
                        createdAt = entity.createAt.toString(),
                        status = entity.status,
                        userInfo = UserDTO.Dto.TweetOwnerInfo.entityToDTO(entity.user)
                    )
                }

                fun projectionToDTO( projection: TweetProjection) : TweetInfo {
                    return TweetInfo(
                        tweetId = projection.getTweetId(),
                        tweetContent = projection.getTweetContent(),
                        images = ImageDTO.Dto.ImageInfo.projectionsToListDTO(projection.getImages()),
                        modified = projection.getModified(),
                        createdAt = projection.getCreateAt().toString(),
                        status = projection.getStatus(),
                        userInfo = UserDTO.Dto.TweetOwnerInfo.projectionToDTO(projection.getUser())
                    )
                }

                fun toPageableDTO(pageEntity : Slice<TweetProjection>) : Slice<TweetInfo> {
                    return pageEntity.map { projectionToDTO(it) }
                }
            }
        }
    }
}



