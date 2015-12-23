/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.jpa.poi;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.PointTypeEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * @Description: LotteryDraw
 * @Author: zhou hang
 * @CreateTime: 2015-01-27 10:49
 */
@Entity
@Table(name = "poi_lottery_draw")
public class LotteryDraw extends BaseDomain {

    @Column(name = "user_id",nullable = false)
    private Long userId; //用户id

    @Column(name = "order_Identifier",nullable = false)
    private String orderIdentifier; //编号

    @Column(name = "prize")
    private Integer prize;//奖项

    @Column(name = "point_purchase_id")
    private Long pointPurchaseId;   //系统默认消耗

    @Column(name = "total_point")
    private Integer totalPoint;  //用户消耗积分数量

    @Column(name = "point_detail_id")
    private Long pointDetailId;   //用户消耗积分明细

    @Column(name = "use_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date useTime;//时间

    @Column(name = "point_type", length = 1, nullable = false)
    @Enumerated(EnumType.STRING)
    private PointTypeEnum pointType;//积分类型【P-E积分,E-E通币】

    //==============================getter  setter ===========================


    public String getOrderIdentifier() {
        return orderIdentifier;
    }

    public void setOrderIdentifier(String orderIdentifier) {
        this.orderIdentifier = orderIdentifier;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPointPurchaseId() {
        return pointPurchaseId;
    }

    public void setPointPurchaseId(Long pointPurchaseId) {
        this.pointPurchaseId = pointPurchaseId;
    }

    public Integer getPrize() {
        return prize;
    }

    public void setPrize(Integer prize) {
        this.prize = prize;
    }

    public Integer getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(Integer totalPoint) {
        this.totalPoint = totalPoint;
    }

    public PointTypeEnum getPointType() {
        return pointType;
    }

    public void setPointType(PointTypeEnum pointType) {
        this.pointType = pointType;
    }

    public Date getUseTime() {
        return useTime;
    }

    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }

    public Long getPointDetailId() {
        return pointDetailId;
    }

    public void setPointDetailId(Long pointDetailId) {
        this.pointDetailId = pointDetailId;
    }
}