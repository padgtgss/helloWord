package com.pemass.persist.domain.jpa.poi;

import com.pemass.common.server.domain.BaseDomain;
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
 * Description: PointPool
 * Author: estn.zuo
 * CreateTime: 2014-10-10 15:03
 */

@Entity
@Table(name = "poi_point_pool")
public class PointPool extends BaseDomain {

    @Column(name = "issue_identifier", length = 20, unique = true, nullable = false)
    private String issueIdentifier;//发行编号

    @Column(name = "issuer", length = 50, nullable = false)
    private String issuer;//发行方

    @Column(name = "issue_name", length = 50, nullable = false)
    private String issueName;   //发行名称

    @Column(name = "point_type", length = 1, nullable = false)
    @Enumerated(EnumType.STRING)
    private PointTypeEnum pointType;//积分类型

    @Column(name = "amount", nullable = false)
    private Integer amount;//数量

    @Column(name = "area", length = 4000, nullable = false)
    private String area; //区域

    @Column(name = "expiry_period")
    private Integer expiryPeriod;//该积分批次的过期时间，使用过期时间段来描述(单位：月)

    @Column(name = "expiry_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryTime;//该积分批次过期时间，使用过期时间点(商品具体哪天过期时间点)

    @Column(name = "issue_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date issueTime;//发行时间


    //=================== getter and setter =========

    public String getIssueIdentifier() {
        return issueIdentifier;
    }

    public void setIssueIdentifier(String issueIdentifier) {
        this.issueIdentifier = issueIdentifier;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getIssueName() {
        return issueName;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }

    public PointTypeEnum getPointType() {
        return pointType;
    }

    public void setPointType(PointTypeEnum pointType) {
        this.pointType = pointType;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    public Date getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Date issueTime) {
        this.issueTime = issueTime;
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

    public Integer getExpiryPeriod() {
        return expiryPeriod;
    }

    public void setExpiryPeriod(Integer expiryPeriod) {
        this.expiryPeriod = expiryPeriod;
    }
}
