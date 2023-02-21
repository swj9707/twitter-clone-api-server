package com.swj9707.twittercloneapiserver.v1.user.service

import com.swj9707.twittercloneapiserver.v1.user.repository.TwitterUserRepository
import com.swj9707.twittercloneapiserver.constant.enum.BaseResponseCode
import com.swj9707.twittercloneapiserver.exception.BaseException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

@Service
class UserDetailsServiceImpl(private val twitterUserRepository: TwitterUserRepository) : UserDetailsService {
    @Throws(BaseException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        return twitterUserRepository.findUserByEmail(username)
            .orElseThrow { BaseException(BaseResponseCode.USER_NOT_FOUND) }
    }
}