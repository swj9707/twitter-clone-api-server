package com.swj9707.twittercloneapiserver.v1.tweet.repository

import com.swj9707.twittercloneapiserver.v1.tweet.entity.ReTweet
import com.swj9707.twittercloneapiserver.v1.tweet.repository.projection.RetweetProjection
import org.springframework.data.jpa.repository.JpaRepository

interface RetweetRepository : JpaRepository<ReTweet, Long> {
    fun findRetweetsByUserUserName(userName : String) : List<RetweetProjection>
}