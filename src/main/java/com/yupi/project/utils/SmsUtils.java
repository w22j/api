package com.yupi.project.utils;

import com.tu.apicommon.constant.RedisConstant;
import com.tu.apicommon.model.dto.SmsDTO;
import com.yupi.project.common.RedisTokenBucket;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class SmsUtils {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private RedisTokenBucket redisTokenBucket;

    @Resource RabbitMqUtils rabbitMqUtils;

    public boolean sendMsg(SmsDTO smsDTO) {
        // 从令牌桶中获取令牌，未获取不得发送短信
        boolean acquire = redisTokenBucket.tryAcquire(smsDTO.getPhoneNum());
        if (!acquire) {
            log.info("phoneNum：{}，send SMS frequent", smsDTO.getPhoneNum());
            return false;
        }
        String phoneNum = smsDTO.getPhoneNum();
        String code = smsDTO.getCode();
        // 将手机号对应的验证码存入Redis，方便后续检验
        redisTemplate.opsForValue().set(RedisConstant.SMS_CODE_PREFIX + phoneNum, String.valueOf(code), 5, TimeUnit.MINUTES);
        // 利用消息队列，异步发送短信
        rabbitMqUtils.sendMsgAsync(smsDTO);
        return true;
    }

    public boolean verifyCode(String phoneNum, String code) {
        String key = RedisConstant.SMS_CODE_PREFIX + phoneNum;
        String checkCode = redisTemplate.opsForValue().get(key);
        if (StringUtils.isNotBlank(code) && code.equals(checkCode)) {
            redisTemplate.delete(key);
            return true;
        }
        return false;

    }
}
