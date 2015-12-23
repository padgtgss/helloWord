/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.domain.jpa.ec;

import com.pemass.common.server.domain.BaseDomain;
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
 * @Description: 支付订单
 * @Author: estn.zuo
 * @CreateTime: 2015-07-16 17:20
 */
@Entity
@Table(name = "ec_payment")
public class Payment extends BaseDomain {

    @Column(name = "order_id", nullable = false)
    private Long orderId;   //支付订单号

    @Column(name = "order_identifier", nullable = false, length = 20)
    private String orderIdentifier;   //订单编号

    @Column(name = "payment_identifier", nullable = false, length = 20)
    private String paymentIdentifier; //支付流水号

    @Column(name = "pay_id")
    private String payId;    //支付帐号

    @Column(name = "payment_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentTypeEnum paymentType;    //支付类型

    @Column(name = "total_price")
    private Double totalPrice; //支付金额

    @Column(name = "payment_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentTime;   //支付时间

    @Column(name = "external_payment_Identifier", length = 100)
    private String externalPaymentIdentifier;   //外部支付流水号

    @Column(name = "pos_serial", length = 50)
    private String posSerial;   //机具编号（当使用商通道的POS机具支付的时，上传本参数）

    @Column(name = "device_id")
    private Integer deviceId; //设备ID（当使用云钱包支付时，上传本参数）

    @Column(name = "is_customization_order")
    private Integer isCustomizationOrder;   //是否为自定义订单支付【0-不是、1-是】

    @Column(name = "is_succeed")
    private Integer isSucceed;   //是否支付成功【0-失败、1-成功】

    //===========================getter and setter=======================================

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

    public String getPaymentIdentifier() {
        return paymentIdentifier;
    }

    public void setPaymentIdentifier(String paymentIdentifier) {
        this.paymentIdentifier = paymentIdentifier;
    }

    public PaymentTypeEnum getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentTypeEnum paymentType) {
        this.paymentType = paymentType;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getExternalPaymentIdentifier() {
        return externalPaymentIdentifier;
    }

    public void setExternalPaymentIdentifier(String externalPaymentIdentifier) {
        this.externalPaymentIdentifier = externalPaymentIdentifier;
    }

    public String getPosSerial() {
        return posSerial;
    }

    public void setPosSerial(String posSerial) {
        this.posSerial = posSerial;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getIsCustomizationOrder() {
        return isCustomizationOrder;
    }

    public void setIsCustomizationOrder(Integer isCustomizationOrder) {
        this.isCustomizationOrder = isCustomizationOrder;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public Integer getIsSucceed() {
        return isSucceed;
    }

    public void setIsSucceed(Integer isSucceed) {
        this.isSucceed = isSucceed;
    }
}
