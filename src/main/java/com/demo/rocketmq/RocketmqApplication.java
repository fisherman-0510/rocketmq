package com.demo.rocketmq;

import com.demo.rocketmq.producer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping("/mq")
public class RocketmqApplication {

    public static void main(String[] args) {
        SpringApplication.run(RocketmqApplication.class, args);
    }

    @Autowired
    SyncProducer syncProducer;

    /**
     * 同步消息发送
     *
     * @return
     */
    @GetMapping("/push2")
    public String push2() {
        syncProducer.sendMsg();
        return "SUCCESS";
    }

    @Autowired
    AsyncProducer asyncProducer;

    /**
     * 发送异步消息 发送端不等待broker的响应
     *
     * @return
     */
    @GetMapping("/asyncPush")
    public String asyncPush() {
        asyncProducer.sendMsg();
        return "SUCCESS";
    }

    @Autowired
    OnewayProducer onewayProducer;

    /**
     * 发送单向消息 如 日志等操作
     *
     * @return
     */
    @GetMapping("/onewayPush")
    public String onewayPush() {
        onewayProducer.sendMsg();
        return "SUCCESS";
    }

    @Autowired
    OrderProducer orderProducer;

    /**
     * 订单的  顺序消息消费者
     *
     * @return
     */
    @GetMapping("/orderProducer")
    public String orderProducer() throws Exception {
        orderProducer.sendMsg();
        return "SUCCESS";
    }

    @Autowired
    ScheduledProducer scheduledProducer;

    /**
     * 订单的  超时关闭操作
     * 延迟消息 发送
     *
     * @return
     */
    @GetMapping("/scheduledProducer")
    public String scheduledProducer() throws Exception {
        scheduledProducer.sendMsg();
        return "SUCCESS";
    }

    @Autowired
    BatchProducer batchProducer;

    /**
     * 批量消息发送
     *
     * @return
     */
    @GetMapping("/batchProducer")
    public String batchProducer() throws Exception {
        batchProducer.sendMsg();
        return "SUCCESS";
    }

}
