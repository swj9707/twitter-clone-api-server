package com.swj9707.twittercloneapiserver.v1.auth.entity

import com.swj9707.twittercloneapiserver.constant.entity.BaseEntity
import com.swj9707.twittercloneapiserver.constant.enum.Authority
import com.swj9707.twittercloneapiserver.constant.enum.UserStatus
import com.swj9707.twittercloneapiserver.constant.enum.Provider
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import java.util.UUID
import java.util.stream.Collectors

@Entity
@Table(name = "TWITTER_USER")
data class TwitterUser (
    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.UUID)
    @GenericGenerator(name="uuid2", strategy="uuid2")
    @Column(name = "user_id")
    val userId: UUID = UUID.randomUUID(),

    @Column(name="user_email")
    val email: String,

    @Column(name="user_passwd")
    var passwd: String,

    @Column(name="user_name")
    var userName: String,

    @Column(name="user_role")
    @Enumerated(EnumType.STRING)
    val userRole : Authority = Authority.ROLE_USER,

    @Column(name="provider", columnDefinition = "varchar(10) default 'EMAIL'")
    @Enumerated(EnumType.STRING)
    val provider: Provider = Provider.EMAIL,

    @Column(name="user_status", columnDefinition = "varchar(10) default 'NOR'")
    @Enumerated(EnumType.STRING)
    val userStatus: UserStatus = UserStatus.NOR,

    @Column
    var lastLogin: LocalDateTime? = null,

    ) : BaseEntity(), UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val auth = mutableListOf<Authority>(this.userRole)
        return auth.stream().map{role -> SimpleGrantedAuthority(role.toString())}.collect(Collectors.toSet())
    }

    override fun getPassword(): String {
        return this.passwd
    }

    override fun getUsername(): String {
        return this.email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
       return true
    }
}