/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.enumeration;

/**
 * @Description: 支付类型枚举类
 * @Author: estn.zuo
 * @CreateTime: 2015-06-09 23:30
 */
public enum PaymentTypeEnum {

    OFFLINE("线下支付"),

    ALIPAY("支付宝支付"),

    WECHAT("微信支付"),

    BESTPAY("翼支付"),

    BANKCARD("银行卡"),

    E_DEDUCTION("E通币抵扣"),

    ;

    private String description;

    PaymentTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static PaymentTypeEnum getByDescription(String description) {
        for (PaymentTypeEnum paymentTypeEnum : PaymentTypeEnum.values()) {
            if (paymentTypeEnum.getDescription().equals(description)) {
                return paymentTypeEnum;
            }
        }
        throw new IllegalArgumentException(description + "Is not in PaymentTypeEnum");
    }
}
