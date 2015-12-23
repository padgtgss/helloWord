/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.biz;

/**
 * @Description: ExpresspayCardPojo
 * @Author: zhou hang
 * @CreateTime: 2015-04-20 14:18
 */
public class ExpresspayCardPojo {

    private String cardNumber;//卡号

    private String cardCategory;//类型

    private Double costPrice;//成本价

    private String userName;//姓名

    private String telephone;//手机号

    private String invoice;//发票[1:需要发票、空：不需要发票]

    private Float totalPrice;//金额

    //=======================getter and setter====================


    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardCategory() {
        return cardCategory;
    }

    public void setCardCategory(String cardCategory) {
        this.cardCategory = cardCategory;
    }

    public Double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }
}