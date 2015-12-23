/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.ec;

/**
 * @Description: InvoicePojo
 * @Author: estn.zuo
 * @CreateTime: 2014-12-10 15:11
 */
public class InvoicePojo {

    private String invoiceTitle;//发票抬头

    private Double totalPrice;//总金额

    private String receiveName;//接收人姓名

    private String receivePhone;//接收人电话

    private String receiveAddress;//接收人地址

    private String postDescription;//邮寄描述

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
}
