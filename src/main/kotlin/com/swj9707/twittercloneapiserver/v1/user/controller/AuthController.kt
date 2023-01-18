package com.swj9707.twittercloneapiserver.v1.user.controller

import com.swj9707.twittercloneapiserver.v1.user.dto.UserReqDTO
import com.swj9707.twittercloneapiserver.v1.user.dto.UserResDTO
import com.swj9707.twittercloneapiserver.v1.user.entity.TwitterUser
import com.swj9707.twittercloneapiserver.v1.user.service.TwitterUserServiceImpl
import com.swj9707.twittercloneapiserver.constant.dto.BaseResponse
import com.swj9707.twittercloneapiserver.constant.enum.BaseResponseCode
import com.swj9707.twittercloneapiserver.exception.BaseException
import com.swj9707.twittercloneapiserver.utils.CookieUtils
import com.swj9707.twittercloneapiserver.utils.JwtUtils
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders.SET_COOKIE
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth/v1")
class AuthController(
    private val twitterUserServiceImpl: TwitterUserServiceImpl,
    private val jwtUtils: JwtUtils,
    private val cookieUtils: CookieUtils) {

    @GetMapping("/test")
    fun testApi() : ResponseEntity<BaseResponse<String>> {
        return ResponseEntity.ok().body(BaseResponse.success("Heartbeat check *^^*"))
    }

    @PostMapping("/register")
    fun register(@RequestBody request: UserReqDTO.Req.Register) : ResponseEntity<BaseResponse<UserResDTO.Res.Register>> {
        val result = twitterUserServiceImpl.createUser(request)
        return ResponseEntity.ok().body(BaseResponse.success(result))
    }

    @PostMapping("/login")
    fun login(@RequestBody request: UserReqDTO.Req.Login,
              req : HttpServletRequest,
              res : HttpServletResponse) : ResponseEntity<BaseResponse<UserResDTO.Res.Login>> {
        val result = twitterUserServiceImpl.login(request)
        val refreshTokenCookie = result.tokenInfo.refreshToken.let { cookieUtils.createCookie(JwtUtils.REFRESH_TOKEN_NAME, it) }
        return ResponseEntity.ok().header(SET_COOKIE, refreshTokenCookie.toString()).body(BaseResponse.success(result))
    }


    @PostMapping("/logout")
    fun logout(@AuthenticationPrincipal user : TwitterUser,
               @CookieValue(value = JwtUtils.REFRESH_TOKEN_NAME, defaultValue = "") refreshToken: String,
               req : HttpServletRequest, res : HttpServletResponse) : ResponseEntity<BaseResponse<UserResDTO.Res.Logout>> {

        val accessToken = jwtUtils.resolveToken(req)
        if(accessToken != null){
            val result = twitterUserServiceImpl.logout(accessToken)
            if(refreshToken.isNotEmpty()){
                cookieUtils.deleteCookie(req, res, JwtUtils.REFRESH_TOKEN_NAME)
            }
            return ResponseEntity.ok().body(BaseResponse.success(result))
        } else {
           throw BaseException(BaseResponseCode.INVALID_TOKEN)
        }
    }

    @PostMapping("/reissue")
    fun reissue(@CookieValue(value= JwtUtils.REFRESH_TOKEN_NAME, defaultValue = "") refreshToken : String,
                req : HttpServletRequest,
                res : HttpServletResponse) : ResponseEntity<BaseResponse<UserResDTO.Res.TokenInfo>>{

        if(refreshToken.isNotEmpty()){
            val result = twitterUserServiceImpl.reissue(refreshToken)
            if(!result.refreshToken.isEmpty()){
                cookieUtils.deleteCookie(req, res, JwtUtils.REFRESH_TOKEN_NAME)
                val refreshTokenCookie = cookieUtils.createCookie(JwtUtils.REFRESH_TOKEN_NAME, result.refreshToken)
                return ResponseEntity.ok().header(SET_COOKIE, refreshTokenCookie.toString()).body(BaseResponse.success(result))
            }
            return ResponseEntity.ok().body(BaseResponse.success(result))
        } else {
            throw BaseException(BaseResponseCode.REFRESH_TOKEN_EXPIRED)
        }

    }

}