package com.swj9707.twittercloneapiserver.utils

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RedisUtil(
    @Autowired
    private val stringRedisTemplate: StringRedisTemplate
) {
    fun getData(key : String) : String? {
        var valueOperations : ValueOperations<String, String> = stringRedisTemplate.opsForValue()
        return valueOperations.get(key)
    }

    fun setData(key : String, value : String) {
        var valueOperations : ValueOperations<String, String> = stringRedisTemplate.opsForValue()
        valueOperations.set(key, value)
    }

    fun setDataExpire(key : String, value : String, duraion : Long) {
        var valueOperations : ValueOperations<String, String> = stringRedisTemplate.opsForValue()
        val expireDuration : Duration = Duration.ofSeconds(duraion)
        valueOperations.set(key, value, expireDuration)
    }

    fun deleteData(key : String){
        stringRedisTemplate.delete(key)
    }
}