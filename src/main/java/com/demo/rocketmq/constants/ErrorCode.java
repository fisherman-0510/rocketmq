package com.demo.rocketmq.constants;

import java.io.Serializable;

/**
 * @author : xh.Z
 * @Date : 2020/12/22 16:22
 * @description :
 */
public interface ErrorCode extends Serializable {
    /**
     * 错误码
     *
     * @return
     */
    String getCode();

    /**
     * 错误信息
     *
     * @return
     */
    String getMsg();
}
