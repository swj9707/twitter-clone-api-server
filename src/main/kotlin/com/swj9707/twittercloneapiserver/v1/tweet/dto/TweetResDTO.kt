package com.swj9707.twittercloneapiserver.v1.tweet.dto

import java.util.*

class TweetResDTO {
    companion object Res {
        data class TweetInfo(
            val tweetId : Long?
        )
    }
}