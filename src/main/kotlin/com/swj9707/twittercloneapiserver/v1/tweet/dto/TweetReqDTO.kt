package com.swj9707.twittercloneapiserver.v1.tweet.dto

class TweetReqDTO {

    companion object Req {

        data class CreateTweet(
            val tweetContent : String,
            val tweetImageMeta : TweetImageMeta?,
            )

        data class UpdateTweet(
            val tweetId : Long,
            val tweetContent : String,
            val tweetImageMeta : TweetImageMeta?,
        )

        data class DeleteTweet(
            val tweetId : Long,
        )

        data class TweetImageMeta (
            val type : String,
            val name : String,
            val size : Int
        )

    }
}