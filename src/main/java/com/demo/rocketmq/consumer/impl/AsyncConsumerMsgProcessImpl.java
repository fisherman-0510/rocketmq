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
 * @Date : 2020/12/24 14:56
 * @description :  异步消息的mq 处理
 */
@Log4j2
@MQConsumeService(topic = TopicEnum.AsyncTopic, tags = {"*"})
public class AsyncConsumerMsgProcessImpl extends AbstractMQMsgProcessor {

    /**
     *  处理某条消息
     * @param tag        标签
     * @param keys       消息关键字
     * @param messageExt
     * @return
     */
    @Override
    protected MQConsumeResult consumeMessage(String tag, List<String> keys, MessageExt messageExt) {
        String uniquekey = messageExt.getKeys();// 自定义的唯一key

        String msg = new String(messageExt.getBody());
        log.info("AsyncListenerProcessor 获取到的消息为：{}", msg);
        MQConsumeResult result = new MQConsumeResult();
        result.setSuccess(true);
        return result;
    }
}
