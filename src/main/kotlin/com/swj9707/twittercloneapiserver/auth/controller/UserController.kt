package com.swj9707.twittercloneapiserver.auth.controller

import com.swj9707.twittercloneapiserver.auth.dto.UserReqDTO
import com.swj9707.twittercloneapiserver.auth.dto.UserResDTO
import com.swj9707.twittercloneapiserver.auth.entity.TwitterUser
import com.swj9707.twittercloneapiserver.auth.service.TwitterUserServiceImpl
import com.swj9707.twittercloneapiserver.constant.dto.BaseResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val twitterUserServiceImpl: TwitterUserServiceImpl
) {
    @PutMapping("/editProfile")
    fun editProfile(
        @AuthenticationPrincipal user : TwitterUser,
        @RequestBody request : UserReqDTO.Req.EditProfile) : ResponseEntity<BaseResponse<UserResDTO.Res.EditProfile>>  {
        val result = twitterUserServiceImpl.editUserProfile(request)
        return ResponseEntity.ok().body(BaseResponse.success(result))
    }

    @PutMapping("/editPassword")
    fun editPassword(
        @AuthenticationPrincipal user : TwitterUser,
        @RequestBody request : UserReqDTO.Req.EditPassword) : ResponseEntity<BaseResponse<UserResDTO.Res.EditPassword>>{
        val result = twitterUserServiceImpl.editUserPassword(request)
        return ResponseEntity.ok().body(BaseResponse.success(result))
    }
}