package com.swj9707.twittercloneapiserver.constant.entity

import jakarta.persistence.*

@Entity
@Table(name = "image_info")
class Image (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    val imageId : Long,

    @Column(name = "image_url")
    val imageUrl : String
)