package com.swj9707.twittercloneapiserver.v1.user.service.inter

import com.swj9707.twittercloneapiserver.v1.user.dto.vo.UserReqDTO
import com.swj9707.twittercloneapiserver.v1.user.dto.vo.UserResDTO
import com.swj9707.twittercloneapiserver.v1.user.model.TwitterUser


interface TwitterUserService {
    fun createUser(userRegistReq: UserReqDTO.Req.Register): UserResDTO.Res.Register
    fun editUserProfile(editProfileReq: UserReqDTO.Req.EditProfile): UserResDTO.Res.EditProfile
    fun editUserProfile(editUserProfile: UserReqDTO.Req.EditUserProfile): UserResDTO.Res.EditProfile
    fun editUserPassword(editUserPasswordReq: UserReqDTO.Req.EditPassword): UserResDTO.Res.EditPassword
    fun login(req: UserReqDTO.Req.Login): UserResDTO.Res.Login
    fun reissue(refreshToken: String): UserResDTO.Res.TokenInfo
    fun logout(accessToken: String, refreshToken: String): UserResDTO.Res.Logout
    fun getUserInfoByUserId(userId: String): UserResDTO.Res.UserInfo
    fun getUserProfileByUserName(userName: String): UserResDTO.Res.UserProfile
    fun followToUser(user: TwitterUser, req : UserReqDTO.Req.FollowReq) : UserResDTO.Res.FollowRes
    fun unfollowToUser(user: TwitterUser, req : UserReqDTO.Req.FollowReq) : UserResDTO.Res.FollowRes
}