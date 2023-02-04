package com.swj9707.twittercloneapiserver.v1.tweet.service

import com.swj9707.twittercloneapiserver.constant.entity.Image
import com.swj9707.twittercloneapiserver.constant.entity.repository.ImageRepository
import com.swj9707.twittercloneapiserver.constant.enum.BaseResponseCode
import com.swj9707.twittercloneapiserver.constant.enum.TweetStatus
import com.swj9707.twittercloneapiserver.exception.BaseException
import com.swj9707.twittercloneapiserver.utils.FileUtils
import com.swj9707.twittercloneapiserver.utils.StringUtils
import com.swj9707.twittercloneapiserver.v1.user.entity.TwitterUser
import com.swj9707.twittercloneapiserver.v1.tweet.dto.TweetDTO
import com.swj9707.twittercloneapiserver.v1.tweet.dto.TweetReqDTO
import com.swj9707.twittercloneapiserver.v1.tweet.dto.TweetResDTO
import com.swj9707.twittercloneapiserver.v1.tweet.entity.Tweet
import com.swj9707.twittercloneapiserver.v1.tweet.repository.TweetRepository
import com.swj9707.twittercloneapiserver.v1.tweet.service.inter.TweetService
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class TweetServiceImpl(
    private val tweetRepository: TweetRepository,
    private val imageRepository: ImageRepository
) : TweetService{

    @Value("\${file.ImageLocation}")
    lateinit var imgLocation: String
    @Value("\${file.cdn.tweetImage}")
    lateinit var cdnUrl : String

    @Transactional
    override fun createTweet(userInfo : TwitterUser, request: TweetReqDTO.Req.CreateTweet): TweetResDTO.Res.TweetInfo {


        val tweet = Tweet(
            userId = userInfo.userId,
            tweetContent = request.tweetContent,
            images = Image.dtoListToEntityList(request.tweetImages)
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

    @Transactional
    override fun uploadTweetImage(imageData: MultipartFile): TweetResDTO.Res.TweetImageRes {
        try{
            val fileName = StringUtils.Utils.createImageFileName(imageData.originalFilename)
            FileUtils.Utils.uploadFile(imgLocation, fileName, imageData.bytes)
            val imageEntity = Image(imageUrl = "$cdnUrl/$fileName")
            imageRepository.save(imageEntity)
            return TweetResDTO.Res.TweetImageRes(imageId = imageEntity.imageId, imageUrl = imageEntity.imageUrl)
        } catch (e : Exception){
            throw BaseException(BaseResponseCode.FILE_UPLOAD_ERROR)
        }
    }

    override fun readAllTweets(): List<TweetDTO> {
        val tweets = tweetRepository.findAllByStatusNot(TweetStatus.DELETED)
        return tweets.map { TweetDTO.entityToDTO(it)}
    }

    @Transactional
    override fun deleteTweet(userInfo : TwitterUser, request: TweetReqDTO.Req.DeleteTweet): TweetResDTO.Res.TweetInfo {
        val tweet = tweetRepository.findById(request.tweetId)
            .orElseThrow { BaseException(BaseResponseCode.TWEET_NOT_FOUND) }

        if(tweet.userId != userInfo.userId){
            throw BaseException(BaseResponseCode.FORBIDDEN)
        }

        tweet.status = TweetStatus.DELETED
        tweetRepository.save(tweet)
        return TweetResDTO.Res.TweetInfo(tweetId = tweet.tweetId)
    }
}