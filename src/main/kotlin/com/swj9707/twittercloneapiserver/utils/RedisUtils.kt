package com.swj9707.twittercloneapiserver.utils

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class RedisUtils(
    private val stringRedisTemplate: StringRedisTemplate
) {
    fun getData(key: String): String? {
        var valueOperations: ValueOperations<String, String> = stringRedisTemplate.opsForValue()
        return valueOperations.get(key)
    }

    fun setDataExpire(key: String, value: String, duration: Long) {
        var valueOperations: ValueOperations<String, String> = stringRedisTemplate.opsForValue()
        valueOperations.set(key, value, duration, TimeUnit.MILLISECONDS)
    }

    fun deleteData(key: String) {
        stringRedisTemplate.delete(key)
    }
}