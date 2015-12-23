/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.redis;

import java.io.Serializable;

/**
 * @Description: Order
 * @Author: lin.shi
 * @CreateTime: 2015-09-28 10:14
 */
public class OrderCode implements Serializable {
    /*-- 本对象在redis数据库中的key前缀 --*/
    public static String REDIS_KEY = "order";

    private String orderIdentifier;   //订单编号

    private String code;         //验证码


    //==========================getter and setter



    public String getOrderIdentifier() {
        return orderIdentifier;
    }

    public void setOrderIdentifier(String orderIdentifier) {
        this.orderIdentifier = orderIdentifier;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}