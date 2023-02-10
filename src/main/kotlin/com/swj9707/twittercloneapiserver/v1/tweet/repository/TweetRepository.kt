package com.swj9707.twittercloneapiserver.v1.tweet.repository

import com.swj9707.twittercloneapiserver.constant.enum.TweetStatus
import com.swj9707.twittercloneapiserver.v1.tweet.entity.Tweet
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository

interface TweetRepository : JpaRepository<Tweet, Long> {
    fun findTweetsByStatusNot(status : TweetStatus, pageable : Pageable) : Slice<Tweet>
    //TODO
    //현재 삭제 처리되지 않은 트윗 중 본인이 쓴 트윗과 본인이 팔로우하는 유저가 쓴 트윗만 보기
    @Deprecated("테스트용! 실 사용 하지 말것")
    fun findAllByStatusNotOrderByCreateAtDesc(status : TweetStatus) : List<Tweet>
}