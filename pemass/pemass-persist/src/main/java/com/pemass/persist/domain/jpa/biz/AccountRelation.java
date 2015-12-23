package com.pemass.persist.domain.jpa.biz;

import com.pemass.common.server.domain.BaseDomain;

import com.pemass.persist.enumeration.AuditStatusEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.persistence.*;
import java.util.Date;

/**
 * Description: 业务关系表
 * Author: luoc
 * CreateTime: 2014-10-10 14:15
 */
@Entity
@Table(name = "biz_account_relation")
public class AccountRelation extends BaseDomain {

    @Column(name = "organization_id",nullable = false)
    private Long organizationId;//所属用户

    @Column(name = "parent_organization_id",nullable = false)
    private Long parentOrganizationId;//所属用户的父类信息

    @Column(name = "path", length = 100)
    private String path;//用户层次关系结构

    @Column(name = "audit_status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private AuditStatusEnum auditStatus;//审核状态

    @Column(name = "apply_time")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date applyTime;//申请时间

    @Column(name = "remark",length = 200)
    private String remark; //备注

    @Column(name = "discount",length = 5)
    private String discount; //分销折扣

    @Column(name = "hope_discount",length = 5)
    private String hopeDiscount; //分销商期望分销折扣

    @Column(name = "product_id")
    private Long productId;//发起申请分销的商品

    @Column(name = "discount_status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private AuditStatusEnum discountStatus; //申请调价审核状态

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public AuditStatusEnum getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(AuditStatusEnum auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getParentOrganizationId() {
        return parentOrganizationId;
    }

    public void setParentOrganizationId(Long parentOrganizationId) {
        this.parentOrganizationId = parentOrganizationId;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getHopeDiscount() {
        return hopeDiscount;
    }

    public void setHopeDiscount(String hopeDiscount) {
        this.hopeDiscount = hopeDiscount;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public AuditStatusEnum getDiscountStatus() {
        return discountStatus;
    }

    public void setDiscountStatus(AuditStatusEnum discountStatus) {
        this.discountStatus = discountStatus;
    }
}
