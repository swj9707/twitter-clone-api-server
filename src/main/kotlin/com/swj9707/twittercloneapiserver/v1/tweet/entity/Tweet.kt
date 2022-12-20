package com.swj9707.twittercloneapiserver.v1.tweet.entity

import com.swj9707.twittercloneapiserver.constant.entity.BaseEntity
import com.swj9707.twittercloneapiserver.constant.enum.TweetStatus
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "TWEET")
data class Tweet (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tweet_id")
    val tweetId : Long? = null,

    @Column(name = "user_id")
    val userId : UUID,

    @Column(name = "tweet_content")
    var tweetContent : String,

    @Column(name = "tweet_image_meta")
    var tweetImageMeta : String?,

    @Column(name = "modified")
    var modified : Boolean = false,

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    var status : TweetStatus = TweetStatus.NORMAL
    ) : BaseEntity()