package com.swj9707.twittercloneapiserver.v1.tweet.entity

import com.swj9707.twittercloneapiserver.common.entity.BaseImmutableEntity
import com.swj9707.twittercloneapiserver.v1.user.entity.TwitterUser
import jakarta.persistence.*

@Entity
@Table(name = "LIKES")
class Like(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    val likeId : Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user : TwitterUser,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tweet_id")
    val tweet : Tweet

) : BaseImmutableEntity()