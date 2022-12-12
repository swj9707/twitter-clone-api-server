package com.swj9707.twittercloneapiserver.Auth.service

import com.swj9707.twittercloneapiserver.Auth.dto.UserReqDTO
import com.swj9707.twittercloneapiserver.Auth.dto.UserResDTO
import com.swj9707.twittercloneapiserver.Auth.entity.TwitterUser
import com.swj9707.twittercloneapiserver.Auth.repository.TwitterUserRepository
import com.swj9707.twittercloneapiserver.constant.enum.BaseResponseCode
import com.swj9707.twittercloneapiserver.exception.BaseException
import com.swj9707.twittercloneapiserver.utils.CookieUtil
import com.swj9707.twittercloneapiserver.utils.JwtUtil
import com.swj9707.twittercloneapiserver.utils.RedisUtil
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.util.ObjectUtils

@Service
class TwitterUserService(private val twitterUserRepository: TwitterUserRepository,
                         private val jwtUtil: JwtUtil,
                         private val cookieUtil: CookieUtil,
                         private val redisUtil : RedisUtil,
                         private val passwordEncoder: PasswordEncoder,
                         private val authenticationManagerBuilder: AuthenticationManagerBuilder) {
    fun findUser(email : String): TwitterUser {
        return twitterUserRepository.findUserByEmail(email)
            .orElseThrow { BaseException(BaseResponseCode.USER_NOT_FOUND) }
    }

    fun existsUser(email : String) : Boolean {
        return twitterUserRepository.existsTwitterUserByEmail(email)
    }

    fun createUser(userRegistReq: UserReqDTO.Req.Register) : UserResDTO.Res.Register {
        val user  = TwitterUser(
            email = userRegistReq.userEmail,
            userName = userRegistReq.userName,
            passwd = passwordEncoder.encode(userRegistReq.password)
        )
        twitterUserRepository.save(user)
        return UserResDTO.Res.Register(user.email, user.userName)
    }

    fun login(req: UserReqDTO.Req.Login) : UserResDTO.Res.Login {
        twitterUserRepository.findUserByEmail(req.userEmail)
            .orElseThrow { BaseException(BaseResponseCode.USER_NOT_FOUND) }

        val authenticationToken = req.toAuthentication()
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)
        val accessToken = jwtUtil.createAccessToken(authentication.name)
        val refreshToken = jwtUtil.createRefreshToken()

        redisUtil.setDataExpire("RT:"+authentication.name, refreshToken, JwtUtil.REFRESH_TOKEN_VALID_TIME)
        return UserResDTO.Res.Login(UserResDTO.Res.TokenInfo(accessToken = accessToken, refreshToken = refreshToken))
    }

    fun reissue(refreshToken : String, accessToken : String) : UserResDTO.Res.TokenInfo {
        if(!jwtUtil.validateToken(refreshToken)){
            throw BaseException(BaseResponseCode.BAD_REQUEST)
        }
        val authentication = jwtUtil.getAuthentication(accessToken)
        val refreshToken = redisUtil.getData("RT:"+authentication.name)
        if(ObjectUtils.isEmpty(refreshToken)){
            throw BaseException(BaseResponseCode.BAD_REQUEST)
        }
        if(refreshToken == null || !refreshToken.equals(refreshToken)){
            throw BaseException(BaseResponseCode.INVALID_TOKEN)
        } else {
            val newAccessToken = jwtUtil.createAccessToken(authentication.name)
            var response = UserResDTO.Res.TokenInfo(accessToken = newAccessToken, refreshToken = "")

            if(jwtUtil.getExpirationPeriod(refreshToken) <= 7){
                val newRefreshToken = jwtUtil.createRefreshToken()
                redisUtil.setDataExpire("RT:"+authentication.name, newRefreshToken, JwtUtil.REFRESH_TOKEN_VALID_TIME)
                response.refreshToken = newRefreshToken
            }
            return response
        }
    }

    fun logout(req: UserReqDTO.Req.Logout) : UserResDTO.Res.Logout {
        if(!jwtUtil.validateToken(req.accessToken)){
            throw BaseException(BaseResponseCode.BAD_REQUEST)
        }

        val authentication = jwtUtil.getAuthentication(req.accessToken)

        if(redisUtil.getData("RT:"+authentication.name) != null){
            redisUtil.deleteData("RT:"+authentication.name)
        }

        val expiration = jwtUtil.getExpiration(req.accessToken)
        redisUtil.setDataExpire(req.accessToken, "logout", expiration)
        return UserResDTO.Res.Logout(authentication.name)
    }
}