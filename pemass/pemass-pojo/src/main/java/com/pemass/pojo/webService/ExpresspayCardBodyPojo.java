/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.webService;

/**
 * @Description: ExpresspayCardBodyPojo
 * @Author: lin.shi
 * @CreateTime: 2015-09-15 10:59
 */
public class ExpresspayCardBodyPojo {

    private String activeDay;    //卡片激活日期
    private String cardType;    //卡类型
    private String cardno;      //卡号
    private String cdBal;       //卡余额
    private String status;      //卡状态

    private String addAmt;      //充值金额
    private String amount;      //金额

    private String balance;     //当前余额

    //=====================getter and setter==================================


    public String getActiveDay() {
        return activeDay;
    }

    public void setActiveDay(String activeDay) {
        this.activeDay = activeDay;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getCdBal() {
        return cdBal;
    }

    public void setCdBal(String cdBal) {
        this.cdBal = cdBal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddAmt() {
        return addAmt;
    }

    public void setAddAmt(String addAmt) {
        this.addAmt = addAmt;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}