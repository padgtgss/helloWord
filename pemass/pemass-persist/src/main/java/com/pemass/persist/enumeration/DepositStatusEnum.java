/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.enumeration;

/**
 * @Description: UserExpresspayEnum
 * @Author: lin.shi
 * @CreateTime: 2015-08-26 16:11
 */
public enum DepositStatusEnum {


    OPERATEING("处理中"),
    SUCCEED_OPERATE("圈存成功"),
    FAILURE_OPERATE("圈存失败");


    private String description;

    DepositStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}