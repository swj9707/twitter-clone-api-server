package com.swj9707.twittercloneapiserver.v1.tweet.controller

import com.swj9707.twittercloneapiserver.constant.dto.BaseResponse
import com.swj9707.twittercloneapiserver.v1.auth.entity.TwitterUser
import com.swj9707.twittercloneapiserver.v1.tweet.dto.TweetReqDTO
import com.swj9707.twittercloneapiserver.v1.tweet.dto.TweetResDTO
import com.swj9707.twittercloneapiserver.v1.tweet.service.TweetServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/tweet")
class TweetController (
    private val tweetService: TweetServiceImpl
        ) {

    @PostMapping("/create")
    fun createTweet(@AuthenticationPrincipal user : TwitterUser,
        @RequestBody request : TweetReqDTO.Req.CreateTweet) : ResponseEntity<BaseResponse<TweetResDTO.Res.TweetInfo>> {
        val response = tweetService.createTweet(user, request)
        return ResponseEntity.ok().body(BaseResponse.success(response))
    }

    @PutMapping("/update")
    fun updateTweet(@AuthenticationPrincipal user : TwitterUser,
    @RequestBody request : TweetReqDTO.Req.UpdateTweet) : ResponseEntity<BaseResponse<TweetResDTO.Res.TweetInfo>> {
        val response = tweetService.updateTweet(user, request)
        return ResponseEntity.ok().body(BaseResponse.success(response))
    }

    @PutMapping("/delete")
    fun deleteTweet(@AuthenticationPrincipal user : TwitterUser,
    @RequestBody request : TweetReqDTO.Req.DeleteTweet) : ResponseEntity<BaseResponse<TweetResDTO.Res.TweetInfo>> {
        val response = tweetService.deleteTweet(user, request)
        return ResponseEntity.ok().body(BaseResponse.success(response))
    }
}