package com.swj9707.twittercloneapiserver.utils

import com.swj9707.twittercloneapiserver.Auth.service.UserDetailsServiceImpl
import com.swj9707.twittercloneapiserver.constant.enum.BaseResponseCode
import com.swj9707.twittercloneapiserver.exception.BaseException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.Key
import java.time.*
import java.util.*

@Component
class JwtUtil(
    private val userDetailsServiceImpl: UserDetailsServiceImpl
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    private var SECRET_KEY = "thisistestusersecretkeyprojectnameiswassssup"
    //토큰 유효시간 30분
    companion object{
        val ACCESS_TOKEN_NAME = "accessToken"
        val REFRESH_TOKEN_NAME = "refreshToken"
        val ACCESS_TOKEN_VALID_TIME = Duration.ofMinutes(30).toMillis()
        val REFRESH_TOKEN_VALID_TIME = Duration.ofDays(14).toMillis()
    }
    private val SIGNATURE_ALG : SignatureAlgorithm = SignatureAlgorithm.HS256

    fun getSigningkey(secretKey : String) : Key{
        return Keys.hmacShaKeyFor(SECRET_KEY.toByteArray(StandardCharsets.UTF_8))
    }

    fun extractAllClaims(jwtToken: String) : Claims {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningkey(SECRET_KEY))
            .build()
            .parseClaimsJws(jwtToken)
            .body
    }

    fun createAccessToken(userEmail : String) : String{
        val claims : Claims = Jwts.claims().setSubject(userEmail)
        claims["userEmail"] = userEmail // 정보는 key / value 쌍으로 저장된다.
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + ACCESS_TOKEN_VALID_TIME))
            .signWith(getSigningkey(SECRET_KEY), SIGNATURE_ALG)
            .compact()
    }

    fun createRefreshToken() : String{
        return Jwts.builder()
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + REFRESH_TOKEN_VALID_TIME))
            .signWith(getSigningkey(SECRET_KEY), SIGNATURE_ALG)
            .compact()
    }
    // JWT 토큰에서 인증 정보 조회
    fun getAuthentication(token: String): Authentication {
        val userDetails = userDetailsServiceImpl.loadUserByUsername(getUserEmail(token))
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    // 토큰에서 회원 정보 추출
    fun getUserEmail(token: String): String {
        val result = extractAllClaims(token).get("userEmail", String::class.java)
        return result
    }

    // Request의 Header에서 token 값을 가져옵니다. "X-AUTH-TOKEN" : "TOKEN값'
    fun resolveToken(request: HttpServletRequest): String? {
        var token = request.getHeader("Authorization")
        if(token != null){
            token = token.substring("Bearer ".length)
        }
        return token
    }

    fun isTokenExpired(token: String) : Boolean {
        val expiration = extractAllClaims(token).expiration
        return expiration.before(Date())
    }

    fun validateToken(jwtToken: String): Boolean {
        return try {
            val claims = Jwts.parserBuilder().setSigningKey(getSigningkey(SECRET_KEY)).build().parseClaimsJws(jwtToken)
            !claims.body.expiration.before(Date())
        } catch (e: Exception) {
            false
        }
    }

    fun getExpirationPeriod(jwtToken: String) : Int {
        return try {
            val claims = Jwts.parserBuilder().setSigningKey(getSigningkey(SECRET_KEY)).build().parseClaimsJws(jwtToken)
            val expireDate = Instant.ofEpochMilli(claims.body.expiration.time).atZone(ZoneId.systemDefault()).toLocalDate()
            val today = LocalDate.now()
            Period.between(today, expireDate).days
        } catch (e : Exception){
            throw BaseException(BaseResponseCode.INVALID_TOKEN)
        }
    }

    fun getExpiration(jwtToken : String) : Long {
        val expiration = Jwts.parserBuilder().setSigningKey(getSigningkey(SECRET_KEY)).build()
            .parseClaimsJws(jwtToken).body.expiration
        val now = System.currentTimeMillis()
        return expiration.time - now
    }
}