package com.swj9707.twittercloneapiserver.tweet

import com.swj9707.twittercloneapiserver.v1.tweet.entity.repository.LikeRepository
import com.swj9707.twittercloneapiserver.v1.tweet.entity.repository.TweetRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable
import java.util.*

@SpringBootTest
class TweetTest (
    @Autowired
    private val tweetRepository: TweetRepository,
    @Autowired
    private val likeRepository: LikeRepository
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

    @Test
    @DisplayName("리트윗 데이터 가져오기")
    fun getRetweetsByUserID() {
        val result = tweetRepository.findRepliesByUserId(UUID.fromString("2781a1c3-9c53-4572-869d-29b451ae5aec"))
        result.forEach {
            result ->
            logger.info("result : ${result.getTweetId()}")
        }
    }

    @Test
    @DisplayName("좋아요 데이터 가져오기")
    fun getLikesByUserId() {
        val result = likeRepository.getUsersLikedTweets(UUID.fromString("2781a1c3-9c53-4572-869d-29b451ae5aec"),
            Pageable.unpaged())
        result.content.forEach{
            result ->
            logger.info("result : ${result.getTweet().getTweetId()}")
        }
    }
}