package com.swj9707.twittercloneapiserver.v1.tweet.dto

class TweetResDTO {
    companion object Res {
        data class TweetInfo(
            val tweetId: Long?
        )

        data class RetweetResult(
            val result: Boolean
        )

        data class TweetsRes(
            val tweets: MutableList<TweetDTO.Dto.TweetInfo>,
            val size: Int,
            val number: Int,
            val first: Boolean,
            val last: Boolean,
            val numberOfElements: Int,
            val empty: Boolean
        )

        data class UserTweetsRes(
            val tweets: List<TweetDTO.Dto.UsersTweetInfo>,
            val size: Int,
            val number: Int,
            val first: Boolean,
            val last: Boolean,
            val numberOfElements: Int,
            val empty: Boolean
        )
    }
}