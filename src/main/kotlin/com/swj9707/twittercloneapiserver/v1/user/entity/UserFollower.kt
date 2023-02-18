package com.swj9707.twittercloneapiserver.v1.user.entity

import com.swj9707.twittercloneapiserver.constant.entity.BaseImmutableEntity
import jakarta.persistence.*
import java.io.Serializable
import java.util.UUID

@Entity
@Table(name = "user_follower")
@IdClass(UserFollowerPK::class)
class UserFollower (
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    val followee : TwitterUser,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    val follower : TwitterUser

) : BaseImmutableEntity()

@Embeddable
data class UserFollowerPK (
    val followee : UUID = UUID.randomUUID(),
    val follower : UUID = UUID.randomUUID()
) : Serializable