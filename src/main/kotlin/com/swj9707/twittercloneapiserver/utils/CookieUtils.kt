package com.swj9707.twittercloneapiserver.utils

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Service

@Service
class CookieUtils {

    @Value("\${client-side.domain}")
    lateinit var clientDomain : String

    @Value("\${client-side.local-domain}")
    lateinit var localDomain : String

    fun createCookie(cookieName: String, value: String) : ResponseCookie {
        return ResponseCookie.from(cookieName, value)
            .path("/")
            .secure(true)
            .sameSite("None")
            .httpOnly(true)
            .maxAge(JwtUtils.REFRESH_TOKEN_VALID_TIME)
            .build()
    }

    fun getCookie(req : HttpServletRequest, cookieName : String) : Cookie? {
        val cookies  = req.cookies ?: return null
        for(cookie in cookies){
            if(cookie.name.equals(cookieName)){
                return cookie
            }
        }
        return null
    }

    fun deleteCookie(req: HttpServletRequest, res : HttpServletResponse, cookieName: String) {
        val cookie = getCookie(req, cookieName)
        if(cookie != null){
            val myCookie = Cookie(cookieName, null)
            myCookie.maxAge = 0
            myCookie.path = "/"
            res.addCookie(myCookie)
        }
    }
}