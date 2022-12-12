package com.swj9707.twittercloneapiserver.Auth.controller

import com.swj9707.twittercloneapiserver.Auth.dto.UserReqDTO
import com.swj9707.twittercloneapiserver.Auth.dto.UserResDTO
import com.swj9707.twittercloneapiserver.Auth.service.TwitterUserService
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
    fun register(@RequestBody userRegistReq: UserReqDTO.req.Register) : ResponseEntity<UserResDTO.res.Register> {
        if(twitterUserService.existsUser(userRegistReq.userEmail)){
            throw BaseException(BaseResponseCode.DUPLICATE_EMAIL)
        }
        val response = twitterUserService.createUser(userRegistReq)
        return ResponseEntity.ok().body(response)
    }

    @PostMapping("/login")
    fun login(@RequestBody userLoginReq: UserReqDTO.req.Login) : ResponseEntity<UserResDTO.res.Login> {
        val response = twitterUserService.login(userLoginReq)
        return ResponseEntity.ok().body(response)
    }

    @PostMapping("/logout")
    fun logout(@RequestBody userLogoutRes : UserReqDTO.req.Logout) : ResponseEntity<UserResDTO.res.Logout> {
        val response = twitterUserService.logout(userLogoutRes)
        return ResponseEntity.ok().body(response)
    }

    @PostMapping("/reissue")
    fun reissue(@RequestBody reissueRes : UserReqDTO.req.Reissue) : ResponseEntity<UserResDTO.res.TokenInfo>{
        val response = twitterUserService.reissue(reissueRes)
        return ResponseEntity.ok().body(response)
    }

}