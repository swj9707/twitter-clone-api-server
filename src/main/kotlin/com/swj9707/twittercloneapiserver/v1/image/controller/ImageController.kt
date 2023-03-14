package com.swj9707.twittercloneapiserver.v1.image.controller

import com.swj9707.twittercloneapiserver.global.common.dto.BaseRes
import com.swj9707.twittercloneapiserver.global.common.dto.ImageDTO
import com.swj9707.twittercloneapiserver.v1.image.service.ImageServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/image")
class ImageController(
    private val imageService: ImageServiceImpl
) {
    @PostMapping("/uploadImage")
    fun uploadImage(@RequestParam("file") file: MultipartFile): ResponseEntity<BaseRes<ImageDTO.Dto.ImageInfo>> {
        val result = imageService.uploadImage(file)
        return ResponseEntity.ok().body(BaseRes.success(result))
    }
}