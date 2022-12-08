package com.swj9707.twittercloneapiserver.Auth.service

import com.swj9707.twittercloneapiserver.Auth.dto.register.TwitterUserRegistReq
import com.swj9707.twittercloneapiserver.Auth.dto.register.TwitterUserRegistRes
import com.swj9707.twittercloneapiserver.Auth.dto.signin.TwitterUserLoginReq
import com.swj9707.twittercloneapiserver.Auth.dto.signin.TwitterUserLoginRes
import com.swj9707.twittercloneapiserver.Auth.entity.TwitterUser
import com.swj9707.twittercloneapiserver.Auth.repository.TwitterUserRepository
import com.swj9707.twittercloneapiserver.enum.BaseResponseCode
import com.swj9707.twittercloneapiserver.exception.BaseException
import com.swj9707.twittercloneapiserver.utils.JwtTokenProvider
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class TwitterUserService(private val twitterUserRepository: TwitterUserRepository,
                         private val jwtTokenProvider: JwtTokenProvider,
                         private val passwordEncoder: PasswordEncoder) {
    fun findUser(email : String): TwitterUser {
        return twitterUserRepository.findUserByEmail(email)
            .orElseThrow { BaseException(BaseResponseCode.USER_NOT_FOUND) }
    }

    fun existsUser(email : String) : Boolean {
        return twitterUserRepository.existsTwitterUserByEmail(email)
    }

    fun createUser(userRegistReq: TwitterUserRegistReq) : TwitterUserRegistRes {
        val user  = TwitterUser(
            email = userRegistReq.userEmail,
            userName = userRegistReq.userName,
            passwd = passwordEncoder.encode(userRegistReq.password)
        )
        twitterUserRepository.save(user)
        return TwitterUserRegistRes(user.email, user.userName)
    }

    fun login(userLoginReq: TwitterUserLoginReq) : TwitterUserLoginRes {
        val user : TwitterUser = twitterUserRepository.findUserByEmail(userLoginReq.userEmail)
            .orElseThrow { BaseException(BaseResponseCode.USER_NOT_FOUND) }
        if(!passwordEncoder.matches(userLoginReq.password, user.passwd)){
            throw BaseException(BaseResponseCode.INVALID_PASSWORD)
        }
        val token : String = jwtTokenProvider.createToken(userLoginReq.userEmail)
        return TwitterUserLoginRes(HttpStatus.OK, token)
    }
}