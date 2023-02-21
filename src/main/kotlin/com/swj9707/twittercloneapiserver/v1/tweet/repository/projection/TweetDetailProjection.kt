package com.swj9707.twittercloneapiserver.v1.tweet.repository.projection

import com.swj9707.twittercloneapiserver.constant.entity.repository.projection.ImageProjection
import com.swj9707.twittercloneapiserver.constant.enum.TweetStatus
import java.time.LocalDateTime

interface TweetDetailProjection {
    fun getTweetId(): Long
    fun getTweetContent(): String
    fun getModified(): Boolean
    fun getStatus(): TweetStatus
    fun getImages(): MutableList<ImageProjection>
    fun getCreateAt(): LocalDateTime
    fun getUser(): UserProjection
    fun getLikes(): MutableList<LikeProjection>
    fun getRetweets(): MutableList<RetweetProjection>
    fun getReplyTweets(): MutableList<TweetProjection>

    interface UserProjection {
        fun getUserName(): String
        fun getUserNickname(): String
        fun getProfileImage(): ImageProjection?
    }

    interface LikeProjection {
        fun getLikeId(): Long
        fun getUser(): UserProjection
        fun getTweet(): TweetProjection
    }

    interface RetweetProjection {
        fun getRetweetId(): Long
        fun getUser(): UserProjection
        fun getTweet(): TweetProjection

    }

}