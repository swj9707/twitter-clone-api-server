package com.swj9707.twittercloneapiserver.v1.tweet.dto.vo

import com.swj9707.twittercloneapiserver.global.common.dto.ImageDTO

class TweetReqDTO {
    companion object Req {
        data class CreateTweet(
            val tweetId : Long?,
            val tweetContent : String,
            val tweetImages : MutableList<ImageDTO.Dto.ImageInfo>?
        )

        data class DeleteTweet(
            val tweetId : Long,
        )

    }
}