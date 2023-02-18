package com.swj9707.twittercloneapiserver.constant.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseImmutableEntity {
    @CreatedDate
    @Column(name = "create_at", nullable = false, updatable = false)
    open var createAt: LocalDateTime = LocalDateTime.MIN
}