package com.swj9707.twittercloneapiserver.v1.auth.service

import com.swj9707.twittercloneapiserver.v1.auth.dto.TwitterUserDTO
import com.swj9707.twittercloneapiserver.v1.auth.dto.UserReqDTO
import com.swj9707.twittercloneapiserver.v1.auth.dto.UserResDTO
import com.swj9707.twittercloneapiserver.v1.auth.entity.TwitterUser
import com.swj9707.twittercloneapiserver.v1.auth.repository.TwitterUserRepository
import com.swj9707.twittercloneapiserver.v1.auth.service.inter.TwitterUserService
import com.swj9707.twittercloneapiserver.constant.enum.BaseResponseCode
import com.swj9707.twittercloneapiserver.exception.BaseException
import com.swj9707.twittercloneapiserver.utils.JwtUtil
import com.swj9707.twittercloneapiserver.utils.RedisUtil
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.util.ObjectUtils

@Service
class TwitterUserServiceImpl(private val twitterUserRepository: TwitterUserRepository,
                             private val jwtUtil: JwtUtil,
                             private val redisUtil : RedisUtil,
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
        val accessToken = jwtUtil.createAccessToken(authentication.name)
        val refreshToken = jwtUtil.createRefreshToken()
        val userInfoDTO = TwitterUserDTO.entityToDTO(userInfo)
        redisUtil.setDataExpire("RT:"+authentication.name, refreshToken, JwtUtil.REFRESH_TOKEN_VALID_TIME)
        return UserResDTO.Res.Login(
            userInfo = userInfoDTO,
            tokenInfo = UserResDTO.Res.TokenInfo(accessToken = accessToken, refreshToken = refreshToken))
    }

    override fun reissue(refreshToken : String, accessToken : String) : UserResDTO.Res.TokenInfo {
        if(!jwtUtil.validateToken(refreshToken)){
            throw BaseException(BaseResponseCode.BAD_REQUEST)
        }
        val authentication = jwtUtil.getAuthentication(accessToken)
        val savedRefreshToken = redisUtil.getData("RT:"+authentication.name)
        if(ObjectUtils.isEmpty(refreshToken)){
            throw BaseException(BaseResponseCode.BAD_REQUEST)
        }
        if(savedRefreshToken == null || refreshToken != savedRefreshToken){
            throw BaseException(BaseResponseCode.INVALID_TOKEN)
        } else {
            val newAccessToken = jwtUtil.createAccessToken(authentication.name)
            val response = UserResDTO.Res.TokenInfo(accessToken = newAccessToken, refreshToken = "")

            if(jwtUtil.getExpirationPeriod(refreshToken) <= 7){
                val newRefreshToken = jwtUtil.createRefreshToken()
                redisUtil.setDataExpire("RT:"+authentication.name, newRefreshToken, JwtUtil.REFRESH_TOKEN_VALID_TIME)
                response.refreshToken = newRefreshToken
            }
            return response
        }
    }
    override fun logout(accessToken: String) : UserResDTO.Res.Logout {
        if(!jwtUtil.validateToken(accessToken)){
            throw BaseException(BaseResponseCode.BAD_REQUEST)
        }

        val authentication = jwtUtil.getAuthentication(accessToken)

        if(redisUtil.getData("RT:"+authentication.name) != null){
            redisUtil.deleteData("RT:"+authentication.name)
        }

        val expiration = jwtUtil.getExpiration(accessToken)
        redisUtil.setDataExpire(accessToken, "logout", expiration)
        return UserResDTO.Res.Logout(authentication.name)
    }
}