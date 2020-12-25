package com.demo.rocketmq.constants;

/**
 * @author : xh.Z
 * @Date : 2020/12/24 14:56
 * @description :  消息主题 枚举
 */
public enum TopicEnum {
    DemoTopic("DemoTopic", "示例主题"),

    AsyncTopic("AsyncTopic", "异步消息"),

    OnewayTopic("OnewayTopic", "单向发送消息"),

    OrderTopic("OrderTopic", "订单顺序消费"),

    /**
     * 大部分场景适用于 订单超时未支付变更订单状态
     */
    ScheduledTopic("ScheduledTopic", "延时消息"),

    /**
     * 批量发送消息能显著提高传递小消息的性能。
     * 限制是这些批量消息应该有相同的topic，相同的waitStoreMsgOK，而且不能是延时消息。
     * 此外，这一批消息的总大小不应超过4MB。
     */
    BatchTopic("BatchTopic", "批量消息"),


    ;

    private String code;
    private String msg;

    private TopicEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

}