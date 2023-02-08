package com.swj9707.twittercloneapiserver.v1.tweet.dto

import com.swj9707.twittercloneapiserver.constant.dto.ImageDTO
import com.swj9707.twittercloneapiserver.constant.enum.TweetStatus
import com.swj9707.twittercloneapiserver.v1.tweet.entity.Tweet
import org.springframework.data.domain.Slice
import java.util.*

data class TweetDTO (
    val tweetId : Long?,

    val userId : UUID,

    var tweetContent : String,

    var images : MutableList<ImageDTO.Dto.ImageInfo>?,

    var modified : Boolean,

    var status : TweetStatus
) {
    companion object Util {
        fun entityToDTO(entity : Tweet) : TweetDTO{
            return TweetDTO(
                tweetId = entity.tweetId,
                userId = entity.userId,
                tweetContent = entity.tweetContent,
                images = ImageDTO.Dto.ImageInfo.entitysToListDTO(entity.images),
                modified = entity.modified,
                status = entity.status
            )
        }

        fun pageEntityToDTO(pageEntity : Slice<Tweet>) : Slice<TweetDTO> {
            return pageEntity.map { entityToDTO(it) }
        }
    }
}