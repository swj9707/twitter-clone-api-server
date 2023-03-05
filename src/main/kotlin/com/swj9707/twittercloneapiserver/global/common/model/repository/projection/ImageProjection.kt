package com.swj9707.twittercloneapiserver.global.common.model.repository.projection

interface ImageProjection {
    fun getImageId(): Long
    fun getImageUrl(): String
}