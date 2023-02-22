package com.swj9707.twittercloneapiserver.v1.tweet.controller

import com.swj9707.twittercloneapiserver.constant.dto.BaseResponse
import com.swj9707.twittercloneapiserver.v1.tweet.dto.TweetDTO
import com.swj9707.twittercloneapiserver.v1.user.entity.TwitterUser
import com.swj9707.twittercloneapiserver.v1.tweet.dto.TweetReqDTO
import com.swj9707.twittercloneapiserver.v1.tweet.dto.TweetResDTO
import com.swj9707.twittercloneapiserver.v1.tweet.service.TweetServiceImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/tweet")
class TweetController(
    private val tweetService: TweetServiceImpl
) {
    @PostMapping("/create")
    fun createTweet(
        @AuthenticationPrincipal user: TwitterUser, @RequestBody request: TweetReqDTO.Req.CreateTweet
    ): ResponseEntity<BaseResponse<TweetResDTO.Res.TweetInfo>> {
        val response = tweetService.createTweet(user, request)
        return ResponseEntity.ok().body(BaseResponse.success(response))
    }

    @PostMapping("/reply")
    fun createReplyTweet(
        @AuthenticationPrincipal user: TwitterUser, @RequestBody request: TweetReqDTO.Req.CreateTweet
    ): ResponseEntity<BaseResponse<TweetResDTO.Res.TweetInfo>> {
        val response = tweetService.createReplyTweet(user, request)
        return ResponseEntity.ok().body(BaseResponse.success(response))
    }

    @GetMapping("/retweet")
    fun retweetRequest(
        @AuthenticationPrincipal user: TwitterUser, @RequestParam(name = "tweetId", defaultValue = "") tweetId: Long
    ): ResponseEntity<BaseResponse<TweetResDTO.Res.RetweetResult>> {
        val response = tweetService.retweet(user, tweetId)
        return ResponseEntity.ok().body(BaseResponse.success(response))
    }

    @GetMapping("/like")
    fun likeRequest(
        @AuthenticationPrincipal user: TwitterUser, @RequestParam(name = "tweetId", defaultValue = "") tweetId: Long
    ): ResponseEntity<BaseResponse<TweetResDTO.Res.TweetInfo>> {
        val response = tweetService.likeTweet(user, tweetId)
        return ResponseEntity.ok().body(BaseResponse.success(response))
    }

    @GetMapping("/read")
    fun readTweets(
        @PageableDefault(
            size = 5,
            sort = ["tweetId"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable
    ): ResponseEntity<BaseResponse<TweetResDTO.Res.TweetsRes>> {
        val response = tweetService.readTweets(pageable)
        return ResponseEntity.ok().body(BaseResponse.success(response))
    }

    @GetMapping("/getTweet")
    fun getTweetByTweetId(
        @RequestParam(name ="tweetId", defaultValue = "") tweetId : Long
    ) : ResponseEntity<BaseResponse<TweetDTO.Dto.TweetInfo>> {
        val response = tweetService.getUsetTweetByTweetId(tweetId)
        return ResponseEntity.ok().body(BaseResponse.success(response))
    }

    @GetMapping("/getUsersTweet")
    fun getUserTweetsAndRetweet(
        @PageableDefault(size = 5) pageable: Pageable,
        @RequestParam(name = "userId", defaultValue = "") userId : UUID
    ) : ResponseEntity<BaseResponse<TweetResDTO.Res.UserTweetsRes>> {
        val response = tweetService.getUsersTweets(userId, pageable)
        return ResponseEntity.ok().body(BaseResponse.success(response))
    }

    @GetMapping("/getUserReplies")
    fun getUserRepliesAndRetweet(
        @PageableDefault(size = 5) pageable: Pageable,
        @RequestParam(name = "userId", defaultValue = "") userId : UUID
    ) : ResponseEntity<BaseResponse<TweetResDTO.Res.UserTweetsRes>> {
        val response = tweetService.getUsersRetweetsAndReplies(userId, pageable)
        return ResponseEntity.ok().body(BaseResponse.success(response))
    }

    @GetMapping("/getUsersLikes")
    fun getUsersLikes(
        @PageableDefault(size = 5) pageable: Pageable,
        @RequestParam(name = "userId", defaultValue = "") userId : UUID
    ) : ResponseEntity<BaseResponse<TweetResDTO.Res.UserTweetsRes>> {
        val response = tweetService.getUsersLikes(userId, pageable)
        return ResponseEntity.ok().body(BaseResponse.success(response))
    }

    @Deprecated(message = "더이상 사용하지 않음. 역할을 다 하셨습니다.", replaceWith = ReplaceWith("/getUsersTweet"))
    @GetMapping("/user")
    fun getUserTweets(
        @PageableDefault(size = 5, sort = ["tweetId"], direction = Sort.Direction.DESC) pageable: Pageable,
        @RequestParam(name = "userName", defaultValue = "") userName: String
    ): ResponseEntity<BaseResponse<TweetResDTO.Res.TweetsRes>> {
        val response = tweetService.getUserTweets(userName, pageable)
        return ResponseEntity.ok().body(BaseResponse.success(response))
    }

    @GetMapping("/getReplies")
    fun getReplies(
        @PageableDefault(size = 5, sort = ["tweetId"], direction = Sort.Direction.DESC) pageable: Pageable,
        @RequestParam(name = "tweetId", defaultValue = "") tweetId : Long
    ) : ResponseEntity<BaseResponse<TweetResDTO.Res.TweetsRes>> {
        val response = tweetService.getTweetReplies(tweetId, pageable)
        return ResponseEntity.ok().body(BaseResponse.success(response))
    }

    @PutMapping("/delete")
    fun deleteTweet(
        @AuthenticationPrincipal user: TwitterUser, @RequestBody request: TweetReqDTO.Req.DeleteTweet
    ): ResponseEntity<BaseResponse<TweetResDTO.Res.TweetInfo>> {
        val response = tweetService.deleteTweet(user, request)
        return ResponseEntity.ok().body(BaseResponse.success(response))
    }
}