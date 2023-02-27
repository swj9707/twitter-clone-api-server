package com.swj9707.twittercloneapiserver.v1.tweet.entity.repository.projection

import java.time.LocalDateTime

interface LikeProjection {
    fun getLikeId() : Long
    fun getCreateAt() : LocalDateTime
    fun getTweet() : TweetProjection
}