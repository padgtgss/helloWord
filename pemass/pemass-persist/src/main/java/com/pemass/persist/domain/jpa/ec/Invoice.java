/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.jpa.ec;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.InvoiceStatusEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

/**
 * @Description: 发票
 * @Author: huang zhuo
 * @CreateTime: 2014-11-14 14:45
 */
@Entity
@Table(name = "ec_invoice")
public class Invoice extends BaseDomain {

    @Column(name = "order_id", nullable = false)
    private Long orderId;//订单

    @Column(name = "product_id", nullable = false)
    private Long productId;//商品

    @Column(name = "invoice_title", length = 50)
    private String invoiceTitle;//发票抬头

    @Column(name = "total_price")
    private Double totalPrice;//总金额

    @Column(name = "receive_name", length = 50)
    private String receiveName;//接收人姓名

    @Column(name = "receive_phone", length = 20)
    private String receivePhone;//接收人电话

    @Column(name = "receive_address", length = 200)
    private String receiveAddress;//接收人地址

    @Column(name = "post_description", length = 50)
    private String postDescription;//邮寄描述

    @Column(name = "invoice_status", length = 20)
    @Enumerated(EnumType.STRING)
    private InvoiceStatusEnum invoiceStatus; //发票状态

    //=============getter and setter ===================


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getReceivePhone() {
        return receivePhone;
    }

    public void setReceivePhone(String receivePhone) {
        this.receivePhone = receivePhone;
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public InvoiceStatusEnum getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(InvoiceStatusEnum invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }
}

