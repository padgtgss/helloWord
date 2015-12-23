/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.jpa.biz;

import com.pemass.common.server.domain.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Description: UserPresspayCard
 * @Author: lin.shi
 * @CreateTime: 2015-08-26 16:22
 */
@Entity
@Table(name = "biz_user_expresspay_card")
public class UserExpresspayCard extends BaseDomain{

    @Column(name = "user_id",nullable = false)
    private Long userId;//C端用户ID

    @Column(name = "card_number",nullable = false)
    private String cardNumber;//银通卡号



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
}