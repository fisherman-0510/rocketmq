package com.demo.rocketmq.config;

import com.demo.rocketmq.listener.MQConsumeMsgListenerProcessor;
import lombok.extern.log4j.Log4j2;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @author : xh.Z
 * @Date : 2020/12/22 17:08
 * @description :
 */
@Configuration
@Log4j2
public class RocketmqConsumerConfig {

    @Value("${rocketmq.groupName}")
    private String groupName;

    @Value("${rocketmq.namesrvAddr}")
    private String namesrvAddr;

    @Value("${rocketmq.consumer.consumeThreadMin}")
    private int consumeThreadMin;
    @Value("${rocketmq.consumer.consumeThreadMax}")
    private int consumeThreadMax;
    @Value("${rocketmq.consumer.topics}")
    private String topics;
    @Value("${rocketmq.consumer.consumeMessageBatchMaxSize}")
    private int consumeMessageBatchMaxSize;
    @Autowired
    private MQConsumeMsgListenerProcessor mqMessageListenerProcessor;


    @Bean
    public DefaultMQPushConsumer getRocketMQConsumer() {
        if (!StringUtils.hasLength(groupName)) {
            throw new RuntimeException("groupName is null !!!");
        }
        if (!StringUtils.hasLength(namesrvAddr)) {
            throw new RuntimeException("namesrvAddr is null !!!");
        }
        if (!StringUtils.hasLength(topics)) {
            throw new RuntimeException("topics is null !!!");
        }
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setConsumeThreadMin(consumeThreadMin);
        consumer.setConsumeThreadMax(consumeThreadMax);
        consumer.registerMessageListener(mqMessageListenerProcessor);

        /**
         * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
         * 如果非第一次启动，那么按照上次消费的位置继续消费
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        /**
         * Consumer消费者有2种消费模式
         * 1.2.1 负载均衡模式（默认），MessageModel.CLUSTERING ，消息只能被一个消费者消费。
         * 1.2.2 广播模式，MessageModel.BROADCASTING ，消息可以被多个消费者消费。
         */
        //consumer.setMessageModel(MessageModel.CLUSTERING);
        /**
         * 设置一次消费消息的条数，默认为1条
         */
        consumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);
        try {
            /**
             * 设置该消费者订阅的主题和tag，如果是订阅该主题下的所有tag，则tag使用*；如果需要指定订阅该主题下的某些tag，则使用||分割，例如tag1||tag2||tag3
             */
            String[] topicTagsArr = topics.split(";");
            for (String topicTags : topicTagsArr) {
                String[] topicTag = topicTags.split("~");
                if (topicTag.length == 1) {
                    consumer.subscribe(topicTag[0], "*");
                } else {
                    consumer.subscribe(topicTag[0], topicTag[1]);
                }
            }
            consumer.start();
            log.info("consumer is start !!! groupName:{},topics:{},namesrvAddr:{}", groupName, topics, namesrvAddr);
        } catch (MQClientException e) {
            log.error("consumer is start !!! groupName:{},topics:{},namesrvAddr:{}", groupName, topics, namesrvAddr, e);
            throw new RuntimeException(e);
        }
        return consumer;
    }
}
