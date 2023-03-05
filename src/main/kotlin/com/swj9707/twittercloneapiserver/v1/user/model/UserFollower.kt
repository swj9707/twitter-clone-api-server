package com.swj9707.twittercloneapiserver.v1.user.model

import com.swj9707.twittercloneapiserver.global.common.model.BaseImmutableEntity
import jakarta.persistence.*

@Entity
@Table(name = "user_follower")
class UserFollower (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    val id : Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    val followee : TwitterUser,

    @ManyToOne(fetch = FetchType.LAZY)
    val follower : TwitterUser

) : BaseImmutableEntity()