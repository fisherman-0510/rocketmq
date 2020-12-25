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
 * @Date : 2020/12/25 14:49
 * @description : 批量消息 消费者实例
 */
@Log4j2
@MQConsumeService(topic = TopicEnum.BatchTopic,tags = {"*"})
public class BatchConsumerMsgProcessImpl extends AbstractMQMsgProcessor {


    @Override
    protected MQConsumeResult consumeMessage(String tag, List<String> keys, MessageExt messageExt) {
        String uniquekey = messageExt.getKeys();// 自定义的唯一key

        String msg = new String(messageExt.getBody());
        log.info("BatchTopic 获取到的消息为：{}", msg);
        MQConsumeResult result = new MQConsumeResult();
        result.setSuccess(true);
        return result;
    }
}
