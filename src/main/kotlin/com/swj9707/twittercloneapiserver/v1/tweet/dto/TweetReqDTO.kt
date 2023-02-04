package com.swj9707.twittercloneapiserver.v1.tweet.dto

import com.swj9707.twittercloneapiserver.constant.dto.ImageDTO

class TweetReqDTO {
    companion object Req {
        data class CreateTweet(
            val tweetContent : String,
            val tweetImages : MutableList<ImageDTO>?
        )
        data class DeleteTweet(
            val tweetId : Long,
        )

    }
}