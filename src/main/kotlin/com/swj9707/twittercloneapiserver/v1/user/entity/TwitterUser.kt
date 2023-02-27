package com.swj9707.twittercloneapiserver.v1.user.entity

import com.swj9707.twittercloneapiserver.common.entity.BaseEntity
import com.swj9707.twittercloneapiserver.common.enum.Authority
import com.swj9707.twittercloneapiserver.common.enum.UserStatus
import com.swj9707.twittercloneapiserver.common.enum.Provider
import com.swj9707.twittercloneapiserver.common.entity.Image
import com.swj9707.twittercloneapiserver.v1.tweet.entity.Like
import com.swj9707.twittercloneapiserver.v1.tweet.entity.ReTweet
import com.swj9707.twittercloneapiserver.v1.tweet.entity.Tweet
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.JdbcTypeCode
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import java.util.ArrayList
import java.util.UUID
import java.util.stream.Collectors

@Entity
@Table(name = "TWITTER_USER")
class TwitterUser(
    @Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.UUID)
    @GenericGenerator(name="uuid2", strategy="uuid2")
    @JdbcTypeCode(java.sql.Types.VARCHAR)
    @Column(name = "user_id")
    val userId: UUID = UUID.randomUUID(),

    @Column(name="user_email")
    val email: String,

    @Column(name="user_passwd")
    var passwd: String,

    @Column(name="user_name")
    var userName : String,

    @Column(name="user_nickname")
    var userNickname: String,

    @Column(name="user_role")
    @Enumerated(EnumType.STRING)
    val userRole: Authority = Authority.ROLE_USER,

    @Column(name="provider", columnDefinition = "varchar(10) default 'EMAIL'")
    @Enumerated(EnumType.STRING)
    val provider: Provider = Provider.EMAIL,

    @Column(name="user_status", columnDefinition = "varchar(10) default 'NOR'")
    @Enumerated(EnumType.STRING)
    val userStatus: UserStatus = UserStatus.NOR,

    @Column
    var lastLogin: LocalDateTime? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "twitter_user_profile",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "image_id")])
    var profileImage: Image? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "twitter_user_background_image",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "image_id")])
    var backgroundImage: Image? = null,

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val tweets : MutableList<Tweet> = ArrayList(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val retweets : MutableList<ReTweet> = ArrayList(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val likes : MutableList<Like> = ArrayList(),

    @OneToMany(mappedBy = "followee", fetch = FetchType.LAZY)
    val followers : MutableList<UserFollower> = ArrayList(),

    @OneToMany(mappedBy = "follower", fetch = FetchType.LAZY)
    val following : MutableList<UserFollower> = ArrayList(),

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