package com.swj9707.twittercloneapiserver.v1.tweet.service.inter

import com.swj9707.twittercloneapiserver.v1.user.entity.TwitterUser
import com.swj9707.twittercloneapiserver.v1.tweet.dto.TweetReqDTO
import com.swj9707.twittercloneapiserver.v1.tweet.dto.TweetResDTO
import org.springframework.data.domain.Pageable
import java.util.*

interface TweetService {

    fun createTweet(userInfo : TwitterUser, request : TweetReqDTO.Req.CreateTweet) : TweetResDTO.Res.TweetInfo
    fun createReplyTweet(userInfo : TwitterUser, request: TweetReqDTO.Req.CreateTweet) : TweetResDTO.Res.TweetInfo
    fun retweet(userInfo: TwitterUser, tweetId : Long) : TweetResDTO.Res.RetweetResult
    fun likeTweet(userInfo: TwitterUser, tweetId : Long) : TweetResDTO.Res.TweetInfo
    fun readTweets(pageable : Pageable) : TweetResDTO.Res.TweetsRes
    fun getUsersTweets(userId : UUID, pageable: Pageable) : TweetResDTO.Res.UserTweetsRes
    fun getUsersRetweetsAndReplies(userId : UUID, pageable: Pageable) : TweetResDTO.Res.UserTweetsRes
    fun getUsersLikes(userId : UUID, pageable: Pageable) : TweetResDTO.Res.UserTweetsRes
    fun getUserTweets(userName : String, pageable : Pageable) : TweetResDTO.Res.TweetsRes
    fun deleteTweet(userInfo : TwitterUser, request: TweetReqDTO.Req.DeleteTweet) : TweetResDTO.Res.TweetInfo

}