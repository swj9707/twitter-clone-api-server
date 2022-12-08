package com.swj9707.twittercloneapiserver.utils

import com.swj9707.twittercloneapiserver.Auth.service.UserDetailsServiceImpl
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.annotation.PostConstruct
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenProvider(
    private val userDetailsServiceImpl: UserDetailsServiceImpl
) {
    private var SECRET_KEY = "thisistestusersecretkeyprojectnameiswassssup"
    private val TOKEN_VALID_TIME = 30 * 60 * 1000L
    private val SIGNATURE_ALG : SignatureAlgorithm = SignatureAlgorithm.HS256

    @PostConstruct
    protected fun init() {
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.toByteArray())
    }

    fun createToken(userEmail : String) : String {
        val claims : Claims = Jwts.claims().setSubject(userEmail)
        claims["userEmail"] = userEmail // 정보는 key / value 쌍으로 저장된다.
        val now = Date()
        return Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + TOKEN_VALID_TIME))
            .signWith(SIGNATURE_ALG, SECRET_KEY)
            .compact()
    }

    // JWT 토큰에서 인증 정보 조회
    fun getAuthentication(token: String): Authentication {
        val userDetails = userDetailsServiceImpl.loadUserByUsername(getUserEmail(token))
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    // 토큰에서 회원 정보 추출
    fun getUserEmail(token: String): String {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).body.subject
    }

    // Request의 Header에서 token 값을 가져옵니다. "X-AUTH-TOKEN" : "TOKEN값'
    fun resolveToken(request: HttpServletRequest): String? {
        return request.getHeader("Authorization")
    }

    // 토큰의 유효성 + 만료일자 확인
    fun validateToken(jwtToken: String): Boolean {
        return try {
            val claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken)
            !claims.body.expiration.before(Date())
        } catch (e: Exception) {
            false
        }
    }
}