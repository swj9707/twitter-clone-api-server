package com.swj9707.twittercloneapiserver.constant.dto

import com.swj9707.twittercloneapiserver.constant.entity.Image

class ImageDTO{
    companion object Dto {
        data class ImageInfo (
            val imageId : Long?,
            var imageUrl : String
        ) {
            companion object {
                fun entityToDTO(entity : Image) : ImageInfo {
                    return ImageInfo(imageId = entity.imageId, imageUrl = entity.imageUrl)
                }

                fun entitysToListDTO(list : MutableList<Image>?) : MutableList<ImageInfo>? {
                    return list?.map{entityToDTO(it)}?.toMutableList()
                }
            }
        }
    }
}