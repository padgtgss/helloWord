/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.ec;

import com.pemass.persist.enumeration.PointTypeEnum;

/**
 * @Description: OrderPojo
 * @Author: pokl.huang
 * @CreateTime: 2014-11-18 10:35
 */
public class OrderPointConsumeDetailPojo {

    private Long userId; //使用者

    private Long belongUserId; //所属积分用户ID

    private Integer amount;//积分数

    private PointTypeEnum pointType;//积分类型【P-E积分,E-通币】


    //=============================getter and setter===================================


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBelongUserId() {
        return belongUserId;
    }

    public void setBelongUserId(Long belongUserId) {
        this.belongUserId = belongUserId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public PointTypeEnum getPointType() {
        return pointType;
    }

    public void setPointType(PointTypeEnum pointType) {
        this.pointType = pointType;
    }
}

