/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.enumeration;

/**
 * @Description: 订单类型
 * @Author: estn.zuo
 * @CreateTime: 2015-07-24 09:53
 */
public enum OrderTypeEnum {

    PRODUCT_ORDER("商品订单", 0),

    CUSTOMIZATION_ORDER("自定义订单", 1),

    ONE_PURCHASE_ORDER("一元购订单", 2);

    private String description;
    private int ordinalReplace;

    OrderTypeEnum(String description, int ordinalReplace) {
        this.description = description;
        this.ordinalReplace = ordinalReplace;
    }

    public String getDescription() {
        return description;
    }

    public int getOrdinalReplace() {
        return ordinalReplace;
    }

    public void setOrdinalReplace(int ordinalReplace) {
        this.ordinalReplace = ordinalReplace;
    }

    public static OrderTypeEnum getByDescription(String description) {
        for (OrderTypeEnum orderTypeEnum : OrderTypeEnum.values()) {
            if (orderTypeEnum.getDescription().equals(description)) {
                return orderTypeEnum;
            }
        }
        throw new IllegalArgumentException(description + "Is not in OrderTypeEnum");
    }

}
