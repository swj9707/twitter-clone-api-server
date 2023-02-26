package com.swj9707.twittercloneapiserver.v1.tweet.entity.repository.projection

import java.time.LocalDateTime

interface RetweetProjection {
    fun getRetweetId() : Long
    fun getCreateAt() : LocalDateTime
    fun getTweet() : TweetProjection

}