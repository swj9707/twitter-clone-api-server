package com.swj9707.twittercloneapiserver.v1.user.model.repository

import com.swj9707.twittercloneapiserver.v1.user.model.TwitterUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TwitterUserRepository : JpaRepository<TwitterUser, UUID> {
    fun findUserByEmail(userEmail: String): Optional<TwitterUser>
    fun findUserByUserName(userName: String): Optional<TwitterUser>
    fun existsTwitterUserByEmail(userEmail: String): Boolean
    fun existsTwitterUserByUserName(userName: String): Boolean
    fun existsTwitterUserByUserNickname(userNickname: String): Boolean
}