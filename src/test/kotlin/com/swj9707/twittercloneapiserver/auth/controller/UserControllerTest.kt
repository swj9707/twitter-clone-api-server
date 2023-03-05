package com.swj9707.twittercloneapiserver.auth.controller

import com.swj9707.twittercloneapiserver.v1.user.model.repository.TwitterUserRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
internal class UserTest(
    @Autowired
    private val userRepository : TwitterUserRepository
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    @Test
    @DisplayName("유저 UUID 가져오기")
    fun getUsers() {
        val result = userRepository.findAll()
        result.forEach {
            logger.info(it.userId.toString())
        }
    }
    @Test
    @DisplayName("유저 정보 찾기")
    fun getUserById() {
        val result = userRepository.findById(UUID.fromString("36da33b9-0187-40b9-b4f0-9d46c8fd8f7a"))
        logger.info("Result : ${result.get().userName}")
    }
}