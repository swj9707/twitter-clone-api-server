package com.swj9707.twittercloneapiserver.v1.tweet.model

import com.swj9707.twittercloneapiserver.global.common.model.BaseImmutableEntity
import com.swj9707.twittercloneapiserver.v1.user.model.TwitterUser
import jakarta.persistence.*

@Entity
@Table(name = "RETWEET")
class ReTweet (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "retweet_id")
    val retweetId: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: TwitterUser,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="tweet_id", nullable = false)
    val tweet: Tweet

    ) : BaseImmutableEntity()