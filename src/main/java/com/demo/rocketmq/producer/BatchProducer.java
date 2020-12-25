package com.demo.rocketmq.producer;

import com.demo.rocketmq.constants.TopicEnum;
import lombok.extern.log4j.Log4j2;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : xh.Z
 * @email : fisherman0510@163.com
 * @Date : 2020/12/25 14:46
 * @description :  批量消息
 * 批量发送消息能显著提高传递小消息的性能。限制是这些批量消息应该有相同的topic，相同的waitStoreMsgOK，而且不能是延时消息。此外，这一批消息的总大小不应超过4MB。
 * 如果超过4MB 在官方github 上有 解决方式,使用时查看
 */
@Component
@Log4j2
public class BatchProducer {

    /**
     * 使用RocketMq的生产者
     */
    @Autowired
    private DefaultMQProducer defaultMQProducer;

    public void sendMsg() throws UnsupportedEncodingException {

        String topic = TopicEnum.BatchTopic.toString();
        String tags = "Demo";
        List<Message> messages = new ArrayList<>();
        messages.add(new Message(topic, tags, "OrderID001", "Hello world 0".getBytes(RemotingHelper.DEFAULT_CHARSET)));
        messages.add(new Message(topic, tags, "OrderID002", "Hello world 1".getBytes(RemotingHelper.DEFAULT_CHARSET)));
        messages.add(new Message(topic, tags, "OrderID003", "Hello world 2".getBytes(RemotingHelper.DEFAULT_CHARSET)));
        try {
            defaultMQProducer.send(messages);
        } catch (Exception e) {
            e.printStackTrace();
            //处理error
        }

    }
}
