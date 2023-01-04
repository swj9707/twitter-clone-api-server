package com.swj9707.twittercloneapiserver.v1.auth.service

import com.swj9707.twittercloneapiserver.v1.auth.dto.TwitterUserDTO
import com.swj9707.twittercloneapiserver.v1.auth.dto.UserReqDTO
import com.swj9707.twittercloneapiserver.v1.auth.dto.UserResDTO
import com.swj9707.twittercloneapiserver.v1.auth.entity.TwitterUser
import com.swj9707.twittercloneapiserver.v1.auth.repository.TwitterUserRepository
import com.swj9707.twittercloneapiserver.v1.auth.service.inter.TwitterUserService
import com.swj9707.twittercloneapiserver.constant.enum.BaseResponseCode
import com.swj9707.twittercloneapiserver.exception.BaseException
import com.swj9707.twittercloneapiserver.utils.JwtUtils
import com.swj9707.twittercloneapiserver.utils.RedisUtils
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.util.ObjectUtils
import java.util.*

@Service
class TwitterUserServiceImpl(private val twitterUserRepository: TwitterUserRepository,
                             private val jwtUtils: JwtUtils,
                             private val redisUtils : RedisUtils,
                             private val passwordEncoder: PasswordEncoder,
                             private val authenticationManagerBuilder: AuthenticationManagerBuilder) :
    TwitterUserService {
    override fun createUser(userRegistReq: UserReqDTO.Req.Register) : UserResDTO.Res.Register {
        val user  = TwitterUser(
            email = userRegistReq.userEmail,
            userName = userRegistReq.userName,
            passwd = passwordEncoder.encode(userRegistReq.password)
        )

        if(twitterUserRepository.existsTwitterUserByEmail(user.email)){
            throw BaseException(BaseResponseCode.DUPLICATE_EMAIL)
        }

        if(twitterUserRepository.existsTwitterUserByUserName(user.userName)){
            throw BaseException(BaseResponseCode.DUPLICATE_USERNAME)
        }

        twitterUserRepository.save(user)
        return UserResDTO.Res.Register(user.email, user.userName)
    }

    override fun editUserProfile(editProfileReq : UserReqDTO.Req.EditProfile) : UserResDTO.Res.EditProfile {
        val user = twitterUserRepository.findById(editProfileReq.userId)
            .orElseThrow{ BaseException(BaseResponseCode.USER_NOT_FOUND)}
        user.userName = editProfileReq.newUserName
        if(twitterUserRepository.existsTwitterUserByUserName(user.userName)){
            throw BaseException(BaseResponseCode.DUPLICATE_USERNAME)
        } else {
            twitterUserRepository.save(user)
            return UserResDTO.Res.EditProfile(userInfo = TwitterUserDTO.entityToDTO(user))
        }
    }

    override fun editUserPassword(editUserPasswordReq: UserReqDTO.Req.EditPassword): UserResDTO.Res.EditPassword {

        val userInfo = twitterUserRepository.findById(editUserPasswordReq.userId)
            .orElseThrow{ BaseException(BaseResponseCode.USER_NOT_FOUND)}
        if(!passwordEncoder.matches(editUserPasswordReq.currentPassword, userInfo.password)){
            throw BaseException(BaseResponseCode.INVALID_PASSWORD)
        } else {
            userInfo.passwd = passwordEncoder.encode(editUserPasswordReq.newPassword)
            twitterUserRepository.save(userInfo)
            return UserResDTO.Res.EditPassword(userInfo.userId)
        }
    }

    override fun login(req: UserReqDTO.Req.Login) : UserResDTO.Res.Login {
        val userInfo = twitterUserRepository.findUserByEmail(req.userEmail)
            .orElseThrow { BaseException(BaseResponseCode.USER_NOT_FOUND) }

        val authenticationToken = req.toAuthentication()
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)
        val accessToken = jwtUtils.createToken(authentication.name, JwtUtils.ACCESS_TOKEN_VALID_TIME)
        val refreshToken = jwtUtils.createToken(authentication.name, JwtUtils.REFRESH_TOKEN_VALID_TIME)
        val userInfoDTO = TwitterUserDTO.entityToDTO(userInfo)
        redisUtils.setDataExpire("RT:"+authentication.name, refreshToken, JwtUtils.REFRESH_TOKEN_VALID_TIME)
        return UserResDTO.Res.Login(
            userInfo = userInfoDTO,
            tokenInfo = UserResDTO.Res.TokenInfo(accessToken = accessToken, refreshToken = refreshToken))
    }

    override fun reissue(refreshToken : String) : UserResDTO.Res.TokenInfo {
        if(!jwtUtils.validateToken(refreshToken)){
            throw BaseException(BaseResponseCode.REFRESH_TOKEN_EXPIRED)
        }
        val userEmail = jwtUtils.getUserEmail(refreshToken)
        val savedRefreshToken = redisUtils.getData("RT:$userEmail")
        if(ObjectUtils.isEmpty(refreshToken)){
            throw BaseException(BaseResponseCode.BAD_REQUEST)
        }
        if(savedRefreshToken == null || refreshToken != savedRefreshToken){
            throw BaseException(BaseResponseCode.INVALID_TOKEN)
        } else {
            val newAccessToken = jwtUtils.createToken(userEmail, JwtUtils.ACCESS_TOKEN_VALID_TIME)
            val response = UserResDTO.Res.TokenInfo(accessToken = newAccessToken, refreshToken = "")
            //얘를 뺄까 말까
            if(jwtUtils.getExpirationPeriod(refreshToken) <= 7){
                val newRefreshToken = jwtUtils.createToken(userEmail, JwtUtils.REFRESH_TOKEN_VALID_TIME)
                redisUtils.deleteData("RT:$userEmail")
                redisUtils.setDataExpire("RT:$userEmail", newRefreshToken, JwtUtils.REFRESH_TOKEN_VALID_TIME)
                response.refreshToken = newRefreshToken
            }
            return response
        }
    }

    override fun logout(accessToken: String) : UserResDTO.Res.Logout {
        if(!jwtUtils.validateToken(accessToken)){
            throw BaseException(BaseResponseCode.BAD_REQUEST)
        }

        val authentication = jwtUtils.getAuthentication(accessToken)

        if(redisUtils.getData("RT:"+authentication.name) != null){
            redisUtils.deleteData("RT:"+authentication.name)
        }

        val expiration = jwtUtils.getExpiration(accessToken)
        redisUtils.setDataExpire(accessToken, "logout", expiration)
        return UserResDTO.Res.Logout(authentication.name)
    }
}