package com.swj9707.twittercloneapiserver.v1.tweet.controller

import com.swj9707.twittercloneapiserver.global.common.dto.BaseRes
import com.swj9707.twittercloneapiserver.v1.tweet.dto.TweetDTO
import com.swj9707.twittercloneapiserver.v1.user.model.TwitterUser
import com.swj9707.twittercloneapiserver.v1.tweet.dto.vo.TweetReqDTO
import com.swj9707.twittercloneapiserver.v1.tweet.dto.vo.TweetResDTO
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
    ): ResponseEntity<BaseRes<TweetResDTO.Res.TweetInfo>> {
        val response = tweetService.createTweet(user, request)
        return ResponseEntity.ok().body(BaseRes.success(response))
    }

    @PostMapping("/reply")
    fun createReplyTweet(
        @AuthenticationPrincipal user: TwitterUser, @RequestBody request: TweetReqDTO.Req.CreateTweet
    ): ResponseEntity<BaseRes<TweetResDTO.Res.TweetInfo>> {
        val response = tweetService.createReplyTweet(user, request)
        return ResponseEntity.ok().body(BaseRes.success(response))
    }

    @GetMapping("/retweet")
    fun retweetRequest(
        @AuthenticationPrincipal user: TwitterUser, @RequestParam(name = "tweetId", defaultValue = "") tweetId: Long
    ): ResponseEntity<BaseRes<TweetResDTO.Res.RetweetResult>> {
        val response = tweetService.retweet(user, tweetId)
        return ResponseEntity.ok().body(BaseRes.success(response))
    }

    @GetMapping("/like")
    fun likeRequest(
        @AuthenticationPrincipal user: TwitterUser, @RequestParam(name = "tweetId", defaultValue = "") tweetId: Long
    ): ResponseEntity<BaseRes<TweetResDTO.Res.TweetInfo>> {
        val response = tweetService.likeTweet(user, tweetId)
        return ResponseEntity.ok().body(BaseRes.success(response))
    }

    @GetMapping("/read")
    fun readTweets(
        @PageableDefault(
            size = 5,
            sort = ["tweetId"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable
    ): ResponseEntity<BaseRes<TweetResDTO.Res.TweetsRes>> {
        val response = tweetService.readTweets(pageable)
        return ResponseEntity.ok().body(BaseRes.success(response))
    }

    @GetMapping("/getTweet")
    fun getTweetByTweetId(
        @RequestParam(name ="tweetId", defaultValue = "") tweetId : Long
    ) : ResponseEntity<BaseRes<TweetDTO.Dto.TweetInfo>> {
        val response = tweetService.getUsetTweetByTweetId(tweetId)
        return ResponseEntity.ok().body(BaseRes.success(response))
    }

    @GetMapping("/getUsersTweet")
    fun getUserTweetsAndRetweet(
        @PageableDefault(size = 5) pageable: Pageable,
        @RequestParam(name = "userId", defaultValue = "") userId : UUID
    ) : ResponseEntity<BaseRes<TweetResDTO.Res.UserTweetsRes>> {
        val response = tweetService.getUsersTweets(userId, pageable)
        return ResponseEntity.ok().body(BaseRes.success(response))
    }

    @GetMapping("/getUserReplies")
    fun getUserRepliesAndRetweet(
        @PageableDefault(size = 5) pageable: Pageable,
        @RequestParam(name = "userId", defaultValue = "") userId : UUID
    ) : ResponseEntity<BaseRes<TweetResDTO.Res.UserTweetsRes>> {
        val response = tweetService.getUsersRetweetsAndReplies(userId, pageable)
        return ResponseEntity.ok().body(BaseRes.success(response))
    }

    @GetMapping("/getUsersLikes")
    fun getUsersLikes(
        @PageableDefault(size = 5) pageable: Pageable,
        @RequestParam(name = "userId", defaultValue = "") userId : UUID
    ) : ResponseEntity<BaseRes<TweetResDTO.Res.UserTweetsRes>> {
        val response = tweetService.getUsersLikes(userId, pageable)
        return ResponseEntity.ok().body(BaseRes.success(response))
    }

    @GetMapping("/getReplies")
    fun getReplies(
        @PageableDefault(size = 5, sort = ["tweetId"], direction = Sort.Direction.DESC) pageable: Pageable,
        @RequestParam(name = "tweetId", defaultValue = "") tweetId : Long
    ) : ResponseEntity<BaseRes<TweetResDTO.Res.TweetsRes>> {
        val response = tweetService.getTweetReplies(tweetId, pageable)
        return ResponseEntity.ok().body(BaseRes.success(response))
    }

    @PutMapping("/delete")
    fun deleteTweet(
        @AuthenticationPrincipal user: TwitterUser, @RequestBody request: TweetReqDTO.Req.DeleteTweet
    ): ResponseEntity<BaseRes<TweetResDTO.Res.TweetInfo>> {
        val response = tweetService.deleteTweet(user, request)
        return ResponseEntity.ok().body(BaseRes.success(response))
    }
}