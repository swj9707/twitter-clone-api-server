package com.swj9707.twittercloneapiserver.v1.user.service

import com.swj9707.twittercloneapiserver.v1.user.model.repository.TwitterUserRepository
import com.swj9707.twittercloneapiserver.global.common.enum.ResCode
import com.swj9707.twittercloneapiserver.global.exception.CustomException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

@Service
class UserDetailsServiceImpl(private val twitterUserRepository: TwitterUserRepository) : UserDetailsService {
    @Throws(CustomException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        return twitterUserRepository.findUserByEmail(username)
            .orElseThrow { CustomException(ResCode.USER_NOT_FOUND) }
    }
}