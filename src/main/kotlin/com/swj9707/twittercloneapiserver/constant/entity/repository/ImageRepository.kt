package com.swj9707.twittercloneapiserver.constant.entity.repository

import com.swj9707.twittercloneapiserver.constant.entity.Image
import org.springframework.data.jpa.repository.JpaRepository

interface ImageRepository : JpaRepository<Image, Long> {
}