/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.jpa.ec;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.OrderStatusEnum;
import com.pemass.persist.enumeration.OrderTypeEnum;
import com.pemass.persist.enumeration.PayStatusEnum;
import com.pemass.persist.enumeration.PaymentTypeEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @Description: 订单(包含商品订单和自定义订单)
 * @Author: pokl.huang
 * @CreateTime: 2014-11-14 14:14
 */
@Entity
@Table(name = "ec_order")
public class Order extends BaseDomain {

    @Column(name = "order_identifier", length = 50)
    private String orderIdentifier;   //订单编号【80-商品订单、81-自定义订单】

    @Column(name = "order_type", nullable = true)
    @Enumerated(EnumType.ORDINAL)
    private OrderTypeEnum orderType;    //订单类型

    @Column(name = "user_id", nullable = true)
    private Long userId;   //用户

    @Column(name = "original_price")
    private Double originalPrice;    //原价(当为商品订单时originalPrice = amount * marketPrice;当为自定义订单时表示消费的金额 )

    @Column(name = "total_price", nullable = true)
    private Double totalPrice;   //总金额 实付价格

    @Column(name = "cashback_price")
    private Double cashbackPrice;   //返现金额

    @Column(name = "loan_rate")
    private Double loanRate;//贷款利率

    @Column(name = "amount", nullable = true)
    private Integer amount;   //数量

    @Column(name = "total_point_e", nullable = true)
    private Integer totalPointE;   //消耗总E通币数

    @Column(name = "total_point_p", nullable = true)
    private Integer totalPointP;   //消耗总E积分数

    @Column(name = "total_point_o", nullable = true)
    private Integer totalPointO;   //消耗总一元购积分数

    @Column(name = "giving_point_e", nullable = true)
    private Integer givingPointE;  //赠送E通币数量

    @Column(name = "giving_point_p", nullable = true)
    private Integer givingPointP;  //赠送E积分数量

    @Column(name = "giving_point_o", nullable = true)
    private Integer givingPointO;  //赠送一元购积分数量

    @Column(name = "order_status", nullable = true)
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum orderStatus;   //订单状态【0-已确认、1-已取消、2-已失效】

    @Column(name = "pay_status", nullable = true)
    @Enumerated(EnumType.STRING)
    private PayStatusEnum payStatus;   //支付状态【0-未支付、1-已支付、2-已退款】

    @Column(name = "payment_type")
    @Enumerated(EnumType.STRING)
    private PaymentTypeEnum paymentType;    //支付类型(冗余字段和Payment表中保持一致)

    @Column(name = "clearing_status")
    private Integer clearingStatus;    //清分状态（0-未清分，1-已清分）

    @Column(name = "order_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderTime;   //下单时间

    @Column(name = "pay_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date payTime;   //支付时间

    @Column(name = "unsubscribe_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date unsubscribeTime;   //取消时间

    @Column(name = "cashier_user_id")
    private Long cashierUserId;   //收银员ID

    @Column(name = "site_id")
    private Long siteId;   //提货营业点ID

    @Column(name = "remark", length = 200)
    private String remark; //订单备注

    @Column(name = "external_order_identifier", length = 100)
    private String externalOrderIdentifier;   //外部订单号

    @Column(name = "bar_code", length = 200)
    private String barCode;   //条形码,二维码由客户端根据订单编号自动生成

    //=============getter and setter ===================

    public String getOrderIdentifier() {
        return orderIdentifier;
    }

    public void setOrderIdentifier(String orderIdentifier) {
        this.orderIdentifier = orderIdentifier;
    }

    public OrderTypeEnum getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderTypeEnum orderType) {
        this.orderType = orderType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public PaymentTypeEnum getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentTypeEnum paymentType) {
        this.paymentType = paymentType;
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

    public Date getUnsubscribeTime() {
        return unsubscribeTime;
    }

    public void setUnsubscribeTime(Date unsubscribeTime) {
        this.unsubscribeTime = unsubscribeTime;
    }

    public Long getCashierUserId() {
        return cashierUserId;
    }

    public void setCashierUserId(Long cashierUserId) {
        this.cashierUserId = cashierUserId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getExternalOrderIdentifier() {
        return externalOrderIdentifier;
    }

    public void setExternalOrderIdentifier(String externalOrderIdentifier) {
        this.externalOrderIdentifier = externalOrderIdentifier;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public Integer getClearingStatus() {
        return clearingStatus;
    }

    public void setClearingStatus(Integer clearingStatus) {
        this.clearingStatus = clearingStatus;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getCashbackPrice() {
        return cashbackPrice;
    }

    public void setCashbackPrice(Double cashbackPrice) {
        this.cashbackPrice = cashbackPrice;
    }

    public Double getLoanRate() {
        return loanRate;
    }

    public void setLoanRate(Double loanRate) {
        this.loanRate = loanRate;
    }
}

