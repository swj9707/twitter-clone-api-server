package com.swj9707.twittercloneapiserver.v1.image.service.inter

import com.swj9707.twittercloneapiserver.global.common.dto.ImageDTO
import org.springframework.web.multipart.MultipartFile

interface ImageService {
    fun uploadImage(imageData : MultipartFile) : ImageDTO.Dto.ImageInfo
}