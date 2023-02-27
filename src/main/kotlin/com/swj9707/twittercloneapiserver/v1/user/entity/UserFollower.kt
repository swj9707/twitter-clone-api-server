package com.swj9707.twittercloneapiserver.v1.user.entity

import com.swj9707.twittercloneapiserver.common.entity.BaseImmutableEntity
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