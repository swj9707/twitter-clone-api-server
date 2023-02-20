package com.swj9707.twittercloneapiserver.tweet

import com.swj9707.twittercloneapiserver.constant.enum.TweetStatus
import com.swj9707.twittercloneapiserver.v1.tweet.dto.TweetDTO
import com.swj9707.twittercloneapiserver.v1.tweet.repository.RetweetRepository
import com.swj9707.twittercloneapiserver.v1.tweet.repository.TweetRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.transaction.annotation.Transactional
import java.util.*

@SpringBootTest
class TweetTest (
    @Autowired
    private val tweetRepository: TweetRepository,
    @Autowired
    private val retweetRepository: RetweetRepository
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    @Test
    @DisplayName("Tweet Data 확인")
    fun findTweet() {
        val result = tweetRepository.findTweets()
        result.forEach {
            tweet ->
            run {
                val user = tweet.user
                logger.info(user.profileImage?.imageUrl ?: "")
            }
        }
    }

    @Test
    @DisplayName("Find by Id")
    fun findTweetById() {
        val result = tweetRepository.findTweetById(1)
        logger.info(result.get().toString())
    }

    @Test
    @DisplayName("Find Detail info by Id")
    fun findDetailById(){
        val result = tweetRepository.findTweetDetailInfoById(1)
        logger.info(result.get().getLikes().toString()
        )
    }

    @Test
    @DisplayName("유저 트윗 총 갯수 Count Test")
    fun countByUserNameTest() {
        val result = tweetRepository.countByUserUserName("wassup")
        logger.info("result : $result")
    }
}