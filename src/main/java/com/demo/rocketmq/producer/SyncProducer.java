package com.demo.rocketmq.producer;

import com.demo.rocketmq.constants.TopicEnum;
import lombok.extern.log4j.Log4j2;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author : xh.Z
 * @Date : 2020/12/22 16:01
 * @description :  消息生产者-发送 普通同步消息
 */
@Log4j2
@Component
public class SyncProducer {

    /**
     * 使用RocketMq的生产者
     */
    @Autowired
    private DefaultMQProducer defaultMQProducer;

    public String sendMsg() {
        SendResult result = null;
        try {
            for (int i = 0; i < 10; i++) {
                Message sendMsg = new Message(TopicEnum.DemoTopic.toString(), "Demo", ("HelloDemo" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                sendMsg.setKeys(UUID.randomUUID().toString());
                result = defaultMQProducer.send(sendMsg, new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                        long demoId = Long.parseLong(o.toString());
                        long index = demoId % list.size();
                        return list.get((int) index);
                    }
                }, 1);
                log.info("发送结果 {}", result.toString());
                TimeUnit.MILLISECONDS.sleep(10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("----------------------消息发送完成----------------------");
        return result.toString();
    }

}
