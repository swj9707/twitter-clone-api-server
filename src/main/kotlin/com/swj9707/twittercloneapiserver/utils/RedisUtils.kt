package com.swj9707.twittercloneapiserver.utils

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RedisUtils(
    @Autowired private val stringRedisTemplate: StringRedisTemplate
) {
    fun getData(key: String): String? {
        var valueOperations: ValueOperations<String, String> = stringRedisTemplate.opsForValue()
        return valueOperations.get(key)
    }

    fun setDataExpire(key: String, value: String, duration: Long) {
        var valueOperations: ValueOperations<String, String> = stringRedisTemplate.opsForValue()
        val expireDuration: Duration = Duration.ofMillis(duration)
        valueOperations.set(key, value, expireDuration)
    }

    fun deleteData(key: String) {
        stringRedisTemplate.delete(key)
    }
}