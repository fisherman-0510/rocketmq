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
 * @Date : 2020/12/22 17:37
 * @description :  发送同步消息：消息可靠，客户端会阻塞等待MQ服务端收到消息，并返回通知，才执行下一步。保证消息可靠发送。
 */
@Log4j2
@MQConsumeService(topic = TopicEnum.DemoTopic, tags = {"*"})
public class SyncConsumerMsgProcessImpl extends AbstractMQMsgProcessor {

    @Override
    protected MQConsumeResult consumeMessage(String tag, List<String> keys, MessageExt messageExt) {
        String uniquekey = messageExt.getKeys();// 自定义的唯一key
        String msg = new String(messageExt.getBody());
        log.info("获取到的消息为：" + msg);
        MQConsumeResult result = new MQConsumeResult();
//        if (!redisUtil.hasKey(uniquekey)) {
        if (true) {
            // 如果此时的业务逻辑是将收到的消息存放到数据库
            System.out.println("消息id:" + messageExt.getMsgId() + "---" + new String(messageExt.getBody()));
            //TODO 判断该消息是否重复消费（RocketMQ不保证消息不重复，如果你的业务需要保证严格的不重复消息，需要你自己在业务端去重）
            //如果注解中tags数据中包含多个tag或者是全部的tag(*)，则需要根据tag判断是那个业务，
            //如果注解中tags为具体的某个tag，则该服务就是单独针对tag处理的
//        if(tag.equals("BaiHe")){
//            //做某个操作
//            System.out.println("推送消息到白鹤");
//            result.setSuccess(true);
//        }
            //TODO 获取该消息重试次数
            int reconsume = messageExt.getReconsumeTimes();
            //根据消息重试次数判断是否需要继续消费
            if (reconsume == 3) {//消息已经重试了3次，如果不需要再次消费，则返回成功
                log.error("消费三次不成功，消息内容为 ：" + msg);
                result.setErrCode("110");

            }
            // 消息去重 消费成功放入redis
            //redisUtil.set(messageExt.getKeys(), new String(messageExt.getBody()));
            result.setSuccess(true);
            return result;
        } else {
            log.error("重复消费，消息内容为 ：" + msg);
            result.setSuccess(true);
            return result;
        }
    }
}
