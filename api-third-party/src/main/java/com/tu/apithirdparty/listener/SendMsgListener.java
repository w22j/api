package com.tu.apithirdparty.listener;

import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rabbitmq.client.Channel;
import com.tencentcloudapi.tci.v20190318.models.Person;
import com.tu.apicommon.constant.RabbitMqConstant;
import com.tu.apicommon.constant.RedisConstant;
import com.tu.apicommon.model.dto.SmsDTO;
import com.tu.apithirdparty.utils.SendSmsUtilsByAli;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class SendMsgListener {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

//    @Resource
//    private SendSmsUtils sendSmsUtils;

    @Resource
    private SendSmsUtilsByAli sendSmsUtilsByAli;

    /**
     * 监听发送短信普通队列
     * @param smsDTO
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitMqConstant.SMS_QUEUE_NAME, durable = "true"),
            exchange = @Exchange(value = RabbitMqConstant.SMS_EXCHANGE_TOPIC_NAME, type = ExchangeTypes.TOPIC),
            key = RabbitMqConstant.SMS_EXCHANGE_ROUTING_KEY,
            arguments = {@Argument(name = "x-dead-letter-exchange", value = RabbitMqConstant.SMS_EXCHANGE_DIRECT_NAME),
                    @Argument(name = "x-dead-letter-routing-key", value = RabbitMqConstant.SMS_DELAY_EXCHANGE_ROUTING_KEY)}
    ))
    public void sendMsgListener(SmsDTO smsDTO, Message message, Channel channel) throws IOException {
        String messageId = message.getMessageProperties().getMessageId();
        int retryCount = (int) redisTemplate.opsForHash().get(RedisConstant.SMS_MESSAGE_PREFIX + messageId, "retryCount");
        // 如果重试次数大于3次，直接放到死信队列中
        if (retryCount >= 3) {
            log.error("短信消息重试超过3次：{}", messageId);
            // basicReject方法拒绝deliveryTag对应的消息,getDeliveryTag(): 获取当前消息的投递标识,
            // 第二个参数是否requeue，true则重新入队列，否则丢弃或者进入死信队列。
            // 该方法reject后，该消费者还是会消费到该条被reject的消息。
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            redisTemplate.delete(RedisConstant.SMS_MESSAGE_PREFIX + messageId);
            return;
        }
        try {
            String phoneNum = smsDTO.getPhoneNum();
            String code = smsDTO.getCode();
            if (StringUtils.isAnyBlank(phoneNum, code)) {
                throw new RuntimeException("sendMsgListener参数为空");
            }
            // TODO 调用第三方服务发送短信 (未使用腾讯云，签名模板认证一直不通过可使用阿里云的测试模板）
            SendSmsResponse sendSmsResponse = sendSmsUtilsByAli.sendSmsResponse(phoneNum, code);
            String statusCode = sendSmsResponse.getBody().getCode();
            String messageByAli = sendSmsResponse.getBody().getMessage();
            if (!"OK".equals(statusCode) || !"OK".equals(messageByAli)) {
                throw new RuntimeException("发送验证码失败");
            }
            // 手动确认消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            log.info("短信发送成功：{}",smsDTO);
            redisTemplate.delete(RedisConstant.SMS_MESSAGE_PREFIX + messageId);
        } catch (Exception e) {
            redisTemplate.opsForHash().put(RedisConstant.SMS_MESSAGE_PREFIX + messageId, "retryCount", retryCount + 1);
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }

    /**
     * 监听到发送短信死信队列
     * @param smsDTO
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitMqConstant.SMS_DELAY_QUEUE_NAME, durable = "true"),
            exchange = @Exchange(value = RabbitMqConstant.SMS_EXCHANGE_DIRECT_NAME),
            key = RabbitMqConstant.SMS_DELAY_EXCHANGE_ROUTING_KEY
    ))
    public void smsDelayQueueListener(SmsDTO smsDTO, Message message, Channel channel) throws IOException {
        try {
            log.error("监听到死信队列消息==>{}",smsDTO);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }
    }

}
