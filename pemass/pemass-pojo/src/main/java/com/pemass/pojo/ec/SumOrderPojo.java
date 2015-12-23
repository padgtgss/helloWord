/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.ec;

/**
 * @Description: OrderPojo
 * @Author: pokl.huang
 * @CreateTime: 2014-11-18 10:35
 */
public class SumOrderPojo {

    private Double totalPrice; //总价

    private Integer usePointP;//消耗派积分数

    private Integer usePointE;//消耗贝积分数


    //============================getter and setter====================================


    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getUsePointP() {
        return usePointP;
    }

    public void setUsePointP(Integer usePointP) {
        this.usePointP = usePointP;
    }

    public Integer getUsePointE() {
        return usePointE;
    }

    public void setUsePointE(Integer usePointE) {
        this.usePointE = usePointE;
    }
}

