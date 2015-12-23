/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.enumeration;

/**
 * @Description: 支付状态
 * @Author: estn.zuo
 * @CreateTime: 2014-11-16 14:21
 */
public enum PayStatusEnum {

    NONE_PAY("未支付"),

    HAS_PAY("已支付"),

    HAS_REFUND("已退款");

    private String description;

    PayStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static PayStatusEnum getByDescription(String description) {
        for (PayStatusEnum payStatusEnum : PayStatusEnum.values()) {
            if (payStatusEnum.getDescription().equals(description)) {
                return payStatusEnum;
            }
        }
        throw new IllegalArgumentException(description + "Is not in PayStatusEnum");
    }

    }
