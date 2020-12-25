package com.demo.rocketmq.consumer.impl;

import com.demo.rocketmq.MqResultvo.MQConsumeResult;
import com.demo.rocketmq.annotation.MQConsumeService;
import com.demo.rocketmq.constants.TopicEnum;
import com.demo.rocketmq.consumer.AbstractMQMsgProcessor;
import lombok.extern.log4j.Log4j2;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author : xh.Z
 * @email : fisherman0510@163.com
 * @Date : 2020/12/25 9:33
 * @description :  顺序消息消费，带事务方式（应用可控制Offset什么时候提交）
 */
@Log4j2
@MQConsumeService(topic = TopicEnum.OrderTopic, tags = {"*"})
public class ConsumerInOrderMsgProcessorImpl extends AbstractMQMsgProcessor {

    Random random = new Random();

    @Override
    protected MQConsumeResult consumeMessage(String tag, List<String> keys, MessageExt messageExt) {
        System.out.println("consumeThread=" + Thread.currentThread().getName() + "queueId=" + messageExt.getQueueId() + ", content:" + new String(messageExt.getBody()));
        MQConsumeResult result = new MQConsumeResult();
        try {
            //模拟业务逻辑处理中...
            TimeUnit.SECONDS.sleep(random.nextInt(10));
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
        }

        return result;
    }
}
