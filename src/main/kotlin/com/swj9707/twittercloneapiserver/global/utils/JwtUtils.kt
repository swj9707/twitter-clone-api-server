package com.swj9707.twittercloneapiserver.global.utils

import com.swj9707.twittercloneapiserver.v1.user.service.UserDetailsServiceImpl
import com.swj9707.twittercloneapiserver.global.common.enum.ResCode
import com.swj9707.twittercloneapiserver.global.exception.CustomException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException
import java.nio.charset.StandardCharsets
import java.security.Key
import java.time.*
import java.util.*

@Component
class JwtUtils(
    private val userDetailsServiceImpl: UserDetailsServiceImpl
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Value("\${jwt.security.key}")
    private lateinit var SECRETKEY: String

    companion object {
        const val REFRESH_TOKEN_NAME = "refreshToken"
        const val ACCESS_TOKEN_VALID_TIME = 2 * 60 * 1000L
        const val REFRESH_TOKEN_VALID_TIME = 30 * 24 * 60 * 60 * 1000L
    }

    private val signatureAlgorithm: SignatureAlgorithm = SignatureAlgorithm.HS256

    fun getSigningkey(secretKey: String): Key {
        return Keys.hmacShaKeyFor(SECRETKEY.toByteArray(StandardCharsets.UTF_8))
    }

    fun extractAllClaims(jwtToken: String): Claims {
        return Jwts.parserBuilder().setSigningKey(getSigningkey(SECRETKEY)).build().parseClaimsJws(jwtToken).body
    }

    fun createToken(userEmail: String, validTime: Long): String {
        val claims: Claims = Jwts.claims().setSubject(userEmail)
        claims["userEmail"] = userEmail
        return Jwts.builder().setClaims(claims).setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + validTime))
            .signWith(getSigningkey(SECRETKEY), signatureAlgorithm).compact()
    }

    fun getAuthentication(token: String): Authentication {
        val userDetails = userDetailsServiceImpl.loadUserByUsername(getUserEmail(token))
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun getUserEmail(token: String): String {
        val result = extractAllClaims(token).get("userEmail", String::class.java)
        return result
    }

    fun resolveToken(request: HttpServletRequest): String? {
        var token = request.getHeader("Authorization")
        if (token != null) {
            token = token.substring("Bearer ".length)
        }
        return token
    }

    fun validateToken(jwtToken: String): Boolean {
        return try {
            val claims = Jwts.parserBuilder().setSigningKey(getSigningkey(SECRETKEY)).build().parseClaimsJws(jwtToken)
            !claims.body.expiration.before(Date())
        } catch (e: SecurityException) {
            throw JwtException("Invalid JWT Signature")
        } catch (e: MalformedJwtException) {
            throw JwtException("Malformed JWT Signature")
        } catch (e: UnsupportedJwtException) {
            throw JwtException("Unsupported JWT Token")
        } catch (e: IllegalArgumentException) {
            throw JwtException("JWT token compact of handler are invalid")
        } catch (e: ExpiredJwtException) {
            throw CustomException(ResCode.EXPIRED_TOKEN)
        }
    }

    fun getExpirationPeriod(jwtToken: String): Int {
        return try {
            val claims = Jwts.parserBuilder().setSigningKey(getSigningkey(SECRETKEY)).build().parseClaimsJws(jwtToken)
            val expireDate =
                Instant.ofEpochMilli(claims.body.expiration.time).atZone(ZoneId.systemDefault()).toLocalDate()
            val today = LocalDate.now()
            Period.between(today, expireDate).days
        } catch (e: Exception) {
            throw CustomException(ResCode.INVALID_TOKEN)
        }
    }

    fun getExpiration(jwtToken: String): Long {
        val expiration = Jwts.parserBuilder().setSigningKey(getSigningkey(SECRETKEY)).build()
            .parseClaimsJws(jwtToken).body.expiration
        val now = System.currentTimeMillis()
        return expiration.time - now
    }
}