package com.pemass.persist.enumeration;


/**
 * @Description: 订单状态
 * @Author: estn.zuo
 * @CreateTime: 2014-10-13 15:48
 */
public enum OrderStatusEnum {

    //订单状态【0-已确认、1-已取消、2-已失效】
    CONFIRMED("已确认"),

    CANCEL("已取消"),

    INVALID("已失效"),

    RETURNED("已退单");

    private String description;

    OrderStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static OrderStatusEnum getByDescription(String description) {
        for (OrderStatusEnum orderStatusEnum : OrderStatusEnum.values()) {
            if (orderStatusEnum.getDescription().equals(description)) {
                return orderStatusEnum;
            }
        }
        throw new IllegalArgumentException(description + "Is not in OrderStatusEnum");
    }


}
