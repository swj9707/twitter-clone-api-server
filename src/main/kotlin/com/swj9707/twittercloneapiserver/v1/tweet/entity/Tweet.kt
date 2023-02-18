package com.swj9707.twittercloneapiserver.v1.tweet.entity

import com.swj9707.twittercloneapiserver.constant.entity.BaseEntity
import com.swj9707.twittercloneapiserver.constant.enum.TweetStatus
import com.swj9707.twittercloneapiserver.constant.entity.Image
import com.swj9707.twittercloneapiserver.v1.user.entity.TwitterUser
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "TWEET")
class Tweet (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tweet_id")
    val tweetId : Long = 0L,

    @Column(name = "tweet_content")
    var tweetContent : String,

    @Column(name = "modified")
    var modified : Boolean = false,

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    var status : TweetStatus = TweetStatus.NORMAL,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id")
    val user : TwitterUser,

    @Column(name = "connected_tweet_id")
    var connectedTweetId : Long? = null,

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tweet_images",
        joinColumns = [JoinColumn(name = "tweet_id")],
        inverseJoinColumns = [JoinColumn(name = "image_id")])
    var images : MutableList<Image>? = ArrayList(),

    @OneToMany(mappedBy = "tweet", fetch = FetchType.LAZY)
    var retweets : MutableList<ReTweet>? = ArrayList(),

    @OneToMany(mappedBy = "tweet", fetch = FetchType.LAZY)
    var likes : MutableList<Like>? = ArrayList(),

    @OneToMany(mappedBy = "tweet", fetch = FetchType.LAZY)
    var replyTweets : MutableList<ReplyTweet>? = ArrayList()

    ) : BaseEntity()