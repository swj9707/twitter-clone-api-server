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

}