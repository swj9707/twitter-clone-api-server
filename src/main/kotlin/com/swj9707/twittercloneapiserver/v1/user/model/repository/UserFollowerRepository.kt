package com.swj9707.twittercloneapiserver.v1.user.model.repository

import com.swj9707.twittercloneapiserver.v1.user.model.UserFollower
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserFollowerRepository : JpaRepository<UserFollower, Long> {
    fun existsByFolloweeUserIdAndFollowerUserId(followeeId : UUID, followerId : UUID) : Boolean
    fun deleteByFolloweeUserIdAndFollowerUserId(followeeId : UUID, followerId : UUID) : Boolean
}