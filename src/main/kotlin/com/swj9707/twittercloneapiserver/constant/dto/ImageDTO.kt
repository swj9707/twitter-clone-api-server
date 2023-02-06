package com.swj9707.twittercloneapiserver.constant.dto

import com.swj9707.twittercloneapiserver.constant.entity.Image

data class ImageDTO (
    val imageId : Long?,
    var imageUrl : String
) {
    companion object {
        fun entityToDTO(entity : Image) : ImageDTO {
            return ImageDTO(imageId = entity.imageId, imageUrl = entity.imageUrl)
        }

        fun entitysToListDTO(list : MutableList<Image>?) : MutableList<ImageDTO>? {
            return list?.map{entityToDTO(it)}?.toMutableList()
        }
    }
}