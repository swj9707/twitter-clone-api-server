package com.swj9707.twittercloneapiserver.v1.tweet.repository.projection

import com.swj9707.twittercloneapiserver.constant.entity.repository.projection.ImageProjection
import com.swj9707.twittercloneapiserver.constant.enum.TweetStatus
import org.springframework.beans.factory.annotation.Value
import java.time.LocalDateTime

interface TweetUserProjection {
    fun getTweetId(): Long
    fun getTweetContent(): String
    fun getModified(): Boolean
    fun getConnectedTweetId() : Long
    fun getStatus(): TweetStatus
    fun getImages(): MutableList<ImageProjection>
    fun getCreateAt(): LocalDateTime
    fun getUser(): UserProjection

    @Value("#{@tweetUtilServiceImpl.isUserRetweetedTweet(getUser().)}")
    fun getIsRetweeted() : Boolean

    @Value("#{@tweetUtilServiceImpl.isUserLikedTweet}")
    fun getIsLiked() : Boolean

    @Value("#{target.likes.size()}")
    fun getLikedTweetsCount(): Int

    @Value("#{target.retweets.size()}")
    fun getRetweetsCount(): Int

    @Value("#{target.replyTweets.size()}")
    fun getRepliesCount(): Int

    interface UserProjection {
        fun getUserName(): String
        fun getUserNickname(): String
        fun getProfileImage(): ImageProjection?
    }
}