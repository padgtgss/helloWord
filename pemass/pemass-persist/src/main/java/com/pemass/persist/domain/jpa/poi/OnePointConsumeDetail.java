/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.domain.jpa.poi;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.ConsumeTypeEnum;

import javax.persistence.*;

/**
 * @Description: OnePointConsumeDetail
 * @Author: estn.zuo
 * @CreateTime: 2015-06-11 17:22
 */
@Entity
@Table(name = "poi_one_point_consume_detail")
public class OnePointConsumeDetail extends BaseDomain {

    @Column(name = "user_id", nullable = false)
    private Long userId;  //所属用户

    @Column(name = "one_point_detail_id", nullable = false)
    private Long onePointDetailId;//积分明细

    @Column(name = "consume_type", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private ConsumeTypeEnum consumeType;   //消费类型,目前只支持ORDER

    @Column(name = "order_identifier",  nullable = false)
    private String orderIdentifier;  //订单编号

    @Column(name = "order_id",  nullable = false)
    private Long orderId;  //所属订单

    @Column(name = "amount", nullable = false)
    private Integer amount; //实付积分数

    @Column(name = "payable_amount",  nullable = false)
    private Integer payableAmount;  //应付积分数

    @Column(name = "profit")
    private Double profit; //订单消耗的一元购积分所赚取的钱

    @Column(name = "belong_user_id", nullable = false)
    private Long belongUserId;  // pointSource==ONE_POINT_GIVE 表示朋友赠送

    //============== getter and setter ===================


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOnePointDetailId() {
        return onePointDetailId;
    }

    public void setOnePointDetailId(Long onePointDetailId) {
        this.onePointDetailId = onePointDetailId;
    }

    public ConsumeTypeEnum getConsumeType() {
        return consumeType;
    }

    public void setConsumeType(ConsumeTypeEnum consumeType) {
        this.consumeType = consumeType;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderIdentifier() {
        return orderIdentifier;
    }

    public void setOrderIdentifier(String orderIdentifier) {
        this.orderIdentifier = orderIdentifier;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Long getBelongUserId() {
        return belongUserId;
    }

    public void setBelongUserId(Long belongUserId) {
        this.belongUserId = belongUserId;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Integer getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(Integer payableAmount) {
        this.payableAmount = payableAmount;
    }
}
