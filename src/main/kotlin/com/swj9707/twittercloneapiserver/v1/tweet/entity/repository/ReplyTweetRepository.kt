package com.swj9707.twittercloneapiserver.v1.tweet.entity.repository

import com.swj9707.twittercloneapiserver.v1.tweet.entity.ReplyTweet
import org.springframework.data.jpa.repository.JpaRepository

interface ReplyTweetRepository : JpaRepository<ReplyTweet, Long> {
}