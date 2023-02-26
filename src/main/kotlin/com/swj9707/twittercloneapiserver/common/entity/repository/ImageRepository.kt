package com.swj9707.twittercloneapiserver.common.entity.repository

import com.swj9707.twittercloneapiserver.common.entity.Image
import org.springframework.data.jpa.repository.JpaRepository

interface ImageRepository : JpaRepository<Image, Long> {
}