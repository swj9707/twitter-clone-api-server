package com.swj9707.twittercloneapiserver.v1.tweet.model.repository

import com.swj9707.twittercloneapiserver.v1.tweet.model.ReplyTweet
import org.springframework.data.jpa.repository.JpaRepository

interface ReplyTweetRepository : JpaRepository<ReplyTweet, Long> {
}