package com.swj9707.twittercloneapiserver.Auth.controller

import com.swj9707.twittercloneapiserver.Auth.dto.register.TwitterUserRegistReq
import com.swj9707.twittercloneapiserver.Auth.dto.register.TwitterUserRegistRes
import com.swj9707.twittercloneapiserver.Auth.dto.signin.TwitterUserLoginReq
import com.swj9707.twittercloneapiserver.Auth.dto.signin.TwitterUserLoginRes
import com.swj9707.twittercloneapiserver.Auth.service.TwitterUserService
import com.swj9707.twittercloneapiserver.enum.BaseResponseCode
import com.swj9707.twittercloneapiserver.exception.BaseException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user")
class UserController(private val twitterUserService: TwitterUserService) {

    @GetMapping("/test")
    suspend fun test() : ResponseEntity<Any> {
        return ResponseEntity.ok().body("Test API");
    }

    @PostMapping("/register")
    fun register(@RequestBody userRegistReq: TwitterUserRegistReq) : ResponseEntity<TwitterUserRegistRes> {
        if(twitterUserService.existsUser(userRegistReq.userEmail)){
            throw BaseException(BaseResponseCode.DUPLICATE_EMAIL)
        }
        return ResponseEntity.ok(twitterUserService.createUser(userRegistReq))
    }

    @PostMapping("/login")
    fun login(@RequestBody userLoginReq: TwitterUserLoginReq) : ResponseEntity<TwitterUserLoginRes>{
        return ResponseEntity.ok(twitterUserService.login(userLoginReq))
    }

}