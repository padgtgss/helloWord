/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.ec;

import com.pemass.persist.enumeration.OrderStatusEnum;
import com.pemass.persist.enumeration.PayStatusEnum;

import java.util.Date;

/**
 * @Description: 自定义消费记录明细
 * @Author: zhou hang
 * @CreateTime: 2014-11-20 17:35
 */
public class CustomizationOrderPojo {

    private Long id;

    private String customizationOrderIdentifier;   //自定义订单编号

    private Long userId;   //用户;

    private OrderStatusEnum orderStatus;    //订单状态

    private PayStatusEnum payStatus;   //支付状态

    private Long cashierUserId;   //收银员ID

    private Double consumeAmount;   //消费金额

    private Double totalPrice;   //实付金额

    private Integer totalPointP;  //抵扣E积分呢

    private Integer totalPointE;   //抵扣E通币

    private Integer givingPointP;//赠送E积分

    private Integer givingPointE;//赠送E通币

    private Double discount;   //现场折扣

    private Long organizationId;//收款商户

    private Date orderTime;   //下单时间

    private Date payTime;   //支付时间

    private Long siteId;  //收款营业点/门店

    //==========================getter setter====================


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomizationOrderIdentifier() {
        return customizationOrderIdentifier;
    }

    public void setCustomizationOrderIdentifier(String customizationOrderIdentifier) {
        this.customizationOrderIdentifier = customizationOrderIdentifier;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public PayStatusEnum getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(PayStatusEnum payStatus) {
        this.payStatus = payStatus;
    }

    public OrderStatusEnum getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatusEnum orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getCashierUserId() {
        return cashierUserId;
    }

    public void setCashierUserId(Long cashierUserId) {
        this.cashierUserId = cashierUserId;
    }

    public Integer getTotalPointP() {
        return totalPointP;
    }

    public void setTotalPointP(Integer totalPointP) {
        this.totalPointP = totalPointP;
    }

    public Integer getTotalPointE() {
        return totalPointE;
    }

    public void setTotalPointE(Integer totalPointE) {
        this.totalPointE = totalPointE;
    }

    public Integer getGivingPointP() {
        return givingPointP;
    }

    public void setGivingPointP(Integer givingPointP) {
        this.givingPointP = givingPointP;
    }

    public Integer getGivingPointE() {
        return givingPointE;
    }

    public void setGivingPointE(Integer givingPointE) {
        this.givingPointE = givingPointE;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Double getConsumeAmount() {
        return consumeAmount;
    }

    public void setConsumeAmount(Double consumeAmount) {
        this.consumeAmount = consumeAmount;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}