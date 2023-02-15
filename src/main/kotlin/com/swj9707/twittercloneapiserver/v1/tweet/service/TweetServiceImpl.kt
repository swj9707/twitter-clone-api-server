package com.swj9707.twittercloneapiserver.v1.tweet.service

import com.swj9707.twittercloneapiserver.constant.entity.Image
import com.swj9707.twittercloneapiserver.constant.enum.BaseResponseCode
import com.swj9707.twittercloneapiserver.constant.enum.TweetStatus
import com.swj9707.twittercloneapiserver.exception.BaseException
import com.swj9707.twittercloneapiserver.v1.user.entity.TwitterUser
import com.swj9707.twittercloneapiserver.v1.tweet.dto.TweetDTO
import com.swj9707.twittercloneapiserver.v1.tweet.dto.TweetReqDTO
import com.swj9707.twittercloneapiserver.v1.tweet.dto.TweetResDTO
import com.swj9707.twittercloneapiserver.v1.tweet.entity.ReTweet
import com.swj9707.twittercloneapiserver.v1.tweet.entity.Tweet
import com.swj9707.twittercloneapiserver.v1.tweet.repository.RetweetRepository
import com.swj9707.twittercloneapiserver.v1.tweet.repository.TweetRepository
import com.swj9707.twittercloneapiserver.v1.tweet.service.inter.TweetService
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TweetServiceImpl(
    private val tweetRepository: TweetRepository,
    private val retweetRepository: RetweetRepository
) : TweetService{

    @Transactional
    override fun createTweet(userInfo : TwitterUser, request: TweetReqDTO.Req.CreateTweet): TweetResDTO.Res.TweetInfo {
        val tweet = Tweet(
            tweetContent = request.tweetContent,
            images = Image.dtoListToEntityList(request.tweetImages),
            user = userInfo
        )
        tweetRepository.save(tweet)

        return TweetResDTO.Res.TweetInfo(tweetId = tweet.tweetId)
    }

    override fun createReplyTweet(
        userInfo: TwitterUser,
        request: TweetReqDTO.Req.CreateReplyTweet
    ): TweetResDTO.Res.TweetInfo {
        val tweet = tweetRepository.findById(request.tweetId)
            .orElseThrow{ BaseException(BaseResponseCode.TWEET_NOT_FOUND) }

        val replyTweet = Tweet(
            tweetContent = request.tweetContent,
            images = Image.dtoListToEntityList(request.tweetImage),
            user = userInfo,
            connectedTweetId = request.tweetId
        )
        tweetRepository.save(replyTweet)
        tweet.replies.add(replyTweet)

        return TweetResDTO.Res.TweetInfo(tweetId = replyTweet.tweetId)
    }

    override fun retweet(userInfo: TwitterUser, tweetId: Long): TweetResDTO.Res.RetweetResult {
        val tweet = tweetRepository.findById(tweetId)
            .orElseThrow{ BaseException(BaseResponseCode.TWEET_NOT_FOUND)}

        val retweets = userInfo.retweets

        val retweet = retweets.stream().filter{t -> t.tweet.tweetId?.equals(tweet.tweetId) ?: false }
            .findFirst()

        return if(retweet.isPresent){
            retweets.remove(retweet.get())
            retweetRepository.delete(retweet.get())
            TweetResDTO.Res.RetweetResult(result = false)
        } else {
            val newRetweet = ReTweet(
                user = userInfo,
                tweet = tweet
            )
            retweetRepository.save(newRetweet)
            userInfo.retweets.add(newRetweet)
            TweetResDTO.Res.RetweetResult(result = true)
        }
    }

    override fun readTweets(pageable: Pageable): TweetResDTO.Res.TweetsRes {
        val result = tweetRepository.findTweetsByStatusNot(pageable)
        val responseData = TweetDTO.Dto.TweetInfo.toPageableDTO(result)
        return TweetResDTO.Res.TweetsRes(
            tweets = responseData.content,
            size = responseData.size,
            number = responseData.number,
            first = responseData.isFirst,
            last = responseData.isLast,
            numberOfElements = responseData.numberOfElements,
            empty = responseData.isEmpty
        )
    }

    override fun getUserTweets(userName : String, pageable: Pageable): TweetResDTO.Res.TweetsRes {
        val result = tweetRepository.findTweetsByUserUserName(userName, pageable)
        val responseData = TweetDTO.Dto.TweetInfo.toPageableDTO(result)
        return TweetResDTO.Res.TweetsRes(
            tweets = responseData.content,
            size = responseData.size,
            number = responseData.number,
            first = responseData.isFirst,
            last = responseData.isLast,
            numberOfElements = responseData.numberOfElements,
            empty = responseData.isEmpty
        )
    }

    @Transactional
    override fun deleteTweet(userInfo : TwitterUser, request: TweetReqDTO.Req.DeleteTweet): TweetResDTO.Res.TweetInfo {
        val tweet = tweetRepository.findById(request.tweetId)
            .orElseThrow { BaseException(BaseResponseCode.TWEET_NOT_FOUND) }

        if(tweet.user.userId != userInfo.userId){
            throw BaseException(BaseResponseCode.FORBIDDEN)
        }

        tweet.status = TweetStatus.DELETED
        tweetRepository.save(tweet)
        return TweetResDTO.Res.TweetInfo(tweetId = tweet.tweetId)
    }
}