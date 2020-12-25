package com.demo.rocketmq.consumer.impl;

import com.demo.rocketmq.MqResultvo.MQConsumeResult;
import com.demo.rocketmq.annotation.MQConsumeService;
import com.demo.rocketmq.constants.TopicEnum;
import com.demo.rocketmq.consumer.AbstractMQMsgProcessor;
import lombok.extern.log4j.Log4j2;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author : xh.Z
 * @email : fisherman0510@163.com
 * @Date : 2020/12/25 14:13
 * @description : 延时消息 消费者
 */
@Log4j2
@MQConsumeService(topic = TopicEnum.ScheduledTopic, tags = {"*"})
public class ScheduledConsumerMsgProcessImpl extends AbstractMQMsgProcessor {

    @Override
    protected MQConsumeResult consumeMessage(String tag, List<String> keys, MessageExt messageExt) {
        System.out.println("Receive message[msgId=" + messageExt.getMsgId() + "] " + (System.currentTimeMillis() - messageExt.getStoreTimestamp()) + "ms later");
        MQConsumeResult result = new MQConsumeResult();
        result.setSuccess(true);
        return result;
    }


}
