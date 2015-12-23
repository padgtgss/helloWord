/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.poi;

import com.pemass.persist.enumeration.PointTypeEnum;

/**
 * 用户 订单积分支付明细
 */
public class OrderPointPayDetailPojo {

    private Long orderId;//订单ID

    private Long userId;//使用者ID

    private Long  belongUserId;   //所属用户ID

    private PointTypeEnum pointType;//积分类型

    private Integer amount;//积分数量

    //================getter setter=================


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBelongUserId() {
        return belongUserId;
    }

    public void setBelongUserId(Long belongUserId) {
        this.belongUserId = belongUserId;
    }

    public PointTypeEnum getPointType() {
        return pointType;
    }

    public void setPointType(PointTypeEnum pointType) {
        this.pointType = pointType;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}