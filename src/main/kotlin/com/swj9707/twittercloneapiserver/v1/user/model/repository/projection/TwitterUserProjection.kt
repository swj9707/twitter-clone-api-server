package com.swj9707.twittercloneapiserver.v1.user.model.repository.projection

interface TwitterUserProjection {
    fun getUserName() : String
    fun getUserNickName() : String
}