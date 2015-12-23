package com.pemass.persist.enumeration;


/**
 * @Description: 订单项状态
 * @Author: estn.zuo
 * @CreateTime: 2014-10-13 15:48
 */
public enum OrderItemStatusEnum {
//订单项状态【0-已确认、1-已取消、2-已失效】

    UNDRAW("未出票"),
    UNUSED("未使用"),
    USED("已使用"),
    OUT_OF_DATE("已过期"),
    RETURNED("已退货"),
    IS_GIVING("已赠送");

    private String description;

    OrderItemStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static OrderItemStatusEnum getByDescription(String description) {
        for (OrderItemStatusEnum orderStatusEnum : OrderItemStatusEnum.values()) {
            if (orderStatusEnum.getDescription().equals(description)) {
                return orderStatusEnum;
            }
        }
        throw new IllegalArgumentException(description + "Is not in OrderItemStatusEnum");
    }


}
