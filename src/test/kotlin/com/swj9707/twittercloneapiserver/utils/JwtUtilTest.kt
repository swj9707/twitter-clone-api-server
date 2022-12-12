package com.swj9707.twittercloneapiserver.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Duration

@SpringBootTest
internal class JwtUtilTest(
    @Autowired
    private val jwtUtil: JwtUtil
){
    private val logger = LoggerFactory.getLogger(javaClass)

    @Test
    @DisplayName("토큰 유효기간 판별 로직 검증 테스트")
    fun getExpirationPeriodTest(){
        val token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzd2o5NzA3QGdtYWlsLmNvbSIsInVzZXJFbWFpbCI6InN3ajk3MDdAZ21haWwuY29tIiwiaWF0IjoxNjcwODIyOTgxLCJleHAiOjE2NzIwMzI1ODF9.iOsuwVCXKusFR3dGGblTmh3zJ-5R7G65gwIWLwQ8GDs"
        val result = jwtUtil.getExpirationPeriod(token)
        logger.info("result : " + result)
    }
}