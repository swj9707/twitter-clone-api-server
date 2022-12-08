package com.swj9707.twittercloneapiserver.Auth.repository

import com.swj9707.twittercloneapiserver.Auth.entity.TwitterUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TwitterUserRepository : JpaRepository<TwitterUser, UUID>{
    fun findUserByEmail(Email : String) : Optional<TwitterUser>
    fun existsTwitterUserByEmail(Email : String) : Boolean
}