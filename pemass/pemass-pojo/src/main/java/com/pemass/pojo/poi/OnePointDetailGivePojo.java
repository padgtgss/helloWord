/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.poi;

import java.util.Date;

/**
 * @Description: 一元购积分赠送详情pojo
 * @Author: lin.shi
 * @CreateTime: 2015-07-14 22:05
 */
public class OnePointDetailGivePojo {

    private Long id;            //赠送记录ID

    private String userName;   //用户手机号

    private Long userId;      //用户编号

    private Long amount;      //赠送的积分

    private Long useableAmount;//剩余积分

    private Double profit;      //赚取的金额

    private Date createTime;   //赠送时间


    //==========================================getter and setter===============================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getUseableAmount() {
        return useableAmount;
    }

    public void setUseableAmount(Long useableAmount) {
        this.useableAmount = useableAmount;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}