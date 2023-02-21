package com.swj9707.twittercloneapiserver.v1.tweet.service

import com.swj9707.twittercloneapiserver.constant.entity.Image
import com.swj9707.twittercloneapiserver.constant.enum.BaseResponseCode
import com.swj9707.twittercloneapiserver.constant.enum.TweetStatus
import com.swj9707.twittercloneapiserver.exception.BaseException
import com.swj9707.twittercloneapiserver.v1.user.entity.TwitterUser
import com.swj9707.twittercloneapiserver.v1.tweet.dto.TweetDTO
import com.swj9707.twittercloneapiserver.v1.tweet.dto.TweetReqDTO
import com.swj9707.twittercloneapiserver.v1.tweet.dto.TweetResDTO
import com.swj9707.twittercloneapiserver.v1.tweet.entity.Like
import com.swj9707.twittercloneapiserver.v1.tweet.entity.ReTweet
import com.swj9707.twittercloneapiserver.v1.tweet.entity.ReplyTweet
import com.swj9707.twittercloneapiserver.v1.tweet.entity.Tweet
import com.swj9707.twittercloneapiserver.v1.tweet.repository.LikeRepository
import com.swj9707.twittercloneapiserver.v1.tweet.repository.ReplyTweetRepository
import com.swj9707.twittercloneapiserver.v1.tweet.repository.RetweetRepository
import com.swj9707.twittercloneapiserver.v1.tweet.repository.TweetRepository
import com.swj9707.twittercloneapiserver.v1.tweet.service.inter.TweetService
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class TweetServiceImpl(
    private val tweetRepository: TweetRepository,
    private val retweetRepository: RetweetRepository,
    private val replyTweetRepository: ReplyTweetRepository,
    private val likeRepository: LikeRepository
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
        request: TweetReqDTO.Req.CreateTweet
    ): TweetResDTO.Res.TweetInfo {
        val tweet = request.tweetId?.let {
            tweetRepository.findById(it)
                .orElseThrow{ BaseException(BaseResponseCode.TWEET_NOT_FOUND) }
        }

        val replyTweet = Tweet(
            tweetContent = request.tweetContent,
            images = Image.dtoListToEntityList(request.tweetImages),
            user = userInfo,
            connectedTweetId = request.tweetId
        )
        tweetRepository.save(replyTweet)

        val tweetReplyInfo = tweet?.let {
            ReplyTweet(
                tweet = it,
                connectedTweet = replyTweet
            )
        }

        tweetReplyInfo?.let { replyTweetRepository.save(it) }
        return TweetResDTO.Res.TweetInfo(tweetId = replyTweet.tweetId)
    }

    @Transactional
    override fun retweet(userInfo: TwitterUser, tweetId: Long): TweetResDTO.Res.RetweetResult {
        val tweet = tweetRepository.findById(tweetId)
            .orElseThrow{ BaseException(BaseResponseCode.TWEET_NOT_FOUND)}

        val retweets = TweetDTO.Dto.RetweetInfo.getRetweetInfo(tweet)

        val retweet = retweets.stream().filter{t -> (t.userId == userInfo.userId) }
            .findFirst()


        return if(retweet.isPresent){
            retweetRepository.deleteById(retweet.get().id)
            TweetResDTO.Res.RetweetResult(result = false)
        } else {
            val newRetweet = ReTweet(
                user = userInfo,
                tweet = tweet
            )
            retweetRepository.save(newRetweet)
            TweetResDTO.Res.RetweetResult(result = true)
        }
    }

    @Transactional
    override fun likeTweet(userInfo: TwitterUser, tweetId: Long) : TweetResDTO.Res.TweetInfo {
        var tweet = tweetRepository.findById(tweetId)
            .orElseThrow { BaseException(BaseResponseCode.TWEET_NOT_FOUND) }

        val likes = TweetDTO.Dto.LikeInfo.getLikeInfo(tweet)

        val like = likes.stream().filter { t -> (t.userId == userInfo.userId) }
            .findFirst()

        return if(like.isPresent){
            likeRepository.deleteById(like.get().id)
            TweetResDTO.Res.TweetInfo(tweetId = tweet.tweetId)
        } else {
            val newLike = Like(
                tweet = tweet,
                user = userInfo
            )
            likeRepository.save(newLike)
            TweetResDTO.Res.TweetInfo(tweetId = tweet.tweetId)
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

    override fun getUsersTweets(userId: UUID, pageable: Pageable): TweetResDTO.Res.UserTweetsRes {
        val tweets = tweetRepository.findTweetsByUserUserId(userId)
        val retweets = retweetRepository.findRetweetsByUserUserId(userId)

        val tweetsDTO = TweetDTO.Dto.UsersTweetInfo.projectionsToListDTO(tweets)
        val retweetsDTO = TweetDTO.Dto.UsersTweetInfo.retweetProjToListDTO(retweets)

        val result = tweetsDTO.plus(retweetsDTO)
            .sortedWith(compareBy({it.modifiedDate}, {it.createdAt})).reversed()

        val start : Int = pageable.offset.toInt()
        val end : Int = (start + pageable.pageSize).coerceAtMost(result.size)

        val pageResult = PageImpl(result.subList(start, end), pageable, result.size.toLong())
        return TweetResDTO.Res.UserTweetsRes(
            tweets = pageResult.content,
            size = pageResult.size,
            number = pageResult.number,
            first = pageResult.isFirst,
            last = pageResult.isLast,
            numberOfElements = pageResult.numberOfElements,
            empty = pageResult.isEmpty
        )
    }

    override fun getUsersRetweetsAndReplies(userId: UUID, pageable: Pageable): TweetResDTO.Res.UserTweetsRes {
        val retweets = retweetRepository.findRetweetsByUserUserId(userId)
        val replies = tweetRepository.findRepliesByUserId(userId)

        val retweetsDTO = TweetDTO.Dto.UsersTweetInfo.retweetProjToListDTO(retweets)
        val repliesDTO = TweetDTO.Dto.UsersTweetInfo.projectionsToListDTO(replies)

        val result = retweetsDTO.plus(repliesDTO)
            .sortedWith(compareBy({it.modifiedDate}, {it.createdAt})).reversed()

        val start : Int = pageable.offset.toInt()
        val end : Int = (start + pageable.pageSize).coerceAtMost(result.size)

        val pageResult = PageImpl(result.subList(start, end), pageable, result.size.toLong())
        return TweetResDTO.Res.UserTweetsRes(
            tweets = pageResult.content,
            size = pageResult.size,
            number = pageResult.number,
            first = pageResult.isFirst,
            last = pageResult.isLast,
            numberOfElements = pageResult.numberOfElements,
            empty = pageResult.isEmpty
        )

    }

    override fun getUsersLikes(userId: UUID, pageable: Pageable): TweetResDTO.Res.UserTweetsRes {
        TODO("Not yet implemented")
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