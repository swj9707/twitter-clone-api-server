package com.swj9707.twittercloneapiserver.v1.tweet.dto

import org.springframework.data.domain.Slice
import java.util.*

class TweetResDTO {
    companion object Res {
        data class TweetInfo(
            val tweetId : Long?
        )

        data class Tweets(
            val tweets : Slice<TweetDTO>
        )
    }
}