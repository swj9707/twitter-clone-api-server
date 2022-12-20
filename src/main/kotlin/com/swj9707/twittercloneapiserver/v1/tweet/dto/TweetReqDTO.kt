package com.swj9707.twittercloneapiserver.v1.tweet.dto

import java.util.*

class TweetReqDTO {

    companion object Req {

        data class CreateTweet(
            val tweetContent : String,
            val tweetImageMeta : String?,
            )

        data class UpdateTweet(
            val tweetId : Long,
            val tweetContent : String,
            val tweetImageMeta : String?
        )

        data class DeleteTweet(
            val tweetId : Long,
        )

    }
}