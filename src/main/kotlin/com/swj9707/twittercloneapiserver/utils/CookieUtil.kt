package com.swj9707.twittercloneapiserver.utils

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Service

@Service
class CookieUtil {
    fun createCookie(cookieName : String, value : String) : Cookie {
        var token : Cookie = Cookie(cookieName, value)
        token.isHttpOnly = true
        token.maxAge = JwtUtil.ACCESS_TOKEN_VALID_TIME.toInt()
        token.path = "/"
        return token
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