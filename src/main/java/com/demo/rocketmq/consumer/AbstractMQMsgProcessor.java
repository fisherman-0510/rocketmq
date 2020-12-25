package com.demo.rocketmq.consumer;


import com.demo.rocketmq.MqResultvo.MQConsumeResult;
import lombok.extern.log4j.Log4j2;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.Arrays;
import java.util.List;

/**
 * @author : xh.Z
 * @Date : 2020/12/22 17:08
 * @description :
 */
@Log4j2
public abstract class AbstractMQMsgProcessor implements MQMsgProcessor {


    @Override
    public MQConsumeResult handle(String topic, String tag, List<MessageExt> msgs) {
        MQConsumeResult mqConsumeResult = new MQConsumeResult();
        /**可以增加一些其他逻辑*/

        for (MessageExt messageExt : msgs) {
            //消费具体的消息，抛出钩子供真正消费该消息的服务调用
            mqConsumeResult = this.consumeMessage(tag, messageExt.getKeys() == null ? null : Arrays.asList(messageExt.getKeys().split(MessageConst.KEY_SEPARATOR)), messageExt);
        }

        /**可以增加一些其他逻辑*/
        return mqConsumeResult;
    }

    /**
     * 消息某条消息
     *
     * @param tag        标签
     * @param keys       消息关键字
     * @param messageExt
     * @return
     */
    protected abstract MQConsumeResult consumeMessage(String tag, List<String> keys, MessageExt messageExt);

}
