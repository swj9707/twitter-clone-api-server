package com.swj9707.twittercloneapiserver.v1.user.service

import com.swj9707.twittercloneapiserver.global.common.enum.JwtTokenStatus
import com.swj9707.twittercloneapiserver.global.common.model.Image
import com.swj9707.twittercloneapiserver.v1.user.dto.vo.UserReqDTO
import com.swj9707.twittercloneapiserver.v1.user.dto.vo.UserResDTO
import com.swj9707.twittercloneapiserver.v1.user.model.TwitterUser
import com.swj9707.twittercloneapiserver.v1.user.model.repository.TwitterUserRepository
import com.swj9707.twittercloneapiserver.v1.user.service.inter.TwitterUserService
import com.swj9707.twittercloneapiserver.global.common.enum.ResCode
import com.swj9707.twittercloneapiserver.global.exception.CustomException
import com.swj9707.twittercloneapiserver.global.utils.JwtUtils
import com.swj9707.twittercloneapiserver.global.utils.RedisUtils
import com.swj9707.twittercloneapiserver.v1.tweet.model.repository.TweetRepository
import com.swj9707.twittercloneapiserver.v1.user.dto.UserDTO
import com.swj9707.twittercloneapiserver.v1.user.model.UserFollower
import com.swj9707.twittercloneapiserver.v1.user.model.repository.UserFollowerRepository
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.util.ObjectUtils
import java.time.LocalDateTime
import java.util.*

@Service
class TwitterUserServiceImpl(
    private val twitterUserRepository: TwitterUserRepository,
    private val tweetRepository: TweetRepository,
    private val userFollowerRepository: UserFollowerRepository,
    private val jwtUtils: JwtUtils,
    private val redisUtils: RedisUtils,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder
) : TwitterUserService {
    override fun createUser(userRegistReq: UserReqDTO.Req.Register): UserResDTO.Res.Register {
        val user = TwitterUser(
            email = userRegistReq.userEmail,
            userName = userRegistReq.userName,
            userNickname = userRegistReq.userNickname,
            passwd = passwordEncoder.encode(userRegistReq.password)
        )

        if (twitterUserRepository.existsTwitterUserByEmail(user.email)) {
            throw CustomException(ResCode.DUPLICATE_EMAIL)
        }

        if (twitterUserRepository.existsTwitterUserByUserName(user.userName)) {
            throw CustomException(ResCode.DUPLICATE_USERNAME)
        }

        if (twitterUserRepository.existsTwitterUserByUserName(user.userNickname)) {
            throw CustomException(ResCode.DUPLICATE_USERNAME)
        }

        twitterUserRepository.save(user)
        return UserResDTO.Res.Register(user.email, user.userNickname)
    }

    override fun editUserProfile(editProfileReq: UserReqDTO.Req.EditProfile): UserResDTO.Res.EditProfile {
        val user = twitterUserRepository.findById(editProfileReq.userId)
            .orElseThrow { CustomException(ResCode.USER_NOT_FOUND) }
        user.userNickname = editProfileReq.newUserNickname
        if (twitterUserRepository.existsTwitterUserByUserName(user.userName)) {
            throw CustomException(ResCode.DUPLICATE_USERNAME)
        } else {
            twitterUserRepository.save(user)
            return UserResDTO.Res.EditProfile(
                userInfo = UserDTO.Dto.TwitterUserProfile.fromEntity(user)
            )
        }
    }

    override fun editUserProfile(editUserProfile: UserReqDTO.Req.EditUserProfile): UserResDTO.Res.EditProfile {
        val user = twitterUserRepository.findById(editUserProfile.userId)
            .orElseThrow { CustomException(ResCode.USER_NOT_FOUND) }
        if (user.userNickname != editUserProfile.newUserNickname && twitterUserRepository.existsTwitterUserByUserNickname(
                editUserProfile.newUserNickname
            )
        ) {
            throw CustomException(ResCode.DUPLICATE_USER_NICKNAME)
        } else {
            user.userNickname = editUserProfile.newUserNickname
            user.profileImage = Image.dtoToEntity(editUserProfile.profileImage)
            user.backgroundImage = Image.dtoToEntity(editUserProfile.backgroundImage)
            twitterUserRepository.save(user)
            return UserResDTO.Res.EditProfile(
                userInfo = UserDTO.Dto.TwitterUserProfile.fromEntity(
                    user
                )
            )
        }
    }

    override fun editUserPassword(editUserPasswordReq: UserReqDTO.Req.EditPassword): UserResDTO.Res.EditPassword {

        val userInfo = twitterUserRepository.findById(editUserPasswordReq.userId)
            .orElseThrow { CustomException(ResCode.USER_NOT_FOUND) }
        if (!passwordEncoder.matches(editUserPasswordReq.currentPassword, userInfo.password)) {
            throw CustomException(ResCode.INVALID_PASSWORD)
        } else {
            userInfo.passwd = passwordEncoder.encode(editUserPasswordReq.newPassword)
            twitterUserRepository.save(userInfo)
            return UserResDTO.Res.EditPassword(userInfo.userId)
        }
    }

    override fun login(req: UserReqDTO.Req.Login): UserResDTO.Res.Login {
        val userInfo = twitterUserRepository.findUserByEmail(req.userEmail)
            .orElseThrow { CustomException(ResCode.USER_NOT_FOUND) }

        val authenticationToken = req.toAuthentication()
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)

        val accessToken =
            jwtUtils.createToken(authentication.name, JwtUtils.ACCESS_TOKEN_VALID_TIME)
        val userInfoDTO = UserDTO.Dto.TwitterUserAuthInfo.fromEntity(userInfo)

        val refreshToken =
            jwtUtils.createToken(authentication.name, JwtUtils.REFRESH_TOKEN_VALID_TIME)
        redisUtils.setDataExpire(
            refreshToken,
            "RT:" + authentication.name,
            JwtUtils.REFRESH_TOKEN_VALID_TIME
        )

        val now: LocalDateTime = LocalDateTime.now()
        userInfo.lastLogin = now
        twitterUserRepository.save(userInfo)

        return UserResDTO.Res.Login(
            userInfo = userInfoDTO,
            tokenInfo = UserResDTO.Res.TokenInfo(
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        )
    }

    override fun reissue(refreshToken: String): UserResDTO.Res.TokenInfo {
        if (jwtUtils.validateToken(refreshToken) != JwtTokenStatus.VALID) {
            throw CustomException(ResCode.REFRESH_TOKEN_EXPIRED)
        }
        val userEmail = jwtUtils.getUserEmail(refreshToken)
        val userInfo = redisUtils.getData(refreshToken)
        if (ObjectUtils.isEmpty(refreshToken) || userInfo == null || userInfo != "RT:${userEmail}") {
            throw CustomException(ResCode.INVALID_TOKEN)
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

    override fun logout(accessToken: String, refreshToken: String): UserResDTO.Res.Logout {
        if (jwtUtils.validateToken(accessToken) != JwtTokenStatus.VALID) {
            throw CustomException(ResCode.BAD_REQUEST)
        }

        val authentication = jwtUtils.getAuthentication(accessToken)

        if (redisUtils.getData(refreshToken) != null) {
            redisUtils.deleteData(refreshToken)
        }

        val expiration = jwtUtils.getExpiration(accessToken)
        redisUtils.setDataExpire(accessToken, "logout", expiration)
        return UserResDTO.Res.Logout(authentication.name)
    }

    override fun getUserInfoByUserId(userId: String): UserResDTO.Res.UserInfo {
        val result = twitterUserRepository.findById(UUID.fromString(userId))
            .orElseThrow { CustomException(ResCode.USER_NOT_FOUND) }
        return UserResDTO.Res.UserInfo(UserDTO.Dto.TwitterUserInfo.fromEntity(result))
    }

    override fun getUserProfileByUserName(userName: String): UserResDTO.Res.UserProfile {
        val result = twitterUserRepository.findUserByUserName(userName)
            .orElseThrow { CustomException(ResCode.USER_NOT_FOUND) }
        val countOfTweets = tweetRepository.countByUserUserName(userName)
        val userProfile = UserDTO.Dto.TwitterUserProfile.fromEntity(result)

        return UserResDTO.Res.UserProfile(userProfile = userProfile, countOfTweet = countOfTweets)
    }

    override fun followToUser(
        user: TwitterUser,
        req: UserReqDTO.Req.FollowReq
    ): UserResDTO.Res.FollowRes {
        val followee = twitterUserRepository.findById(req.userId)
            .orElseThrow { CustomException(ResCode.USER_NOT_FOUND) }

        val followeeData = UserFollower(followee = followee, follower = user)
        userFollowerRepository.save(followeeData)

        return UserResDTO.Res.FollowRes(userId = followee.userId)
    }

    override fun unfollowToUser(
        user: TwitterUser,
        req: UserReqDTO.Req.FollowReq
    ): UserResDTO.Res.FollowRes {
        if (userFollowerRepository.existsByFolloweeUserIdAndFollowerUserId(
                req.userId,
                user.userId
            )
        ) {
            userFollowerRepository.deleteByFolloweeUserIdAndFollowerUserId(req.userId, user.userId)
            return UserResDTO.Res.FollowRes(userId = req.userId)
        } else {
            throw CustomException(ResCode.BAD_REQUEST)
        }
    }
}