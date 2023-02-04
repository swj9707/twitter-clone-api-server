package com.swj9707.twittercloneapiserver.v1.tweet.dto

import com.swj9707.twittercloneapiserver.constant.entity.Image
import com.swj9707.twittercloneapiserver.constant.enum.TweetStatus
import com.swj9707.twittercloneapiserver.v1.tweet.entity.Tweet
import org.springframework.data.domain.Slice
import java.util.*

data class TweetDTO (
    val tweetId : Long?,

    val userId : UUID,

    var tweetContent : String,

    var images : MutableList<Image>?,

    var modified : Boolean,

    var status : TweetStatus
) {
    companion object Util {
        fun entityToDTO(entity : Tweet) : TweetDTO{
            return TweetDTO(
                tweetId = entity.tweetId,
                userId = entity.userId,
                tweetContent = entity.tweetContent,
                images = entity.images,
                modified = entity.modified,
                status = entity.status
            )
        }

        fun pageEntityToDTO(pageEntity : Slice<Tweet>) : Slice<TweetDTO> {
            return pageEntity.map { entityToDTO(it) }
        }
    }
}