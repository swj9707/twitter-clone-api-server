package com.swj9707.twittercloneapiserver.v1.user.controller

import com.swj9707.twittercloneapiserver.v1.user.dto.vo.UserReqDTO
import com.swj9707.twittercloneapiserver.v1.user.dto.vo.UserResDTO
import com.swj9707.twittercloneapiserver.v1.user.service.TwitterUserServiceImpl
import com.swj9707.twittercloneapiserver.global.common.dto.BaseRes
import com.swj9707.twittercloneapiserver.v1.user.model.TwitterUser
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val twitterUserServiceImpl: TwitterUserServiceImpl
) {
    @GetMapping("/getUserInfo")
    fun getUserInfo(
        @RequestParam(
            name = "userId",
            defaultValue = ""
        ) userId: String
    ): ResponseEntity<BaseRes<UserResDTO.Res.UserInfo>> {
        val result = twitterUserServiceImpl.getUserInfoByUserId(userId)
        return ResponseEntity.ok().body(BaseRes.success(result))
    }

    @GetMapping("/getUserProfile")
    fun getUserProfile(
        @RequestParam(
            name = "userName",
            defaultValue = ""
        ) userName: String
    ): ResponseEntity<BaseRes<UserResDTO.Res.UserProfile>> {
        val result = twitterUserServiceImpl.getUserProfileByUserName(userName)
        return ResponseEntity.ok().body(BaseRes.success(result))
    }

    @PutMapping("/editProfile")
    fun editProfile(
        @RequestBody request: UserReqDTO.Req.EditProfile
    ): ResponseEntity<BaseRes<UserResDTO.Res.EditProfile>> {
        val result = twitterUserServiceImpl.editUserProfile(request)
        return ResponseEntity.ok().body(BaseRes.success(result))
    }

    @PutMapping("/editUserProfile")
    fun editUserProfile(
        @RequestBody request: UserReqDTO.Req.EditUserProfile
    ): ResponseEntity<BaseRes<UserResDTO.Res.EditProfile>> {
        val result = twitterUserServiceImpl.editUserProfile(request)
        return ResponseEntity.ok().body(BaseRes.success(result))
    }

    @PutMapping("/editPassword")
    fun editPassword(
        @RequestBody request: UserReqDTO.Req.EditPassword
    ): ResponseEntity<BaseRes<UserResDTO.Res.EditPassword>> {
        val result = twitterUserServiceImpl.editUserPassword(request)
        return ResponseEntity.ok().body(BaseRes.success(result))
    }

    @PostMapping("/followUser")
    fun followUser(
        @AuthenticationPrincipal user: TwitterUser,
        @RequestBody request : UserReqDTO.Req.FollowReq
    ) : Unit? {
        return null
    }
}