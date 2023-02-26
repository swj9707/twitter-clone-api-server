package com.swj9707.twittercloneapiserver.v1.tweet.entity.repository

import com.swj9707.twittercloneapiserver.v1.tweet.entity.Tweet
import com.swj9707.twittercloneapiserver.v1.tweet.entity.repository.projection.TweetDetailProjection
import com.swj9707.twittercloneapiserver.v1.tweet.entity.repository.projection.TweetProjection
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface TweetRepository : JpaRepository<Tweet, Long> {
    @Query("SELECT tweet from Tweet tweet JOIN FETCH tweet.user WHERE tweet.status != 'DELETED' ORDER BY tweet.createAt DESC")
    fun findTweetsByStatusNot(pageable : Pageable) : Slice<TweetProjection>
    @Query("SELECT tweet FROM Tweet tweet JOIN FETCH tweet.user WHERE tweet.status != 'DELETED' ")
    fun findTweets() : List<Tweet>
    @Query("SELECT tweet from Tweet tweet WHERE tweet.tweetId = :tweetId")
    fun findTweetById(tweetId : Long) : Optional<TweetProjection>
    @Query("SELECT tweet from Tweet tweet WHERE tweet.tweetId = :tweetId")
    fun findTweetDetailInfoById(tweetId : Long) : Optional<TweetDetailProjection>
    fun findTweetsByUserUserName(userName : String, pageable : Pageable) : Slice<TweetProjection>
    fun findTweetsByConnectedTweetId(connectedTweetId : Long, pageable: Pageable) : Slice<TweetProjection>
    @Query("SELECT t FROM Tweet t JOIN ReTweet rt ON t.tweetId = rt.tweet.tweetId WHERE rt.user.userId = :userId AND t.status != 'DELETED' ORDER BY t.createAt DESC")
    fun findRetweetsByUserId(userId : UUID) : List<TweetProjection>
    fun findTweetsByUserUserId(userId : UUID) : List<TweetProjection>
    @Query("SELECT t From Tweet t WHERE t.user.userId = :userId AND t.connectedTweetId IS NOT NULL AND t.status != 'DELETED' ORDER BY t.createAt DESC ")
    fun findRepliesByUserId(userId : UUID) : List<TweetProjection>
    fun countByUserUserName(userName : String) : Int

}