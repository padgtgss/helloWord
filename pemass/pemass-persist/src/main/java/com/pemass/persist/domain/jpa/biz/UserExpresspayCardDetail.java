/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.jpa.biz;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.DepositStatusEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @Description: 圈存记录表
 * @Author: lin.shi
 * @CreateTime: 2015-08-26 15:59
 */
@Entity
@Table(name = "biz_user_expresspay_card_detail")
public class UserExpresspayCardDetail extends BaseDomain {

    @Column(name = "user_id",nullable = false)
    private Long userId;   //用户Id

    @Column(name = "card_number",nullable = false)
    private String cardNumber;  //卡号

    @Column(name = "point_e_amount",nullable = false)
    private Integer pointEAmount;  //圈存E通币数量

    @Column(name = "money",nullable = false)
    private Double money;    //获得金额

    @Column(name = "poundage",nullable = false)
    private Double poundage;  //手续费

    @Column(name = "deposit_status",nullable = false)
    @Enumerated(EnumType.STRING)
    private DepositStatusEnum depositStatus;   //圈存状态

    @Column(name = "operate_identifier",nullable = false)
    private String  operateIdentifier;    //圈存流水号

    @Column(name = "deposit_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date depositTime;   //圈存时间

    @Column(name = "message")
    private String message;    //中银通返还信息


    //============== getter and setter ===================


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}