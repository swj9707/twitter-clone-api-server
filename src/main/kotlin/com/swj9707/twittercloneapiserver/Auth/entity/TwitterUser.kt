package com.swj9707.twittercloneapiserver.Auth.entity

import com.swj9707.twittercloneapiserver.constant.entity.BaseEntity
import com.swj9707.twittercloneapiserver.enum.UserStatus
import com.swj9707.twittercloneapiserver.enum.UserType
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "TWITTER_USER")
class TwitterUser (
    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.UUID)
    @GenericGenerator(name="uuid2", strategy="uuid2")
    @Column(name = "user_id")
    val userId: UUID = UUID.randomUUID(),

    @Column(name="user_email")
    val email: String,

    @Column(name="user_passwd")
    val passwd: String,

    @Column(name="user_name")
    val userName: String,

    @Column(name="user_type", columnDefinition = "varchar(10) default 'EMAIL'")
    @Enumerated(EnumType.STRING)
    val userType: UserType = UserType.EMAIL,

    @Column(name="user_status", columnDefinition = "varchar(10) default 'NOR'")
    @Enumerated(EnumType.STRING)
    val userStatus: UserStatus = UserStatus.NOR,

    @Column
    var lastLogin: LocalDateTime? = null,

    ) : BaseEntity() {
        fun updateLoginTime(now : LocalDateTime){
            this.lastLogin = now
        }
    }