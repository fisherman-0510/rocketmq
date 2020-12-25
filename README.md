# RocketMQ-Demo

#### 介绍
Springboot整合RocketMQ 拆箱即用



#### 使用说明

1.  默认启动端口号8081
2.  可以配合 rocketmq 的可视化界面 https://github.com/apache/rocketmq-externals
3.  前置条件 开启 namesrv  ./bin/mqnamesrv.cmd
4.  前置条件 开启 broker   ./bin/mqbroker.cmd -n localhost:9876 autoCreateTopicEnable=true

#### 核心包

1.  annotation包: MQConsumeService注解用于方便扩展消费者的使用
2.  config包: 配置了 生产者/消费者的部分配置
3.  constants: 主要配置了常量文件
4.  consumer: 消费者的消费样例代码
5.  exception: MQ异常的捕获(未开发完)
6.  listener: 核心注册监听器,用于分发 topic 和tag的逻辑执行
7.  mqresultvo: vo类
8.  order: 用于创建订单顺序消费测试实体类
9.  producer: 生产者实例

#### 参考文献

1. 官方文档:  https://rocketmq.apache.org/docs/quick-start/
2. 可视化界面:  https://github.com/apache/rocketmq-externals
3. 码云代码同步: https://gitee.com/fisherman0510/rocket-mq-demo


