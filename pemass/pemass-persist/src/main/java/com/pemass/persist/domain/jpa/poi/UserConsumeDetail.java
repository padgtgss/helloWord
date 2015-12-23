package com.pemass.persist.domain.jpa.poi;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.ConsumeTypeEnum;
import com.pemass.persist.enumeration.PointTypeEnum;

import javax.persistence.*;

/**
 * 用户积分消耗明细
 */
@Entity
@Table(name = "poi_user_consume_detail")
public class UserConsumeDetail extends BaseDomain {

    @Column(name = "user_point_detail_id",  nullable = false)
    private Long userPointDetailId; //积分明细

    @Column(name = "consume_type", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private ConsumeTypeEnum consumeType;   //消费类型

    @Column(name = "consume_target_id", nullable = false)
    private Long consumeTargetId;   //消耗对象的ID，参考ConsumeTypeEnum枚举

    @Column(name = "point_type", length = 1, nullable = false)
    @Enumerated(EnumType.STRING)
    private PointTypeEnum pointType;    //积分类型【P-E积分,E-E通币】

    @Column(name = "amount", nullable = false)
    private Integer amount; //实付积分数

    @Column(name = "payable_amount", nullable = false)
    private Integer payableAmount;  //应付积分数

    //============ getter and setter ==============


    public Long getUserPointDetailId() {
        return userPointDetailId;
    }

    public void setUserPointDetailId(Long userPointDetailId) {
        this.userPointDetailId = userPointDetailId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public ConsumeTypeEnum getConsumeType() {
        return consumeType;
    }

    public void setConsumeType(ConsumeTypeEnum consumeType) {
        this.consumeType = consumeType;
    }

    public Long getConsumeTargetId() {
        return consumeTargetId;
    }

    public void setConsumeTargetId(Long consumeTargetId) {
        this.consumeTargetId = consumeTargetId;
    }

    public PointTypeEnum getPointType() {
        return pointType;
    }

    public void setPointType(PointTypeEnum pointType) {
        this.pointType = pointType;
    }

    public Integer getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(Integer payableAmount) {
        this.payableAmount = payableAmount;
    }
}
