package com.demo.rocketmq.producer;

import com.demo.rocketmq.constants.TopicEnum;
import com.demo.rocketmq.order.OrderStep;
import lombok.extern.log4j.Log4j2;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author : xh.Z
 * @email : fisherman0510@163.com
 * @Date : 2020/12/24 17:24
 * @description :  订单的  顺序消息生产者
 * 一个线程消费一个queue, 保证消息被顺序消费
 * 一个订单的顺序流程是：创建、付款、推送、完成。
 */
@Component
@Log4j2
public class OrderProducer {
    /**
     * 使用RocketMq的生产者
     */
    @Autowired
    private DefaultMQProducer defaultMQProducer;

    public void sendMsg() throws Exception {

        // 订单列表
        List<OrderStep> orderList = new OrderStep().buildOrders();

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);

        // 方式一
//        orderList.forEach(orderStep -> {
//            Message sendMsg = new Message(TopicEnum.OrderTopic.toString(), "Demo", orderStep.toString().getBytes());
//            //发送顺序消息
//            /**
//             * 参数1：消息
//             * 参数2:SelectMessageQueueByHash 队列queue选择器，通过哈希。
//             * 参数3：订单ID,用于哈希计算。使得同一个订单的消息发送给同一个queue，保证消息顺序发送
//             */
//            SendResult result = defaultMQProducer.send(sendMsg, new SelectMessageQueueByHash(), orderStep.getOrderId());
//            System.out.println(result);
//        });

        // 方式2  官方示例
        for (int i = 0; i < 10; i++) {
            // 加个时间前缀
            String body = dateStr + " Hello RocketMQ " + orderList.get(i);
            Message sendMsg = new Message(TopicEnum.OrderTopic.toString(), "Demo", "KEY" + i, body.getBytes());

            SendResult sendResult = defaultMQProducer.send(sendMsg, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    Long id = (Long) arg;  //根据订单id选择发送queue
                    long index = id % mqs.size();
                    return mqs.get((int) index);
                }
            }, orderList.get(i).getOrderId());//订单id

            System.out.println(String.format("SendResult status:%s, queueId:%d, body:%s",
                    sendResult.getSendStatus(),
                    sendResult.getMessageQueue().getQueueId(),
                    body));
        }

    }
}
