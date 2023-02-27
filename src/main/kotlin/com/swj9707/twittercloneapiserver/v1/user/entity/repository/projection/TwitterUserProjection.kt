package com.swj9707.twittercloneapiserver.v1.user.entity.repository.projection

interface TwitterUserProjection {
    fun getUserName() : String
    fun getUserNickName() : String
}