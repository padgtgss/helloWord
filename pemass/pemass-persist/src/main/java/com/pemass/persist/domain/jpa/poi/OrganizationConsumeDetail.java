package com.pemass.persist.domain.jpa.poi;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.ConsumeTypeEnum;
import com.pemass.persist.enumeration.PointTypeEnum;

import javax.persistence.*;

/**
 * 商户积分消耗明细
 */
@Entity
@Table(name = "poi_organization_consume_detail")
public class OrganizationConsumeDetail extends BaseDomain {

    @Column(name = "point_purchase_id",  nullable = false)
    private Long pointPurchaseId;//积分认购ID

    @Column(name = "consume_type", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private ConsumeTypeEnum consumeType;//消耗类型

    @Column(name = "consume_target_id", nullable = false)
    private Long consumeTargetId;//消耗对象的ID

    @Column(name = "point_type", length = 1, nullable = false)
    @Enumerated(EnumType.STRING)
    private PointTypeEnum pointType;//积分类型

    @Column(name = "amount", nullable = false)
    private Integer amount; //实付积分数

    @Column(name = "payable_amount", nullable = false)
    private Integer payableAmount;  //应付积分数

    //============ getter and setter ==============


    public Long getPointPurchaseId() {
        return pointPurchaseId;
    }

    public void setPointPurchaseId(Long pointPurchaseId) {
        this.pointPurchaseId = pointPurchaseId;
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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(Integer payableAmount) {
        this.payableAmount = payableAmount;
    }
}
