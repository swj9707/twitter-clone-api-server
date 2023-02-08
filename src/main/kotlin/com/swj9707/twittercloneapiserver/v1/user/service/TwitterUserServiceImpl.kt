package com.swj9707.twittercloneapiserver.v1.user.service

import com.swj9707.twittercloneapiserver.constant.entity.Image
import com.swj9707.twittercloneapiserver.v1.user.dto.UserReqDTO
import com.swj9707.twittercloneapiserver.v1.user.dto.UserResDTO
import com.swj9707.twittercloneapiserver.v1.user.entity.TwitterUser
import com.swj9707.twittercloneapiserver.v1.user.repository.TwitterUserRepository
import com.swj9707.twittercloneapiserver.v1.user.service.inter.TwitterUserService
import com.swj9707.twittercloneapiserver.constant.enum.BaseResponseCode
import com.swj9707.twittercloneapiserver.exception.BaseException
import com.swj9707.twittercloneapiserver.utils.JwtUtils
import com.swj9707.twittercloneapiserver.utils.RedisUtils
import com.swj9707.twittercloneapiserver.v1.tweet.repository.TweetRepository
import com.swj9707.twittercloneapiserver.v1.user.dto.UserDTO
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.util.ObjectUtils
import java.util.*

@Service
class TwitterUserServiceImpl(private val twitterUserRepository: TwitterUserRepository,
                             private val tweetRepository: TweetRepository,
                             private val jwtUtils: JwtUtils,
                             private val redisUtils : RedisUtils,
                             private val passwordEncoder: PasswordEncoder,
                             private val authenticationManagerBuilder: AuthenticationManagerBuilder) :
    TwitterUserService {
    override fun createUser(userRegistReq: UserReqDTO.Req.Register) : UserResDTO.Res.Register {
        val user  = TwitterUser(
            email = userRegistReq.userEmail,
            userName = userRegistReq.userName,
            userNickname = userRegistReq.userNickname,
            passwd = passwordEncoder.encode(userRegistReq.password)
        )

        if(twitterUserRepository.existsTwitterUserByEmail(user.email)){
            throw BaseException(BaseResponseCode.DUPLICATE_EMAIL)
        }

        if(twitterUserRepository.existsTwitterUserByUserName(user.userNickname)){
            throw BaseException(BaseResponseCode.DUPLICATE_USERNAME)
        }

        twitterUserRepository.save(user)
        return UserResDTO.Res.Register(user.email, user.userNickname)
    }

    override fun editUserProfile(editProfileReq : UserReqDTO.Req.EditProfile) : UserResDTO.Res.EditProfile {
        val user = twitterUserRepository.findById(editProfileReq.userId)
            .orElseThrow{ BaseException(BaseResponseCode.USER_NOT_FOUND)}
        user.userNickname = editProfileReq.newUserNickname
        if(twitterUserRepository.existsTwitterUserByUserName(user.userName)){
            throw BaseException(BaseResponseCode.DUPLICATE_USERNAME)
        } else {
            twitterUserRepository.save(user)
            return UserResDTO.Res.EditProfile(userInfo =
            UserDTO.Dto.TwitterUserProfile.entityToDTO(user))
        }
    }

    override fun editUserProfile(editUserProfile: UserReqDTO.Req.EditUserProfile): UserResDTO.Res.EditProfile {
        val user = twitterUserRepository.findById(editUserProfile.userId)
            .orElseThrow{ BaseException(BaseResponseCode.USER_NOT_FOUND)}
        if(twitterUserRepository.existsTwitterUserByUserNickname(editUserProfile.newUserNickname)){
            throw BaseException(BaseResponseCode.DUPLICATE_USERNICKNAME)
        } else {
            user.userNickname = editUserProfile.newUserNickname
            user.profileImage = Image.dtoToEntity(editUserProfile.profileImage)
            user.backgroundImage = Image.dtoToEntity(editUserProfile.backgroundImage)
            twitterUserRepository.save(user)
            return UserResDTO.Res.EditProfile(userInfo = UserDTO.Dto.TwitterUserProfile.entityToDTO(user))
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
        var refreshToken = redisUtils.getData("RT"+authentication.name)
        val userInfoDTO = UserDTO.Dto.TwitterUserAuthInfo.entityToDTO(userInfo)

        if(refreshToken == null){
            refreshToken = jwtUtils.createToken(authentication.name, JwtUtils.REFRESH_TOKEN_VALID_TIME)
            redisUtils.setDataExpire("RT:"+authentication.name, refreshToken, JwtUtils.REFRESH_TOKEN_VALID_TIME)
        }

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
        if(ObjectUtils.isEmpty(refreshToken) || savedRefreshToken == null || refreshToken != savedRefreshToken){
            throw BaseException(BaseResponseCode.INVALID_TOKEN)
        } else {
            val newAccessToken = jwtUtils.createToken(userEmail, JwtUtils.ACCESS_TOKEN_VALID_TIME)
            return UserResDTO.Res.TokenInfo(accessToken = newAccessToken, refreshToken = "")
            //리프레쉬 토큰 만료 기간이 7일 이내라면 다시 재발급해주는 코드.
            //여러개의 브라우저 상에서 로그인했을 경우 문제가 생길 수 있으므로 일단 주석처리
//            if(jwtUtils.getExpirationPeriod(refreshToken) <= 7){
//                val newRefreshToken = jwtUtils.createToken(userEmail, JwtUtils.REFRESH_TOKEN_VALID_TIME)
//                redisUtils.deleteData("RT:$userEmail")
//                redisUtils.setDataExpire("RT:$userEmail", newRefreshToken, JwtUtils.REFRESH_TOKEN_VALID_TIME)
//                response.refreshToken = newRefreshToken
//            }
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

    override fun getUserInfoByUserId(userId: String): UserResDTO.Res.UserInfo {
        val result = twitterUserRepository.findById(UUID.fromString(userId))
            .orElseThrow{ BaseException(BaseResponseCode.USER_NOT_FOUND) }
        return UserResDTO.Res.UserInfo(UserDTO.Dto.TwitterUserInfo.entityToDTO(result))
    }

    override fun getUserProfileByUserName(userName: String): UserResDTO.Res.UserProfile {
        val result = twitterUserRepository.findUserByUserName(userName)
            .orElseThrow { BaseException(BaseResponseCode.USER_NOT_FOUND) }
        val countOfTweets = tweetRepository.countByUserId(result.userId)
        val userProfile = UserDTO.Dto.TwitterUserProfile.entityToDTO(result)

        return UserResDTO.Res.UserProfile(userProfile = userProfile, countOfTweet = countOfTweets)
    }
}