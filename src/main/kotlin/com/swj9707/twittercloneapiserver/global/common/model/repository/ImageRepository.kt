package com.swj9707.twittercloneapiserver.global.common.model.repository

import com.swj9707.twittercloneapiserver.global.common.model.Image
import org.springframework.data.jpa.repository.JpaRepository

interface ImageRepository : JpaRepository<Image, Long> {
}