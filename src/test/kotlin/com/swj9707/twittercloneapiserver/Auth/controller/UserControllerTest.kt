package com.swj9707.twittercloneapiserver.Auth.controller

import com.swj9707.twittercloneapiserver.Auth.dto.register.TwitterUserRegistReq
import com.swj9707.twittercloneapiserver.Auth.service.TwitterUserService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class UserControllerTest(
    @Autowired
    private val userService: TwitterUserService
) {

}