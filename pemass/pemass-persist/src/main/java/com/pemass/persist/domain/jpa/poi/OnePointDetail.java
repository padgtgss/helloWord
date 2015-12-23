/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.domain.jpa.poi;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.PointChannelEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * @Description: 用户一元购积分明细表
 * @Author: estn.zuo
 * @CreateTime: 2015-06-11 17:02
 */
@Entity
@Table(name = "poi_one_point_detail")
public class OnePointDetail extends BaseDomain {

    @Column(name = "user_id", nullable = false)
    private  Long userId;  //所属用户

    @Column(name = "point_source", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private PointChannelEnum pointChannelEnum;    //积分来源

    @Column(name = "order_id")
    private Long orderId;//订单ID

    @Column(name = "belong_user_id",nullable = false)
    private Long belongUserId;  // pointChannelEnum==ONE_POINT_GIVE 表示朋友赠送

    @Column(name = "amount", nullable = false)
    private Integer amount; //数量

    @Column(name = "useable_amount", nullable = false)
    private Integer useableAmount;  //可用积分数

    @Column(name = "expiry_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryTime;  //过期时间

    @Column(name = "is_clear", nullable = false)
    private Boolean isClear;    //是否结算 [默认是：false、已结算：true]


    //============== getter and setter ===================



    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public PointChannelEnum getPointChannelEnum() {
        return pointChannelEnum;
    }

    public void setPointChannelEnum(PointChannelEnum pointChannelEnum) {
        this.pointChannelEnum = pointChannelEnum;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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

    public Integer getUseableAmount() {
        return useableAmount;
    }

    public void setUseableAmount(Integer useableAmount) {
        this.useableAmount = useableAmount;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    public Boolean getIsClear() {
        return isClear;
    }

    public void setIsClear(Boolean isClear) {
        this.isClear = isClear;
    }


}
