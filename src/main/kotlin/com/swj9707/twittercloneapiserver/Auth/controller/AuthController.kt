package com.swj9707.twittercloneapiserver.Auth.controller

import com.swj9707.twittercloneapiserver.Auth.dto.UserReqDTO
import com.swj9707.twittercloneapiserver.Auth.dto.UserResDTO
import com.swj9707.twittercloneapiserver.Auth.service.TwitterUserService
import com.swj9707.twittercloneapiserver.constant.dto.BaseResponse
import com.swj9707.twittercloneapiserver.constant.enum.BaseResponseCode
import com.swj9707.twittercloneapiserver.exception.BaseException
import com.swj9707.twittercloneapiserver.utils.CookieUtil
import com.swj9707.twittercloneapiserver.utils.JwtUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth/v1")
class AuthController(
    private val twitterUserService: TwitterUserService,
    private val cookieUtil: CookieUtil) {

    @GetMapping("/test")
    fun test() : ResponseEntity<Any> {
        return ResponseEntity.ok().body("Test API")
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
    fun login(@RequestBody userLoginReq: UserReqDTO.Req.Login,
                req : HttpServletRequest,
                res : HttpServletResponse) : ResponseEntity<BaseResponse<UserResDTO.Res.Login>> {
        val result = twitterUserService.login(userLoginReq)
        val refreshTokenCookie = result.tokenInfo.refreshToken?.let { cookieUtil.createCookie(JwtUtil.REFRESH_TOKEN_NAME, it) }
        res.addCookie(refreshTokenCookie)
        return ResponseEntity.ok().body(BaseResponse.success(result))
    }

    @PostMapping("/logout")
    fun logout(@RequestBody userLogoutRes : UserReqDTO.Req.Logout,
                @CookieValue(value = JwtUtil.REFRESH_TOKEN_NAME, defaultValue = "") refreshToken: String,
               req : HttpServletRequest, res : HttpServletResponse) : ResponseEntity<BaseResponse<UserResDTO.Res.Logout>> {
        val result = twitterUserService.logout(userLogoutRes)
        if(refreshToken.isNotEmpty()){
            cookieUtil.deleteCookie(req, res, JwtUtil.REFRESH_TOKEN_NAME)
        }
        return ResponseEntity.ok().body(BaseResponse.success(result))
    }

    @PostMapping("/reissue")
    fun reissue(@RequestBody reissueRes : UserReqDTO.Req.Reissue,
                @CookieValue(value= JwtUtil.REFRESH_TOKEN_NAME, defaultValue = "") refreshToken : String,
                req : HttpServletRequest,
                res : HttpServletResponse) : ResponseEntity<BaseResponse<UserResDTO.Res.TokenInfo>>{

        if(refreshToken.isNotEmpty()){
            val result = twitterUserService.reissue(refreshToken, reissueRes.accessToken)
            if(result.refreshToken != null){
                cookieUtil.deleteCookie(req, res, JwtUtil.REFRESH_TOKEN_NAME)
                val refreshTokenCookie = cookieUtil.createCookie(JwtUtil.REFRESH_TOKEN_NAME, result.refreshToken!!)
                res.addCookie(refreshTokenCookie)
            }
            return ResponseEntity.ok().body(BaseResponse.success(result))
        } else {
            throw BaseException(BaseResponseCode.REFRESH_TOKEN_EXPIRED)
        }

    }

}