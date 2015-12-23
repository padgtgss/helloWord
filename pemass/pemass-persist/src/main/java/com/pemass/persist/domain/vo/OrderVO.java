/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.domain.vo;

import com.pemass.persist.enumeration.OrderStatusEnum;
import com.pemass.persist.enumeration.PayStatusEnum;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 订单展示使用实体(商品订单与自定义订单)
 * @Author: oliver.he
 * @CreateTime: 2015-08-17 16:04
 */
public final class OrderVO implements Serializable, Cloneable {

    private static final long serialVersionUID = 8544743945482720571L;

    private long orderId;   // 订单ID

    private String orderIdentifier; // 订单号

    private OrderStatusEnum orderStatus; // 订单状态

    private PayStatusEnum payStatus; // 订单支付状态

    private Integer clearingStatus;    //清分状态（0-未清分，1-已清分）

    private Date orderTime; // 下单时间

    private String casherUsername; // 收银员名称

    private String organizationName; // 商户名

    private String username; // 用户名

    private String totalPrice;  // 实付金额

    private String productName; // 商品名称

    private Integer amount; // 商品数量

    private Integer totalPointE;   // 消耗总E通币数

    private Integer totalPointP;   // 消耗总E积分数

    private Integer totalPointO;   // 消耗总一元购积分数

    private Integer givingPointE;  // 赠送E通币数量

    private Integer givingPointP;  // 赠送E积分数量

    private Integer givingPointO;  // 赠送一元购积分数量


    public OrderVO clone() {
        OrderVO orderVO = null;
        try {
            orderVO = (OrderVO) super.clone();
        } catch (CloneNotSupportedException e) {
            System.err.println("clone happened exception." + this);
        }
        return orderVO;
    }

    // ================== getter and setter ================= \\

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getOrderIdentifier() {
        return orderIdentifier;
    }

    public void setOrderIdentifier(String orderIdentifier) {
        this.orderIdentifier = orderIdentifier;
    }

    public OrderStatusEnum getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatusEnum orderStatus) {
        this.orderStatus = orderStatus;
    }

    public PayStatusEnum getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(PayStatusEnum payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getClearingStatus() {
        return clearingStatus;
    }

    public void setClearingStatus(Integer clearingStatus) {
        this.clearingStatus = clearingStatus;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getCasherUsername() {
        return casherUsername;
    }

    public void setCasherUsername(String casherUsername) {
        this.casherUsername = casherUsername;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getTotalPointE() {
        return totalPointE;
    }

    public void setTotalPointE(Integer totalPointE) {
        this.totalPointE = totalPointE;
    }

    public Integer getTotalPointP() {
        return totalPointP;
    }

    public void setTotalPointP(Integer totalPointP) {
        this.totalPointP = totalPointP;
    }

    public Integer getTotalPointO() {
        return totalPointO;
    }

    public void setTotalPointO(Integer totalPointO) {
        this.totalPointO = totalPointO;
    }

    public Integer getGivingPointE() {
        return givingPointE;
    }

    public void setGivingPointE(Integer givingPointE) {
        this.givingPointE = givingPointE;
    }

    public Integer getGivingPointP() {
        return givingPointP;
    }

    public void setGivingPointP(Integer givingPointP) {
        this.givingPointP = givingPointP;
    }

    public Integer getGivingPointO() {
        return givingPointO;
    }

    public void setGivingPointO(Integer givingPointO) {
        this.givingPointO = givingPointO;
    }
}