package com.swj9707.twittercloneapiserver.v1.tweet.dto

import com.swj9707.twittercloneapiserver.constant.dto.ImageDTO
import com.swj9707.twittercloneapiserver.constant.enum.TweetStatus
import com.swj9707.twittercloneapiserver.v1.tweet.entity.Like
import com.swj9707.twittercloneapiserver.v1.tweet.entity.ReTweet
import com.swj9707.twittercloneapiserver.v1.tweet.entity.Tweet
import com.swj9707.twittercloneapiserver.v1.tweet.repository.projection.TweetProjection
import com.swj9707.twittercloneapiserver.v1.user.dto.UserDTO
import org.springframework.data.domain.Slice
import java.util.*
import kotlin.collections.ArrayList

class TweetDTO {
    companion object Dto {

        data class TweetDTO(
            val tweetId : Long?,

            var tweetContent : String,

            var images : MutableList<ImageDTO.Dto.ImageInfo>?,

            var modified : Boolean,

            var createdAt : String,

            var status : TweetStatus,

            var userInfo: UserDTO.Dto.TweetOwnerInfo,

        )
        data class TweetInfo(
            val tweetId : Long?,

            var tweetContent : String,

            var images : MutableList<ImageDTO.Dto.ImageInfo>?,

            var modified : Boolean,

            var createdAt : String,

            var status : TweetStatus,

            var userInfo: UserDTO.Dto.TweetOwnerInfo,

            var likedTweetsCount : Int = 0,

            var retweetsCount : Int = 0,

            var repliesCount : Int = 0
        ) {
            companion object Util {

                fun entityToDTO(tweet : Tweet) : TweetInfo {
                    return TweetInfo(
                        tweetId = tweet.tweetId,
                        tweetContent = tweet.tweetContent,
                        images = ImageDTO.Dto.ImageInfo.entitiesToListDTO(tweet.images),
                        modified = tweet.modified,
                        createdAt = tweet.createAt.toString(),
                        status = tweet.status,
                        userInfo = UserDTO.Dto.TweetOwnerInfo.entityToDTO(tweet.user),
                        likedTweetsCount = tweet.likes?.size ?: 0,
                        retweetsCount = tweet.retweets?.size ?: 0,
                        repliesCount = tweet.replyTweets?.size ?: 0
                    )
                }
                fun projectionToDTO( projection: TweetProjection) : TweetInfo {
                    return TweetInfo(
                        tweetId = projection.getTweetId(),
                        tweetContent = projection.getTweetContent(),
                        images = ImageDTO.Dto.ImageInfo.projectionsToListDTO(projection.getImages()),
                        modified = projection.getModified(),
                        createdAt = projection.getCreateAt().toString(),
                        status = projection.getStatus(),
                        userInfo = UserDTO.Dto.TweetOwnerInfo.projectionToDTO(projection.getUser()),
                        likedTweetsCount = projection.getLikedTweetsCount(),
                        retweetsCount = projection.getRetweetsCount(),
                        repliesCount = projection.getRepliesCount()
                    )
                }

                fun toPageableDTO(pageEntity : Slice<TweetProjection>) : Slice<TweetInfo> {
                    return pageEntity.map { projectionToDTO(it) }
                }
            }
        }

        data class RetweetInfo (
            val id : Long,
            val userId : UUID,
            val tweetId : Long
        ) {
            companion object Util {
                fun entityToDTO(retweet : ReTweet) : RetweetInfo {
                    return RetweetInfo(
                        id = retweet.retweetId,
                        userId =  retweet.user.userId,
                        tweetId = retweet.tweet.tweetId
                    )
                }
                fun getRetweetInfo(tweet : Tweet) : List<RetweetInfo> {
                    return tweet.retweets?.map { entityToDTO(it) } ?: ArrayList()
                }
            }
        }

        data class LikeInfo (
            val id : Long,
            val userId : UUID,
            val tweetId : Long
        ) {
            companion object Util {
                fun entityToDTO(like : Like) : LikeInfo {
                    return LikeInfo(
                        id = like.likeId,
                        userId = like.user.userId,
                        tweetId = like.tweet.tweetId
                    )
                }
                fun getLikeInfo(tweet : Tweet) : List<LikeInfo> {
                    return tweet.likes?.map { entityToDTO(it) } ?: ArrayList()
                }
            }
        }
    }
}



