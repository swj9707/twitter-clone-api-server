package com.swj9707.twittercloneapiserver.Auth.controller

import com.swj9707.twittercloneapiserver.Auth.dto.UserReqDTO
import com.swj9707.twittercloneapiserver.Auth.dto.UserResDTO
import com.swj9707.twittercloneapiserver.Auth.service.TwitterUserService
import com.swj9707.twittercloneapiserver.constant.dto.BaseResponse
import com.swj9707.twittercloneapiserver.constant.enum.BaseResponseCode
import com.swj9707.twittercloneapiserver.exception.BaseException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth/v1")
class AuthController(private val twitterUserService: TwitterUserService) {

    @GetMapping("/test")
    fun test() : ResponseEntity<Any> {
        return ResponseEntity.ok().body("Test API");
    }

    @PostMapping("/register")
    fun register(@RequestBody userRegistReq: UserReqDTO.Req.Register) : ResponseEntity<BaseResponse<UserResDTO.Res.Register>> {
        if(twitterUserService.existsUser(userRegistReq.userEmail)){
            throw BaseException(BaseResponseCode.DUPLICATE_EMAIL)
        }
        val result = twitterUserService.createUser(userRegistReq)
        return ResponseEntity.ok().body(BaseResponse.success(result))
    }

    @PostMapping("/login")
    fun login(@RequestBody userLoginReq: UserReqDTO.Req.Login) : ResponseEntity<BaseResponse<UserResDTO.Res.Login>> {
        val result = twitterUserService.login(userLoginReq)
        return ResponseEntity.ok().body(BaseResponse.success(result))
    }

    @PostMapping("/logout")
    fun logout(@RequestBody userLogoutRes : UserReqDTO.Req.Logout) : ResponseEntity<BaseResponse<UserResDTO.Res.Logout>> {
        val result = twitterUserService.logout(userLogoutRes)
        return ResponseEntity.ok().body(BaseResponse.success(result))
    }

    @PostMapping("/reissue")
    fun reissue(@RequestBody reissueRes : UserReqDTO.Req.Reissue) : ResponseEntity<BaseResponse<UserResDTO.Res.TokenInfo>>{
        val result = twitterUserService.reissue(reissueRes)
        return ResponseEntity.ok().body(BaseResponse.success(result))
    }

}