package com.swj9707.twittercloneapiserver.v1.tweet.service

import com.swj9707.twittercloneapiserver.constant.enum.BaseResponseCode
import com.swj9707.twittercloneapiserver.constant.enum.TweetStatus
import com.swj9707.twittercloneapiserver.exception.BaseException
import com.swj9707.twittercloneapiserver.v1.auth.entity.TwitterUser
import com.swj9707.twittercloneapiserver.v1.tweet.dto.TweetDTO
import com.swj9707.twittercloneapiserver.v1.tweet.dto.TweetReqDTO
import com.swj9707.twittercloneapiserver.v1.tweet.dto.TweetResDTO
import com.swj9707.twittercloneapiserver.v1.tweet.entity.Tweet
import com.swj9707.twittercloneapiserver.v1.tweet.repository.TweetRepository
import com.swj9707.twittercloneapiserver.v1.tweet.service.inter.TweetService
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class TweetServiceImpl(
    private val tweetRepository: TweetRepository
) : TweetService{
    override fun createTweet(userInfo : TwitterUser, request: TweetReqDTO.Req.CreateTweet): TweetResDTO.Res.TweetInfo {
        val tweet = Tweet(
            userId = userInfo.userId,
            tweetContent = request.tweetContent,
            tweetImageMeta = request.tweetImageMeta
        )
        tweetRepository.save(tweet)
        return TweetResDTO.Res.TweetInfo(tweetId = tweet.tweetId)
    }

    override fun readTweets(pageable: Pageable): TweetResDTO.Res.Tweets {
        val result = tweetRepository.findTweetsByStatusNot(TweetStatus.DELETED, pageable)
        val responseData = TweetDTO.pageEntityToDTO(result)
        return TweetResDTO.Res.Tweets(
            tweets = responseData.content,
            size = responseData.size,
            number = responseData.number,
            first = responseData.isFirst,
            last = responseData.isLast,
            numberOfElements = responseData.numberOfElements,
            empty = responseData.isEmpty
        )
    }

    override fun updateTweet(userInfo : TwitterUser, request: TweetReqDTO.Req.UpdateTweet): TweetResDTO.Res.TweetInfo {
        var tweet = tweetRepository.findById(request.tweetId)
            .orElseThrow{ BaseException(BaseResponseCode.TWEET_NOT_FOUND)}

        if(tweet.userId != userInfo.userId){
            throw BaseException(BaseResponseCode.FORBIDDEN)
        }

        tweet.tweetContent = request.tweetContent
        tweet.tweetImageMeta = request.tweetImageMeta
        tweet.modified = true
        tweetRepository.save(tweet)
        return TweetResDTO.Res.TweetInfo(tweetId = tweet.tweetId)
    }

    override fun deleteTweet(userInfo : TwitterUser, request: TweetReqDTO.Req.DeleteTweet): TweetResDTO.Res.TweetInfo {
        var tweet = tweetRepository.findById(request.tweetId)
            .orElseThrow { BaseException(BaseResponseCode.TWEET_NOT_FOUND) }

        if(tweet.userId != userInfo.userId){
            throw BaseException(BaseResponseCode.FORBIDDEN)
        }

        tweet.status = TweetStatus.DELETED
        tweetRepository.save(tweet)
        return TweetResDTO.Res.TweetInfo(tweetId = tweet.tweetId)
    }
}