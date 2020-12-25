package com.demo.rocketmq.producer;

import com.demo.rocketmq.constants.TopicEnum;
import lombok.extern.log4j.Log4j2;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author : xh.Z
 * @Date : 2020/12/24 10:21
 * @description : 单向发送消息,不关心发送结果,例如：日志发送场景
 */
@Component
@Log4j2
public class OnewayProducer {

    /**
     * 使用RocketMq的生产者
     */
    @Autowired
    private DefaultMQProducer defaultMQProducer;

    public void sendMsg() {
        try {
            for (int i = 0; i < 10; i++) {
                Message sendMsg = new Message(TopicEnum.OnewayTopic.toString(), "Demo", ("HelloDemo" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                sendMsg.setKeys(UUID.randomUUID().toString());
                defaultMQProducer.sendOneway(sendMsg);
                TimeUnit.MILLISECONDS.sleep(1000);
            }
            log.info("OnewayProducer 消息发送完成-------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
