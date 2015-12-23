/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.enumeration;

/**
 * @Description: 清分来源
 * @Author: estn.zuo
 * @CreateTime: 2015-08-13 21:23
 */
public enum ClearSourceEnum {

    /**
     * 商品订单
     * <p/>
     * Clearing.sourceTargetId记录:Order.id
     */
    PRODUCT_ORDER("商品订单"),

    /**
     * 自定义订单
     * <p/>
     * Clearing.sourceTargetId记录:Order.id
     */
    CUSTOMIZATION_ORDER("自定义订单"),

    /**
     * 一元购订单
     * <p/>
     * Clearing.sourceTargetId记录:Order.id
     */
    ONE_PURCHASE_ORDER("一元购订单"),

    /**
     * 积分认购
     * <p/>
     * Clearing.sourceTargetId记录:PointPurchase.id
     */
    POINT_PURCHASE("积分认购"),

    /**
     * 兑换E通币
     * <p/>
     * Clearing.sourceTargetId记录:Order.id
     */
    CONVERT_POINT_E("兑换E通币"),

    /**
     * 手续费
     * <p/>
     * Clearing.sourceTargetId记录:Transaction id
     */
    SERVICE_PRICE("交易手续费"),;


    private String description;

    ClearSourceEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static ClearSourceEnum getByDescription(String description) {
        for (ClearSourceEnum clearingSourceEnum : ClearSourceEnum.values()) {
            if (clearingSourceEnum.getDescription().equals(description)) {
                return clearingSourceEnum;
            }
        }
        throw new IllegalArgumentException(description + "Is not in ClearSourceEnum");
    }

}
