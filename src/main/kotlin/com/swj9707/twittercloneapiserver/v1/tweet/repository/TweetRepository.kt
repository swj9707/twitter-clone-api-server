package com.swj9707.twittercloneapiserver.v1.tweet.repository

import com.swj9707.twittercloneapiserver.constant.enum.TweetStatus
import com.swj9707.twittercloneapiserver.v1.tweet.entity.Tweet
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository

interface TweetRepository : JpaRepository<Tweet, Long> {
    fun findTweetsByStatusNot(status : TweetStatus, pageable : Pageable) : Slice<Tweet>
}