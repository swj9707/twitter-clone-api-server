package com.swj9707.twittercloneapiserver.v1.tweet.repository.projection

import com.swj9707.twittercloneapiserver.constant.entity.repository.projection.ImageProjection
import com.swj9707.twittercloneapiserver.constant.enum.TweetStatus
import java.util.UUID

interface TweetProjection {
    fun getTweetId() : Long
    fun getUserId() : UUID
    fun getTweetContent() : String
    fun getModified() : Boolean
    fun getStatus() : TweetStatus
    fun getImages() : MutableList<ImageProjection>
    fun getUser() : UserProjection
    interface UserProjection {
        fun getUserName() : String
        fun getUserNickName() : String
    }

}