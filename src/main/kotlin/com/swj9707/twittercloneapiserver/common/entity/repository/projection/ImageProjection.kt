package com.swj9707.twittercloneapiserver.common.entity.repository.projection

interface ImageProjection {
    fun getImageId(): Long
    fun getImageUrl(): String
}