package com.swj9707.twittercloneapiserver.v1.image.service

import com.swj9707.twittercloneapiserver.constant.dto.ImageDTO
import com.swj9707.twittercloneapiserver.constant.entity.Image
import com.swj9707.twittercloneapiserver.constant.entity.repository.ImageRepository
import com.swj9707.twittercloneapiserver.constant.enum.BaseResponseCode
import com.swj9707.twittercloneapiserver.exception.BaseException
import com.swj9707.twittercloneapiserver.utils.FileUtils
import com.swj9707.twittercloneapiserver.utils.StringUtils
import com.swj9707.twittercloneapiserver.v1.image.service.inter.ImageService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class ImageServiceImpl (
    private val imageRepository: ImageRepository
) : ImageService {

    @Value("\${file.ImageLocation}")
    lateinit var imgLocation: String
    @Value("\${file.cdn.tweetImage}")
    lateinit var cdnUrl : String

    @Transactional
    override fun uploadImage(imageData: MultipartFile): ImageDTO.Dto.ImageInfo {
        try{
            val fileName = StringUtils.createImageFileName(imageData.originalFilename)
            FileUtils.uploadFile(imgLocation, fileName, imageData.bytes)
            val imageEntity = Image(imageUrl = "$cdnUrl/$fileName")
            imageRepository.save(imageEntity)
            return ImageDTO.Dto.ImageInfo(imageId = imageEntity.imageId, imageUrl = imageEntity.imageUrl)
        } catch (e : Exception){
            throw BaseException(BaseResponseCode.FILE_UPLOAD_ERROR)
        }
    }
}