/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.redis;

import java.io.Serializable;

/**
 * @Description: Pay
 * @Author: lin.shi
 * @CreateTime: 2015-09-21 11:35
 */
public class OrderPay implements Serializable {

    /*-- 本对象在redis数据库中的key前缀 --*/
    public static String REDIS_KEY = "pay";

    private Long userId;   //用户Id

    private String username;  //用户名

    private String  payCode; //支付码

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}