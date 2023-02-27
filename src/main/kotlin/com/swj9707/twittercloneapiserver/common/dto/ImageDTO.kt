package com.swj9707.twittercloneapiserver.common.dto

import com.swj9707.twittercloneapiserver.common.entity.Image
import com.swj9707.twittercloneapiserver.common.entity.repository.projection.ImageProjection

class ImageDTO {
    companion object Dto {
        data class ImageInfo(
            val imageId: Long?, var imageUrl: String
        ) {
            companion object {
                fun entityToDTO(entity: Image): ImageInfo {
                    return ImageInfo(imageId = entity.imageId, imageUrl = entity.imageUrl)
                }

                fun projectionToDTO(projection: ImageProjection): ImageInfo {
                    return ImageInfo(imageId = projection.getImageId(), imageUrl = projection.getImageUrl())
                }

                fun entitiesToListDTO(list: MutableList<Image>?): MutableList<ImageInfo>? {
                    return list?.map { entityToDTO(it) }?.toMutableList()
                }

                fun projectionsToListDTO(list: MutableList<ImageProjection>?): MutableList<ImageInfo>? {
                    return list?.map { projectionToDTO(it) }?.toMutableList()
                }
            }
        }
    }
}