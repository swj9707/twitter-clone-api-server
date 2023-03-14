package com.swj9707.twittercloneapiserver.v1.user.controller

import com.swj9707.twittercloneapiserver.v1.user.dto.vo.UserReqDTO
import com.swj9707.twittercloneapiserver.v1.user.dto.vo.UserResDTO
import com.swj9707.twittercloneapiserver.v1.user.model.TwitterUser
import com.swj9707.twittercloneapiserver.v1.user.service.TwitterUserServiceImpl
import com.swj9707.twittercloneapiserver.global.common.dto.BaseRes
import com.swj9707.twittercloneapiserver.global.common.enum.ResCode
import com.swj9707.twittercloneapiserver.global.exception.CustomException
import com.swj9707.twittercloneapiserver.global.utils.CookieUtils
import com.swj9707.twittercloneapiserver.global.utils.JwtUtils
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
    private val cookieUtils: CookieUtils
) {

    @GetMapping("/test")
    fun testApi(): ResponseEntity<BaseRes<String>> {
        return ResponseEntity.ok().body(BaseRes.success("Heartbeat check *^^*~"))
    }

    @PostMapping("/register")
    fun register(@RequestBody request: UserReqDTO.Req.Register): ResponseEntity<BaseRes<UserResDTO.Res.Register>> {
        val result = twitterUserServiceImpl.createUser(request)
        return ResponseEntity.ok().body(BaseRes.success(result))
    }

    @PostMapping("/login")
    fun login(
        @RequestBody request: UserReqDTO.Req.Login, req: HttpServletRequest, res: HttpServletResponse
    ): ResponseEntity<BaseRes<UserResDTO.Res.Login>> {
        val result = twitterUserServiceImpl.login(request)
        val refreshTokenCookie =
            result.tokenInfo.refreshToken.let { cookieUtils.createCookie(JwtUtils.REFRESH_TOKEN_NAME, it) }
        return ResponseEntity.ok().header(SET_COOKIE, refreshTokenCookie.toString()).body(BaseRes.success(result))
    }


    @PostMapping("/logout")
    fun logout(
        @AuthenticationPrincipal user: TwitterUser,
        @CookieValue(value = JwtUtils.REFRESH_TOKEN_NAME, defaultValue = "") refreshToken: String,
        req: HttpServletRequest,
        res: HttpServletResponse
    ): ResponseEntity<BaseRes<UserResDTO.Res.Logout>> {

        val accessToken = jwtUtils.resolveToken(req)
        val result = twitterUserServiceImpl.logout(accessToken, refreshToken)
        if (refreshToken.isNotEmpty()) {
            cookieUtils.deleteCookie(req, res, JwtUtils.REFRESH_TOKEN_NAME)
        }
        return ResponseEntity.ok().body(BaseRes.success(result))
    }

    @PostMapping("/reissue")
    fun reissue(
        @CookieValue(value = JwtUtils.REFRESH_TOKEN_NAME, defaultValue = "") refreshToken: String,
        req: HttpServletRequest,
        res: HttpServletResponse
    ): ResponseEntity<BaseRes<UserResDTO.Res.TokenInfo>> {

        if (refreshToken.isNotEmpty()) {
            val result = twitterUserServiceImpl.reissue(refreshToken)
            if (result.refreshToken.isNotEmpty()) {
                cookieUtils.deleteCookie(req, res, JwtUtils.REFRESH_TOKEN_NAME)
                val refreshTokenCookie = cookieUtils.createCookie(JwtUtils.REFRESH_TOKEN_NAME, result.refreshToken)
                return ResponseEntity.ok().header(SET_COOKIE, refreshTokenCookie.toString())
                    .body(BaseRes.success(result))
            }
            return ResponseEntity.ok().body(BaseRes.success(result))
        } else {
            throw CustomException(ResCode.REFRESH_TOKEN_EXPIRED)
        }

    }

}