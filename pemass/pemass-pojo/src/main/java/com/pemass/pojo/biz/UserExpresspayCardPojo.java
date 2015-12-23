/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.biz;

/**
 * @Description: UserExpresspayCardPojo
 * @Author: lin.shi
 * @CreateTime: 2015-08-26 19:35
 */
public class UserExpresspayCardPojo {

    private Long userId;

    private String cardNumber;

    //=======================getter and setter===================


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