package com.pemass.persist.domain.jpa.poi;


import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.PointChannelEnum;
import com.pemass.persist.enumeration.PointTypeEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * 商户积分明细表
 */
@Entity
@Table(name = "poi_organization_point_detail")
public class OrganizationPointDetail extends BaseDomain {

    @Column(name = "point_purchase_id")
    private Long pointPurchaseId;//积分认购批号

    @Column(name = "organization_id")
    private Long organizationId;//账户

    @Column(name = "point_type", length = 1, nullable = false)
    @Enumerated(EnumType.STRING)
    private PointTypeEnum pointType;//积分类型【P-E积分,E-E通币】

    @Column(name = "area", nullable = true, length = 4000)
    private String area;//区域

    @Column(name = "amount", nullable = false)
    private Integer amount;//数量

    @Column(name = "useable_amount", nullable = false)
    private Integer useableAmount;//可用积分数

    @Column(name = "point_channel", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private PointChannelEnum pointChannel; //积分来源

    @Column(name = "expiry_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryTime;  //过期时间

    //============== getter and setter ===================


    public Long getPointPurchaseId() {
        return pointPurchaseId;
    }

    public void setPointPurchaseId(Long pointPurchaseId) {
        this.pointPurchaseId = pointPurchaseId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public PointTypeEnum getPointType() {
        return pointType;
    }

    public void setPointType(PointTypeEnum pointType) {
        this.pointType = pointType;
    }


    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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

    public PointChannelEnum getPointChannel() {
        return pointChannel;
    }

    public void setPointChannel(PointChannelEnum pointChannel) {
        this.pointChannel = pointChannel;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }
}
