package com.demo.rocketmq.annotation;

import com.demo.rocketmq.constants.TopicEnum;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * @author : xh.Z
 * @Date : 2020/12/22 17:34
 * @description :
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Service
public @interface MQConsumeService {
    /**
     * 消息主题
     */
    TopicEnum topic();

    /**
     * 消息标签,如果是该主题下所有的标签，使用“*”
     */
    String[] tags();


}