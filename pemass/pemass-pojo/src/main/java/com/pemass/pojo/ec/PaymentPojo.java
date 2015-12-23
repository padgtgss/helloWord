/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.pojo.ec;

import com.pemass.persist.enumeration.PaymentTypeEnum;

import java.util.Date;

/**
 * @Description: PaymentPojo
 * @Author: estn.zuo
 * @CreateTime: 2015-07-16 21:23
 */
public class PaymentPojo {

    private String paymentIdentifier; //支付流水号

    private String orderIdentifier;   //订单编号

    private Double totalPrice; //支付金额

    private PaymentTypeEnum paymentType;    //支付类型

    private Date paymentTime;   //支付时间

    private String externalPaymentIdentifier;   //外部支付流水号

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
}
