/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.domain.vo;

import com.pemass.persist.enumeration.PointTypeEnum;

import java.io.Serializable;

/**
 * @Description: 红包选择单个批次积分的信息
 * @Author: oliver.he
 * @CreateTime: 2015-08-05 15:44
 */
public class PresentPointVO implements Serializable, Cloneable {

    private static final long serialVersionUID = 4727189468145374461L;

    // 积分批次ID
    private Long pointPurchaseId;

    // 积分类型
    private PointTypeEnum pointType;

    // 单个使用积分数
    private int amount;

    // 消耗该批次的总积分数
    private int totalAmount;

    // 最小积分数
    private int minAmount;

    // 最大积分数
    private int maxAmount;

    @Override
    public PresentPointVO clone() {
        PresentPointVO o = null;
        try {
            o = (PresentPointVO) super.clone();
        } catch (CloneNotSupportedException e) {
            System.err.println("clone happened exception." + this);
        }
        return o;
    }

    // ======================= \\

    public Long getPointPurchaseId() {
        return pointPurchaseId;
    }

    public void setPointPurchaseId(Long pointPurchaseId) {
        this.pointPurchaseId = pointPurchaseId;
    }

    public PointTypeEnum getPointType() {
        return pointType;
    }

    public void setPointType(PointTypeEnum pointType) {
        this.pointType = pointType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(int minAmount) {
        this.minAmount = minAmount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }
}