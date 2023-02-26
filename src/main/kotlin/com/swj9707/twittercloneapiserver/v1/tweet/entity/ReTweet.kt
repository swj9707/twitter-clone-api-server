package com.swj9707.twittercloneapiserver.v1.tweet.entity

import com.swj9707.twittercloneapiserver.common.entity.BaseImmutableEntity
import com.swj9707.twittercloneapiserver.v1.user.entity.TwitterUser
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