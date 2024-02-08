package com.yupi.project.common;

import com.tu.apicommon.constant.RedisConstant;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class RedisTokenBucket {

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    /**
     *  过期时间，400秒后过期
     */
    private final long EXPIRE_TIME = 400;

    /**
     * 令牌桶算法，一分钟以内，每个手机号只能发送一次
     * @param phoneNum
     * @return
     */
    public boolean tryAcquire(String phoneNum) {
        // 每个手机号码一分钟内只能发送一条短信
        int permitsPerMinute = 1;
        // 令牌桶容量
        int maxPermits = 1;
        // 获取当前时间
        long now = System.currentTimeMillis();
        // 计算令牌桶中令牌数
        String key = RedisConstant.SMS_BUCKET_PREFIX + phoneNum;
        int tokens = Integer.parseInt(redisTemplate.opsForValue().get(key + "_tokens") == null ? "0" :  redisTemplate.opsForValue().get(key + "_tokens"));
        // 计算上次补充令牌的时间
        long lastRefillTime = Long.parseLong(redisTemplate.opsForValue().get(key + "_last_refill_time") == null ? "0" : redisTemplate.opsForValue().get(key + "_last_refill_time"));
        // 计算当前时间与上次补充的时间差
        long timeSinceLast = now - lastRefillTime;
        // 计算需要补充的令牌数
        int refill = (int)(timeSinceLast / 1000) * permitsPerMinute / 60;
        // 更新令牌桶内令牌数
        tokens = Math.min(tokens + refill, maxPermits);
        // 更新令牌补充时间
        redisTemplate.opsForValue().set(key + "_last_refill_time", String.valueOf(now), EXPIRE_TIME, TimeUnit.SECONDS);
        // 如果令牌数大于等于1，则获取令牌
        if (tokens >= 1) {
            tokens--;
            redisTemplate.opsForValue().set(key + "_tokens", String.valueOf(tokens), EXPIRE_TIME, TimeUnit.SECONDS);
            // 如果获取到令牌，则返回true
            return true;
        }
        // 如果没有获取到令牌，则返回false
        return false;
    }
}
