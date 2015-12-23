package com.pemass.persist.domain.jpa.poi;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.AuditStatusEnum;
import com.pemass.persist.enumeration.PointTypeEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * Description: PointPurchase
 * Author: estn.zuo
 * CreateTime: 2014-10-10 15:24
 * 认购积分
 */
@Entity
@Table(name = "poi_point_purchase")
public class PointPurchase extends BaseDomain {

    @Column(name = "organization_id")
    private Long organizationId;    //账户ID

    @Column(name = "point_pool_id",  nullable = false)
    private Long pointPoolId;     //积分池ID

    @Column(name = "purchase_identifier", length = 20, nullable = false)
    private String purchaseIdentifier;  //积分认购编号

    @Column(name = "amount", nullable = false)
    private Integer amount;     //认购总积分

    @Column(name = "point_type", nullable = false, length = 1)
    @Enumerated(EnumType.STRING)
    private PointTypeEnum pointType;    //积分类型

    @Column(name = "is_automatic")
    private Integer isAutomatic;//认购方式（0-手动，1-自动）

    @Column(name = "area",length = 4000, nullable = false)
    private String area; //区域

    @Column(name = "charge", nullable = false)
    private Double charge;   //手续费

    @Column(name = "rate", nullable = true, length = 10)
    private String rate;    //换算率 0.78%

    @Column(name = "audit_status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private AuditStatusEnum auditStatus; //审核状态

    @Column(name = "purchase_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date purchaseTime; //认购时间

    @Column(name = "expiry_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryTime;    //积分过期时间

    @Column(name = "is_clear")
    private Integer isClear; //是否清分（0-没有清分，1-已清分）

    //=================== getter and setter =========

    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
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

    public Double getCharge() {
        return charge;
    }

    public void setCharge(Double charge) {
        this.charge = charge;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public AuditStatusEnum getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(AuditStatusEnum auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Date getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(Date purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public Long getPointPoolId() {
        return pointPoolId;
    }

    public void setPointPoolId(Long pointPoolId) {
        this.pointPoolId = pointPoolId;
    }

    public String getPurchaseIdentifier() {
        return purchaseIdentifier;
    }

    public void setPurchaseIdentifier(String purchaseIdentifier) {
        this.purchaseIdentifier = purchaseIdentifier;
    }

    public Integer getIsAutomatic() {
        return isAutomatic;
    }

    public void setIsAutomatic(Integer isAutomatic) {
        this.isAutomatic = isAutomatic;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getIsClear() {
        return isClear;
    }

    public void setIsClear(Integer isClear) {
        this.isClear = isClear;
    }
}
