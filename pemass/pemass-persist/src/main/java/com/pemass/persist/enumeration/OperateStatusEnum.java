/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.enumeration;

/**
 * @Description: 操作状态
 * @Author: estn.zuo
 * @CreateTime: 2015-08-13 22:01
 */
public enum OperateStatusEnum {

    NONE("未处理"),
    ING("处理中"),
    SUCCESS("处理成功"),
    FAIL("处理失败"),
    PAUSE("暂停"),
    RESUME("恢复"),;

    private String description;

    OperateStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
