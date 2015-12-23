/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.jpa.poi;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.OrderItemStatusEnum;
import com.pemass.persist.enumeration.PointTypeEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * @Description: 积分卷
 * @Author: zhou hang
 * @CreateTime: 2015-04-14 17:33
 */
@Entity
@Table(name = "poi_point_coupon")
public class PointCoupon extends BaseDomain {

    @Column(name = "pack_identifier", nullable = false, length = 50)
    private String packIdentifier;//批次号

    @Column(name = "organization_id",nullable = false)
    private Long organizationId;  //商户ID，积分来源商户，由谁发行的卡

    @Column(name = "user_id")
    private Long userId;  //C用户

    @Column(name = "point_purchase_id",nullable = false)
    private Long pointPurchaseId;//积分认购ID，积分来源

    @Column(name = "point_type",nullable = false,length = 1)
    @Enumerated(EnumType.STRING)
    private PointTypeEnum pointType;    //积分类型【P-E积分、E-E通币】

    @Column(name = "amount", nullable = false)
    private Integer amount;    //积分数量

    @Column(name = "card_secret",length = 50,nullable = false,unique = true)
    private String cardSecret;  //卡密

    @Column(name = "use_status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private OrderItemStatusEnum useStatus;   //使用状态 :【已使用、未使用】

    @Column(name = "issue_time",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date issueTime; //发行时间

    @Column(name = "expiry_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryTime;  //过期时间

    @Column(name = "use_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date useTime;   //使用时间

    @Column(name = "make_card")
    private Boolean makeCard;   //是否制卡【是：true、否:false】


    //=================getter and setter=========================

    public String getPackIdentifier() {
        return packIdentifier;
    }

    public void setPackIdentifier(String packIdentifier) {
        this.packIdentifier = packIdentifier;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
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

    public String getCardSecret() {
        return cardSecret;
    }

    public void setCardSecret(String cardSecret) {
        this.cardSecret = cardSecret;
    }

    public OrderItemStatusEnum getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(OrderItemStatusEnum useStatus) {
        this.useStatus = useStatus;
    }

    public Date getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Date issueTime) {
        this.issueTime = issueTime;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    public Date getUseTime() {
        return useTime;
    }

    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }

    public Boolean getMakeCard() {
        return makeCard;
    }

    public void setMakeCard(Boolean makeCard) {
        this.makeCard = makeCard;
    }
}