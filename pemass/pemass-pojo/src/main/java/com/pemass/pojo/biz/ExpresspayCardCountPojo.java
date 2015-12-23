/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.biz;

/**
 * @Description: ExpresspayCardCountPojo
 * @Author: zhou hang
 * @CreateTime: 2015-04-21 10:39
 */
public class ExpresspayCardCountPojo {

    private Long onSaleCount;//待售卡数量

    private Long soldCount;  //已售卡数量

    private Long inTransitCount; //在途卡批次数量

    private Long  allotCount;//调拨卡批次数量
    //==========================getter and setter==============


    public Long getOnSaleCount() {
        return onSaleCount;
    }

    public void setOnSaleCount(Long onSaleCount) {
        this.onSaleCount = onSaleCount;
    }

    public Long getSoldCount() {
        return soldCount;
    }

    public void setSoldCount(Long soldCount) {
        this.soldCount = soldCount;
    }

    public Long getInTransitCount() {
        return inTransitCount;
    }

    public void setInTransitCount(Long inTransitCount) {
        this.inTransitCount = inTransitCount;
    }

    public Long getAllotCount() {
        return allotCount;
    }

    public void setAllotCount(Long allotCount) {
        this.allotCount = allotCount;
    }
}