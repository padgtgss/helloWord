/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.jpa.biz;

import com.pemass.common.server.domain.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @Description: 审核记录表
 * @Author: terry.luo
 */
@Entity
@Table(name = "bas_audit_record")
public class AuditRecord extends BaseDomain {

    @Column(name = "organization_name", length = 50)
    private String  organizationName;   // 公司名称

    @Column(name = "organization_id")
    private Long organizationId; //受理的一元购商户id

    @Column(name = "product_id")
    private Long productId; //一元购商品id

    @Column(name = "one_audit_status")
    private Integer  oneAuditStatus; // 受理审核状态（1、未审核，2、审核成功，3、审核失败）

    @Column(name = "apply_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date applyTime;   // 申请时间

    @Column(name = "reason")
    private String  reason;  // 失败原因

    @Column(name = "audit_type")
    private Integer  auditType; // 是申请一元购商品还是受理一元购(1、商品申请成为一元购商品 2、商户申请受理一元购积分 3、申请一元购支付)

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getOneAuditStatus() {
        return oneAuditStatus;
    }

    public void setOneAuditStatus(Integer oneAuditStatus) {
        this.oneAuditStatus = oneAuditStatus;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getAuditType() {
        return auditType;
    }

    public void setAuditType(Integer auditType) {
        this.auditType = auditType;
    }
}
