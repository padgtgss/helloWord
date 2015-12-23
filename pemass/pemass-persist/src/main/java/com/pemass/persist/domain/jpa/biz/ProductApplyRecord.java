package com.pemass.persist.domain.jpa.biz;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.enumeration.AuditStatusEnum;

import javax.persistence.*;


/**
 * 商品调价申请记录
 *
 * @author luoc
 */
@Entity
@Table(name = "biz_product_apply_record")
public class ProductApplyRecord extends BaseDomain {

    @Column(name = "organization_id")
    private Long organizationId;//商品申请者账户（当前商户作为分销商）

    @Column(name = "product_id")
    private Long productId;//当前商品

    @Column(name = "parent_product_id")
    private Long parentProductId;//父商品ID

    @Column(name = "apply_price")
    private Double applyPrice;//申请分销价格

    @Column(name = "actual_apply_price")
    private Double actualApplyPrice;//商户实际同意的分销价格

    @Column(name = "apply_remark", length = 50)
    private String applyRemark;//申请备注

    @Column(name = "audit_status")
    @Enumerated(EnumType.ORDINAL)
    private AuditStatusEnum auditStatus;//审核状态

    //============================ getter and setter ==========================\\

    public AuditStatusEnum getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(AuditStatusEnum auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getApplyRemark() {
        return applyRemark;
    }

    public void setApplyRemark(String applyRemark) {
        this.applyRemark = applyRemark;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getParentProductId() {
        return parentProductId;
    }

    public void setParentProductId(Long parentProductId) {
        this.parentProductId = parentProductId;
    }

    public Double getActualApplyPrice() {
        return actualApplyPrice;
    }

    public void setActualApplyPrice(Double actualApplyPrice) {
        this.actualApplyPrice = actualApplyPrice;
    }

    public Double getApplyPrice() {
        return applyPrice;
    }

    public void setApplyPrice(Double applyPrice) {
        this.applyPrice = applyPrice;
    }
}
