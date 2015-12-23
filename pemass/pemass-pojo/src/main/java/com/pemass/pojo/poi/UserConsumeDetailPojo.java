/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.poi;

import com.pemass.persist.enumeration.ConsumeTypeEnum;
import com.pemass.persist.enumeration.PointTypeEnum;


/**
 * @Description: UserConsumeDetailPojo
 * @Author: lin.shi
 * @CreateTime: 2015-07-31 15:56
 */
public class UserConsumeDetailPojo {

    private Long id;  //id


    private Long userPointDetailId; //积分明细

    private ConsumeTypeEnum consumeType;   //消费类型

    private PointTypeEnum pointType;    //积分类型【P-派积分,E-贝积分】

    private Integer amount; //实付积分数


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserPointDetailId() {
        return userPointDetailId;
    }

    public void setUserPointDetailId(Long userPointDetailId) {
        this.userPointDetailId = userPointDetailId;
    }

    public ConsumeTypeEnum getConsumeType() {
        return consumeType;
    }

    public void setConsumeType(ConsumeTypeEnum consumeType) {
        this.consumeType = consumeType;
    }

    public PointTypeEnum getPointType() {
        return pointType;
    }

    public void setPointType(PointTypeEnum pointType) {
        this.pointType = pointType;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}