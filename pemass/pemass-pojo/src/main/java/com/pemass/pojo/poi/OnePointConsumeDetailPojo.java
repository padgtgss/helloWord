/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.poi;

import java.util.Date;

/**
 * @Description: OnePointConsumeDetailPojo
 * @Author: zhou hang
 * @CreateTime: 2015-06-15 17:24
 */
public class OnePointConsumeDetailPojo {

    private Integer consumption; //消费的积分(自己的或是朋友赠送的)

    private String orderIdentifier;   //订单编号;

    private String username;          //订单发起者

    private Double profit;          //返款金额

    private Double myselfProfitCount;   //用户自己赚的还款总额

    private Double friendProfitCount;   //朋友赚的还款总额

    private Long myselfPointCount;    //消耗自己的总积分

    private Long friendPointCount;   //消耗朋友赠送的总积分

    private Date createTime;//入账时间


//====================getter and setter =================


    public Integer getConsumption() {
        return consumption;
    }

    public void setConsumption(Integer consumption) {
        this.consumption = consumption;
    }

    public String getOrderIdentifier() {
        return orderIdentifier;
    }

    public void setOrderIdentifier(String orderIdentifier) {
        this.orderIdentifier = orderIdentifier;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Double getMyselfProfitCount() {
        return myselfProfitCount;
    }

    public void setMyselfProfitCount(Double myselfProfitCount) {
        this.myselfProfitCount = myselfProfitCount;
    }

    public Double getFriendProfitCount() {
        return friendProfitCount;
    }

    public void setFriendProfitCount(Double friendProfitCount) {
        this.friendProfitCount = friendProfitCount;
    }

    public Long getMyselfPointCount() {
        return myselfPointCount;
    }

    public void setMyselfPointCount(Long myselfPointCount) {
        this.myselfPointCount = myselfPointCount;
    }

    public Long getFriendPointCount() {
        return friendPointCount;
    }

    public void setFriendPointCount(Long friendPointCount) {
        this.friendPointCount = friendPointCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}