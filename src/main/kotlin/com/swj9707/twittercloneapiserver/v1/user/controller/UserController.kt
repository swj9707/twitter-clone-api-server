package com.swj9707.twittercloneapiserver.v1.user.controller

import com.swj9707.twittercloneapiserver.v1.user.dto.UserReqDTO
import com.swj9707.twittercloneapiserver.v1.user.dto.UserResDTO
import com.swj9707.twittercloneapiserver.v1.user.service.TwitterUserServiceImpl
import com.swj9707.twittercloneapiserver.constant.dto.BaseResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val twitterUserServiceImpl: TwitterUserServiceImpl
) {
    @GetMapping("/getUserProfile")
    fun getUserInfo(@RequestParam(name = "userName", defaultValue = "") userName : String) : ResponseEntity<BaseResponse<UserResDTO.Res.UserProfile>> {
        val result = twitterUserServiceImpl.getUserInfoByUserName(userName)
        return ResponseEntity.ok().body(BaseResponse.success(result))
    }
    @PutMapping("/editProfile")
    fun editProfile(
        @RequestBody request : UserReqDTO.Req.EditProfile) : ResponseEntity<BaseResponse<UserResDTO.Res.EditProfile>>  {
        val result = twitterUserServiceImpl.editUserProfile(request)
        return ResponseEntity.ok().body(BaseResponse.success(result))
    }

    @PutMapping("/editPassword")
    fun editPassword(
        @RequestBody request : UserReqDTO.Req.EditPassword) : ResponseEntity<BaseResponse<UserResDTO.Res.EditPassword>>{
        val result = twitterUserServiceImpl.editUserPassword(request)
        return ResponseEntity.ok().body(BaseResponse.success(result))
    }
}