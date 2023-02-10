package com.swj9707.twittercloneapiserver.tweet

import com.swj9707.twittercloneapiserver.constant.enum.TweetStatus
import com.swj9707.twittercloneapiserver.v1.tweet.repository.TweetRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class TweetTest (
    @Autowired
    private val tweetRepository: TweetRepository
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    @Test
    @DisplayName("Tweet Entity 확인")
    fun findAllTweet() {
        val result = tweetRepository.findAllByStatusNotOrderByCreateAtDesc(TweetStatus.DELETED)
        result.forEach{
            tweet -> logger.info(tweet.user.userName)
        }
    }
}