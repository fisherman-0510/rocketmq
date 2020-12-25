package com.demo.rocketmq.consumer;

import com.demo.rocketmq.MqResultvo.MQConsumeResult;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author : xh.Z
 * @Date : 2020/12/22 17:08
 * @description :
 */
public interface MQMsgProcessor {
    /**
     * 消息处理<br/>
     * 如果没有return true ，consumer会重新消费该消息，直到return true<br/>
     * consumer可能重复消费该消息，请在业务端自己做是否重复调用处理，该接口设计为幂等接口
     * @param topic 消息主题
     * @param tag 消息标签
     * @param msgs 消息
     * @return
     */
    MQConsumeResult handle(String topic, String tag, List<MessageExt> msgs);
}