package com.swj9707.twittercloneapiserver.utils

import com.swj9707.twittercloneapiserver.global.utils.JwtUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class JwtUtilTest(
    @Autowired
    private val jwtUtils: JwtUtils
){
    private val logger = LoggerFactory.getLogger(javaClass)

}