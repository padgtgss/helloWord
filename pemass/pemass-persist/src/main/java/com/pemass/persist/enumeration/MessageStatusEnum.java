/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.enumeration;

/**
 * @Description: 消息状态
 * @Author: estn.zuo
 * @CreateTime: 2014-12-17 15:19
 */
public enum MessageStatusEnum {
    /**
     * 未推送
     */
    NONE_PUSH("未推送"),

    /**
     * 已发送到JMS队列
     */
    HAS_TO_JMS("已发送到JMS队列"),

    /**
     * 已推送
     */
    HAS_PUSH("已推送");

    private String description;

    MessageStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
