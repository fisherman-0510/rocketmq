package com.demo.rocketmq.producer;

import com.demo.rocketmq.constants.TopicEnum;
import lombok.extern.log4j.Log4j2;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author : xh.Z
 * @Date : 2020/12/24 10:21
 * @description : 发送异步消息 发送端不等待broker的响应
 */
@Component
@Log4j2
public class AsyncProducer {

    /**
     * 使用RocketMq的生产者
     */
    @Autowired
    private DefaultMQProducer defaultMQProducer;

    public void sendMsg() {
        try {
            for (int i = 0; i < 10; i++) {
                Message sendMsg = new Message(TopicEnum.AsyncTopic.toString(), "Demo", ("HelloDemo" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                sendMsg.setKeys(UUID.randomUUID().toString());
                defaultMQProducer.send(sendMsg, new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        // 发送成功 回调函数
                        log.info("AsyncProducer 发送消息成功回调函数: {}", sendResult.toString());
                    }

                    @Override
                    public void onException(Throwable e) {
                        // 发送失败 回调函数
                        log.error("AsyncProducer 消息发送失败: {}", e.getMessage());
                    }

                }, 1);
                TimeUnit.MILLISECONDS.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
