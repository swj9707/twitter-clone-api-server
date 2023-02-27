package com.swj9707.twittercloneapiserver.v1.tweet.entity.repository.projection

import com.swj9707.twittercloneapiserver.common.entity.repository.projection.ImageProjection
import com.swj9707.twittercloneapiserver.common.enum.TweetStatus
import org.springframework.beans.factory.annotation.Value
import java.time.LocalDateTime

interface TweetProjection {
    fun getTweetId(): Long
    fun getTweetContent(): String
    fun getModified(): Boolean
    fun getConnectedTweetId() : Long
    fun getStatus(): TweetStatus
    fun getImages(): MutableList<ImageProjection>
    fun getCreateAt(): LocalDateTime
    fun getUser(): UserProjection

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