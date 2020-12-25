package com.demo.rocketmq.listener;

import com.alibaba.fastjson.JSON;
import com.demo.rocketmq.MqResultvo.MQConsumeResult;
import com.demo.rocketmq.annotation.MQConsumeService;
import com.demo.rocketmq.constants.RocketMQErrorEnum;
import com.demo.rocketmq.exception.AppException;
import com.demo.rocketmq.exception.RocketMQException;
import com.demo.rocketmq.consumer.MQMsgProcessor;
import lombok.extern.log4j.Log4j2;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : xh.Z
 * @Date : 2020/12/22 17:18
 * @description :
 */
@Component
@Log4j2
public class MQConsumeMsgListenerProcessor implements MessageListenerOrderly {

    @Autowired
    private Map<String, MQMsgProcessor> mqMsgProcessorServiceMap;

    /**
     * 默认msgs里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息<br/>
     * 不要抛异常，如果没有return CONSUME_SUCCESS ，consumer会重新消费该消息，直到return CONSUME_SUCCESS
     */
    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {

        if (CollectionUtils.isEmpty(msgs)) {
            log.info("接受到的消息为空，不处理，直接返回成功");
            return ConsumeOrderlyStatus.SUCCESS;
        }
        ConsumeOrderlyStatus consumeOrderlyStatus = ConsumeOrderlyStatus.SUCCESS;
        try {
            //根据Topic分组
            Map<String, List<MessageExt>> topicGroups = msgs.stream().collect(Collectors.groupingBy(MessageExt::getTopic));
            for (Map.Entry<String, List<MessageExt>> topicEntry : topicGroups.entrySet()) {
                String topic = topicEntry.getKey();
                //根据tags分组
                Map<String, List<MessageExt>> tagGroups = topicEntry.getValue().stream().collect(Collectors.groupingBy(MessageExt::getTags));
                for (Map.Entry<String, List<MessageExt>> tagEntry : tagGroups.entrySet()) {
                    String tag = tagEntry.getKey();
                    //消费某个主题下，tag的消息
                    this.consumeMsgForTag(topic, tag, tagEntry.getValue());
                }
            }
        } catch (Exception e) {
            log.error("处理消息失败", e);
            if (e instanceof AppException) {
                AppException mqe = (AppException) e;
                //TODO 记录消费失败日志
                if (mqe.getErrCode().getCode().equals("MQ_110")) {
                    System.out.println("错误信息:" + mqe.getErrMsg());
                    consumeOrderlyStatus = ConsumeOrderlyStatus.SUCCESS;
                } else {
                    consumeOrderlyStatus = ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }
            } else {
                consumeOrderlyStatus = ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
            }
        }
        // 如果没有return success ，consumer会重新消费该消息，直到return success
        return consumeOrderlyStatus;
    }


    private void consumeMsgForTag(String topic, String tag, List<MessageExt> value) {
        //根据topic 和  tag查询具体的消费服务
        MQMsgProcessor imqMsgProcessor = selectConsumeService(topic, tag);
        try {
            if (imqMsgProcessor == null) {
                log.error(String.format("根据Topic：%s和Tag:%s 没有找到对应的处理消息的服务", topic, tag));
                throw new RocketMQException(RocketMQErrorEnum.NOT_FOUND_CONSUMESERVICE);
            }
            //log.info(String.format("根据Topic：%s和Tag:%s 路由到的服务为:%s，开始调用处理消息", topic, tag, imqMsgProcessor.getClass().getName()));
            //调用该类的方法,处理消息
            MQConsumeResult mqConsumeResult = imqMsgProcessor.handle(topic, tag, value);
            if (mqConsumeResult == null) {
                throw new RocketMQException(RocketMQErrorEnum.HANDLE_RESULT_NULL);
            }
            if (mqConsumeResult.getErrCode() != null) {
                throw new RocketMQException(RocketMQErrorEnum.HANDLE_TIMES_OUT);
            }
            if (mqConsumeResult.isSuccess()) {
                //log.info("消息处理成功：" + JSON.toJSONString(mqConsumeResult));
            } else {
                throw new RocketMQException(RocketMQErrorEnum.CONSUME_FAIL, JSON.toJSONString(mqConsumeResult), false);
            }
            if (mqConsumeResult.isSaveConsumeLog()) {
                log.debug("开始记录消费日志");
                //TODO 记录消费日志
            }
        } catch (Exception e) {
            if (e instanceof AppException) {
                AppException mqe = (AppException) e;
                //TODO 记录消费失败日志
                throw new AppException(mqe.getErrCode(), mqe.getErrMsg(), false);
            } else {
                //TODO 记录消费失败日志
                throw e;
            }
        }
    }

    private MQMsgProcessor selectConsumeService(String topic, String tag) {
        MQMsgProcessor imqMsgProcessor = null;
        for (Map.Entry<String, MQMsgProcessor> entry : mqMsgProcessorServiceMap.entrySet()) {
            //获取service实现类上注解的topic和tags
            MQConsumeService consumeService = entry.getValue().getClass().getAnnotation(MQConsumeService.class);
            if (consumeService == null) {
                log.error("消费者服务：" + entry.getValue().getClass().getName() + "上没有添加MQConsumeService注解");
                continue;
            }
            String annotationTopic = consumeService.topic().getCode();
            if (!annotationTopic.equals(topic)) {
                continue;
            }
            String[] tagsArr = consumeService.tags();
            //"*"号表示订阅该主题下所有的tag
            if (tagsArr[0].equals("*")) {
                //获取该实例
                imqMsgProcessor = entry.getValue();
                break;
            }
            boolean isContains = Arrays.asList(tagsArr).contains(tag);
            if (isContains) {
                //获取该实例
                imqMsgProcessor = entry.getValue();
                break;
            }
        }
        return imqMsgProcessor;
    }
}
