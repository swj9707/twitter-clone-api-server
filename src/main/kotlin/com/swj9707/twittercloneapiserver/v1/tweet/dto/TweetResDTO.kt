package com.swj9707.twittercloneapiserver.v1.tweet.dto
class TweetResDTO {
    companion object Res {
        data class TweetInfo(
            val tweetId : Long?
        )
        data class Tweets(
            val tweets : List<TweetDTO.Dto.TweetInfo>,
            val size : Int,
            val number : Int,
            val first : Boolean,
            val last : Boolean,
            val numberOfElements : Int,
            val empty : Boolean

        )
    }
}