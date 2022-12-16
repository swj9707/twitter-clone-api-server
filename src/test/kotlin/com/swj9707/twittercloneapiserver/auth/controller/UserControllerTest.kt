package com.swj9707.twittercloneapiserver.auth.controller

import com.swj9707.twittercloneapiserver.auth.service.TwitterUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class UserControllerTest(
    @Autowired
    private val userService: TwitterUserService
) {

}