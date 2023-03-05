package com.swj9707.twittercloneapiserver.v1.tweet.model.repository

import com.swj9707.twittercloneapiserver.v1.tweet.model.Like
import com.swj9707.twittercloneapiserver.v1.tweet.model.repository.projection.LikeProjection
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface LikeRepository : JpaRepository<Like, Long> {

    @Query("SELECT like FROM Like like " +
            "LEFT JOIN like.user user " +
            "LEFT JOIN like.tweet tweet " +
            "WHERE like.user.userId = :userId " +
            "AND tweet.status != 'DELETED' " +
            "ORDER BY like.createAt DESC")
    fun getUsersLikedTweets(userId : UUID, pageable : Pageable) : Slice<LikeProjection>
}