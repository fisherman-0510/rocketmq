package com.demo.rocketmq.producer;

import com.demo.rocketmq.constants.TopicEnum;
import lombok.extern.log4j.Log4j2;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : xh.Z
 * @email : fisherman0510@163.com
 * @Date : 2020/12/25 14:15
 * @description :  延迟消息生产者 :  使用场景, 订单的尝试操作
 * 延迟消息的等级
 * messageDelayLevel = "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";
 */
@Log4j2
@Component
public class ScheduledProducer {

    /**
     * 使用RocketMq的生产者
     */
    @Autowired
    private DefaultMQProducer defaultMQProducer;

    public void sendMsg() throws Exception {
        int totalMessagesToSend = 10;
        for (int i = 0; i < totalMessagesToSend; i++) {
            Message sendMsg = new Message(TopicEnum.ScheduledTopic.toString(), "Demo", ("Hello scheduled message " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            // 设置延时等级3,这个消息将在10s之后发送(现在只支持固定的几个时间,详看delayTimeLevel)
            sendMsg.setDelayTimeLevel(3);
            // 发送消息
            defaultMQProducer.send(sendMsg);
        }

    }

}
