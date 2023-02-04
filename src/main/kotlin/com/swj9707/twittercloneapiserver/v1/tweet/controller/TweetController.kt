package com.swj9707.twittercloneapiserver.v1.tweet.controller

import com.swj9707.twittercloneapiserver.constant.dto.BaseResponse
import com.swj9707.twittercloneapiserver.v1.user.entity.TwitterUser
import com.swj9707.twittercloneapiserver.v1.tweet.dto.TweetDTO
import com.swj9707.twittercloneapiserver.v1.tweet.dto.TweetReqDTO
import com.swj9707.twittercloneapiserver.v1.tweet.dto.TweetResDTO
import com.swj9707.twittercloneapiserver.v1.tweet.service.TweetServiceImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/tweet")
class TweetController (
    private val tweetService: TweetServiceImpl
        ) {
    @PostMapping("/create")
    fun createTweet(@AuthenticationPrincipal user : TwitterUser, @RequestBody request : TweetReqDTO.Req.CreateTweet) : ResponseEntity<BaseResponse<TweetResDTO.Res.TweetInfo>> {
        val response = tweetService.createTweet(user, request)
        return ResponseEntity.ok().body(BaseResponse.success(response))
    }
    @PostMapping("/uploadTweetImage")
    fun uploadTweetImage(@RequestParam("file") file : MultipartFile) : ResponseEntity<BaseResponse<TweetResDTO.Res.TweetImageRes>> {
        val response = tweetService.uploadTweetImage(file)
        return ResponseEntity.ok().body(BaseResponse.success(response))
    }

    @GetMapping("/readAll")
    @Deprecated("테스트용! 실 사용 시 사용하지 말것")
    fun readAllTweets() : ResponseEntity<BaseResponse<List<TweetDTO>>>{
        val response = tweetService.readAllTweets()
        return ResponseEntity.ok().body(BaseResponse.success(response))
    }

    @GetMapping("/read")
    fun readTweets(@PageableDefault(size = 10, sort = ["tweetId"], direction = Sort.Direction.DESC) pageable : Pageable)
        : ResponseEntity<BaseResponse<TweetResDTO.Res.Tweets>>{
        val response = tweetService.readTweets(pageable)
        return ResponseEntity.ok().body(BaseResponse.success(response))
    }

    @PutMapping("/delete")
    fun deleteTweet(@AuthenticationPrincipal user : TwitterUser, @RequestBody request : TweetReqDTO.Req.DeleteTweet) : ResponseEntity<BaseResponse<TweetResDTO.Res.TweetInfo>> {
        val response = tweetService.deleteTweet(user, request)
        return ResponseEntity.ok().body(BaseResponse.success(response))
    }
}