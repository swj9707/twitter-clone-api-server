package com.swj9707.twittercloneapiserver.v1.tweet.model.repository

import com.swj9707.twittercloneapiserver.v1.tweet.model.ReTweet
import com.swj9707.twittercloneapiserver.v1.tweet.model.repository.projection.RetweetProjection
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RetweetRepository : JpaRepository<ReTweet, Long> {
    fun findRetweetsByUserUserId(userId : UUID) : List<RetweetProjection>
}