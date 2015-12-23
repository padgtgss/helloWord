/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.enumeration;

/**
 * @Description: 意见反馈类型
 * @Author: estn.zuo
 * @CreateTime: 2014-11-16 11:16
 */
public enum FeedbackTypeEnum {

    C("C 端用户意见反馈"),
    B("B 端用户意见反馈");

    private String description;

    FeedbackTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
