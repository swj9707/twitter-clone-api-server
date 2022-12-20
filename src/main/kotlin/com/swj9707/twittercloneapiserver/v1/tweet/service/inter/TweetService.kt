package com.swj9707.twittercloneapiserver.v1.tweet.service.inter

import com.swj9707.twittercloneapiserver.v1.auth.entity.TwitterUser
import com.swj9707.twittercloneapiserver.v1.tweet.dto.TweetReqDTO
import com.swj9707.twittercloneapiserver.v1.tweet.dto.TweetResDTO

interface TweetService {

    fun createTweet(userInfo : TwitterUser, request : TweetReqDTO.Req.CreateTweet) : TweetResDTO.Res.TweetInfo

    fun updateTweet(userInfo : TwitterUser, request : TweetReqDTO.Req.UpdateTweet) : TweetResDTO.Res.TweetInfo

    fun deleteTweet(userInfo : TwitterUser, request: TweetReqDTO.Req.DeleteTweet) : TweetResDTO.Res.TweetInfo
}