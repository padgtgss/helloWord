/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.biz;

import com.pemass.persist.enumeration.DepositStatusEnum;

import java.util.Date;

/**
 * @Description: ExpresspayCardPojo
 * @Author: zhou hang
 * @CreateTime: 2015-04-20 14:18
 */
public class UserExpresspayCardDetailPojo {

    private String cardNumber;  //卡号

    private Integer pointEAmount;  //圈存E通币数量

    private Double money;    //获得金额

    private Double poundage;  //手续费

    private DepositStatusEnum depositStatus;   //圈存状态

    private Date depositTime;   //圈存时间

    private String operateIdentifier;  //圈存流水号

    //=======================getter and setter====================


    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Integer getPointEAmount() {
        return pointEAmount;
    }

    public void setPointEAmount(Integer pointEAmount) {
        this.pointEAmount = pointEAmount;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Double getPoundage() {
        return poundage;
    }

    public void setPoundage(Double poundage) {
        this.poundage = poundage;
    }

    public DepositStatusEnum getDepositStatus() {
        return depositStatus;
    }

    public void setDepositStatus(DepositStatusEnum depositStatus) {
        this.depositStatus = depositStatus;
    }

    public Date getDepositTime() {
        return depositTime;
    }

    public void setDepositTime(Date depositTime) {
        this.depositTime = depositTime;
    }

    public String getOperateIdentifier() {
        return operateIdentifier;
    }

    public void setOperateIdentifier(String operateIdentifier) {
        this.operateIdentifier = operateIdentifier;
    }
}