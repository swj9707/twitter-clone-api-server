package com.swj9707.twittercloneapiserver.constant.entity

import com.swj9707.twittercloneapiserver.constant.dto.ImageDTO
import jakarta.persistence.*

@Entity
@Table(name = "image_info")
class Image (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    val imageId : Long? = null,

    @Column(name = "image_url")
    val imageUrl : String
) {
    companion object {
        fun dtoToEntity(dto : ImageDTO.Dto.ImageInfo): Image {
            return Image(imageId = dto.imageId, imageUrl = dto.imageUrl)
        }

        fun dtoListToEntityList(dtoList : MutableList<ImageDTO.Dto.ImageInfo>?) : MutableList<Image>? {
            return dtoList?.map{ dtoToEntity(it)}?.toMutableList()
        }
    }
}