package com.swj9707.twittercloneapiserver.v1.tweet.entity

import com.swj9707.twittercloneapiserver.constant.entity.BaseEntity
import com.swj9707.twittercloneapiserver.constant.enum.TweetStatus
import com.swj9707.twittercloneapiserver.constant.entity.Image
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "TWEET")
class Tweet (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tweet_id")
    val tweetId : Long? = null,

    @Column(name = "user_id")
    val userId : UUID,

    @Column(name = "tweet_content")
    var tweetContent : String,

    @Column(name = "modified")
    var modified : Boolean = false,

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    var status : TweetStatus = TweetStatus.NORMAL,

    @OneToMany
    var images : MutableList<Image> = ArrayList(),

    @OneToMany(mappedBy = "tweet", fetch = FetchType.LAZY)
    var retweets : MutableList<ReTweet> = ArrayList(),

    @OneToMany(mappedBy = "tweet", fetch = FetchType.LAZY)
    var likes : MutableList<Like> = ArrayList(),

    @ManyToMany
    @JoinTable(name = "reply",
    joinColumns = [JoinColumn(name = "tweet_id")],
    inverseJoinColumns = [JoinColumn(name = "reply_tweet_id")])
    var replies : MutableList<Tweet> = ArrayList()

    ) : BaseEntity()