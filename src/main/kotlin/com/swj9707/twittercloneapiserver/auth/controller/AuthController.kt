package com.swj9707.twittercloneapiserver.auth.controller

import com.swj9707.twittercloneapiserver.auth.dto.UserReqDTO
import com.swj9707.twittercloneapiserver.auth.dto.UserResDTO
import com.swj9707.twittercloneapiserver.auth.entity.TwitterUser
import com.swj9707.twittercloneapiserver.auth.service.TwitterUserServiceImpl
import com.swj9707.twittercloneapiserver.constant.dto.BaseResponse
import com.swj9707.twittercloneapiserver.constant.enum.BaseResponseCode
import com.swj9707.twittercloneapiserver.exception.BaseException
import com.swj9707.twittercloneapiserver.utils.CookieUtil
import com.swj9707.twittercloneapiserver.utils.JwtUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth/v1")
class AuthController(
    private val twitterUserServiceImpl: TwitterUserServiceImpl,
    private val jwtUtil: JwtUtil,
    private val cookieUtil: CookieUtil) {

    @GetMapping("/test")
    fun test() : ResponseEntity<Any> {
        return ResponseEntity.ok().body("Test API")
    }

    @PostMapping("/register")
    fun register(@RequestBody userRegistReq: UserReqDTO.Req.Register) : ResponseEntity<BaseResponse<UserResDTO.Res.Register>> {
        if(twitterUserServiceImpl.existsUser(userRegistReq.userEmail)){
            throw BaseException(BaseResponseCode.DUPLICATE_EMAIL)
        }
        val result = twitterUserServiceImpl.createUser(userRegistReq)
        return ResponseEntity.ok().body(BaseResponse.success(result))
    }

    @PostMapping("/login")
    fun login(@RequestBody userLoginReq: UserReqDTO.Req.Login,
                req : HttpServletRequest,
                res : HttpServletResponse) : ResponseEntity<BaseResponse<UserResDTO.Res.Login>> {
        val result = twitterUserServiceImpl.login(userLoginReq)
        val refreshTokenCookie = result.tokenInfo.refreshToken?.let { cookieUtil.createCookie(JwtUtil.REFRESH_TOKEN_NAME, it) }
        res.addCookie(refreshTokenCookie)
        return ResponseEntity.ok().body(BaseResponse.success(result))
    }


    @PostMapping("/logout")
    fun logout(@AuthenticationPrincipal user : TwitterUser,
               @CookieValue(value = JwtUtil.REFRESH_TOKEN_NAME, defaultValue = "") refreshToken: String,
               req : HttpServletRequest, res : HttpServletResponse) : ResponseEntity<BaseResponse<UserResDTO.Res.Logout>> {

        val accessToken = jwtUtil.resolveToken(req)
        if(accessToken != null){
            val result = twitterUserServiceImpl.logout(accessToken)
            if(refreshToken.isNotEmpty()){
                cookieUtil.deleteCookie(req, res, JwtUtil.REFRESH_TOKEN_NAME)
            }
            return ResponseEntity.ok().body(BaseResponse.success(result))
        } else {
           throw BaseException(BaseResponseCode.INVALID_TOKEN)
        }
    }

    @PostMapping("/reissue")
    fun reissue(@RequestBody reissueRes : UserReqDTO.Req.Reissue,
                @CookieValue(value= JwtUtil.REFRESH_TOKEN_NAME, defaultValue = "") refreshToken : String,
                req : HttpServletRequest,
                res : HttpServletResponse) : ResponseEntity<BaseResponse<UserResDTO.Res.TokenInfo>>{

        if(refreshToken.isNotEmpty()){
            val result = twitterUserServiceImpl.reissue(refreshToken, reissueRes.accessToken)
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