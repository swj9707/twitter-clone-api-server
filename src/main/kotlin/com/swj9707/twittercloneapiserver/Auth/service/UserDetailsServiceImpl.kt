package com.swj9707.twittercloneapiserver.Auth.service

import com.swj9707.twittercloneapiserver.Auth.entity.TwitterUser
import com.swj9707.twittercloneapiserver.Auth.repository.TwitterUserRepository
import com.swj9707.twittercloneapiserver.enum.BaseResponseCode
import com.swj9707.twittercloneapiserver.exception.BaseException
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserDetailsServiceImpl(private val twitterUserRepository: TwitterUserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        var user : TwitterUser = twitterUserRepository.findUserByEmail(username).orElseThrow{BaseException(BaseResponseCode.USER_NOT_FOUND)}
        val now : LocalDateTime = LocalDateTime.now()
        user.lastLogin = now
        twitterUserRepository.save(user)
        return User.builder()
            .username(user.email)
            .password(user.passwd)
            .build()
    }
}