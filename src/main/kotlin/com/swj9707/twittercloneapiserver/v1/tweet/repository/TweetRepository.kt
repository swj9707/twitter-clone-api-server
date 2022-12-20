package com.swj9707.twittercloneapiserver.v1.tweet.repository

import com.swj9707.twittercloneapiserver.v1.tweet.entity.Tweet
import org.springframework.data.jpa.repository.JpaRepository

interface TweetRepository : JpaRepository<Tweet, Long> {
}