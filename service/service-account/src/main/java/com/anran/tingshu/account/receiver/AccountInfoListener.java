package com.anran.tingshu.account.receiver;

import com.anran.tingshu.account.service.MqOpsService;
import com.anran.tingshu.common.execption.BusinessException;
import com.anran.tingshu.common.rabbit.constant.MqConst;
import com.anran.tingshu.common.util.MD5;
import com.rabbitmq.client.Channel;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;



@Slf4j
@Component
public class AccountInfoListener {

    @Resource
    private MqOpsService mqOpsService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @SneakyThrows // 可以绕开编译时候的异常，运行时的异常会正常抛出
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_USER_REGISTER),
            exchange = @Exchange(value = MqConst.EXCHANGE_USER),
            key = MqConst.ROUTING_USER_REGISTER))
    private void listenUserAccountRegister(String content, Message message, Channel channel) {
        // 1. 判断消息是否存在
        if (StringUtils.isEmpty(content)) {
            return; // 不用消费
        }
        // 2. 处理消息
        String msgMD5 = MD5.encrypt(content);
        // 3. 消费消息
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            mqOpsService.userAccountRegister(content);
            // 4. 手动应答消息（将消息从消息队列中删除）
            channel.basicAck(deliveryTag, false);
        } catch (BusinessException e) {
            String msgRetryKey = "msg:retry:" + msgMD5;
            Long count = redisTemplate.opsForValue().increment(msgRetryKey);

            // 三次重试
            if(count >= 3) {
                log.error("消息已经重试{}次，请人工排查错误原因:{}", count,e.getMessage());
                // 不能重试
                channel.basicNack(deliveryTag, false, false);
                redisTemplate.delete(msgRetryKey);
            } else {
                log.info("消息已经重试{}次", count);
                channel.basicNack(deliveryTag, false, true);
            }
        } catch (Exception e) {
            log.error("签收消息时候，网络出现了故障，异常原因：{}", e.getMessage());
            channel.basicNack(deliveryTag, false, false);
        }
    }
}
