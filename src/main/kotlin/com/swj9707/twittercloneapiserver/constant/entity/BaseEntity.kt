package com.swj9707.twittercloneapiserver.constant.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {
    @CreatedDate
    @Column(name = "create_at", nullable = false, updatable = false)
    protected open var createAt: LocalDateTime = LocalDateTime.MIN

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    protected open var updatedAt: LocalDateTime? = LocalDateTime.MIN
}