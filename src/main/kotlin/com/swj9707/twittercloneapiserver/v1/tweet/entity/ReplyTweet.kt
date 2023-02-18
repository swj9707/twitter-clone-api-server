package com.swj9707.twittercloneapiserver.v1.tweet.entity

import com.swj9707.twittercloneapiserver.constant.entity.BaseNonMutableEntity
import jakarta.persistence.*

@Entity
@Table(name = "tweet_reply")
class ReplyTweet (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "tweet_id")
    val tweet: Tweet,

    @ManyToOne
    @JoinColumn(name = "connected_tweet_id")
    val connectedTweet: Tweet

) : BaseNonMutableEntity()