package com.pemass.persist.domain.jpa.poi;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.AuditStatusEnum;
import com.pemass.persist.enumeration.PresentPackTypeEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * reated by hejch on 2014/10/11.
 * 红包批次
 */
@Entity
@Table(name = "poi_present_pack")
public class PresentPack extends BaseDomain {

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;    // 账户ID

    @Column(name = "pack_identifier", nullable = false, unique = true, length = 50)
    private String packIdentifier;  // 批次编号

    @Column(name = "present_pack_name", nullable = false, length = 50)
    private String presentPackName;    // 红包名称

    @Column(name = "amount", nullable = false)
    private Integer amount;     // 红包总个数

    @Column(name = "total_point_e", nullable = true)
    private Integer totalPointE;   // 消耗总E通币数

    @Column(name = "total_point_p", nullable = true)
    private Integer totalPointP;   // 消耗总E积分数

    @Column(name = "instruction", length = 200)
    private String instruction;  //使用说明

    @Column(name = "audit_status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private AuditStatusEnum auditStatus;    //审核状态【0-未审核、1-审核中、2-已审核、3-审核失败】

    @Column(name = "present_pack_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PresentPackTypeEnum presentPackType;    // 红包类型

    @Column(name = "area", length = 4000, nullable = false)
    private String area;    // 区域

    @Column(name = "issue_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date issueTime;  //发行时间

    @Column(name = "expiry_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryTime;    //过期时间

    //=================== getter and setter =========


    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getPackIdentifier() {
        return packIdentifier;
    }

    public void setPackIdentifier(String packIdentifier) {
        this.packIdentifier = packIdentifier;
    }

    public String getPresentPackName() {
        return presentPackName;
    }

    public void setPresentPackName(String presentPackName) {
        this.presentPackName = presentPackName;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getTotalPointE() {
        return totalPointE;
    }

    public void setTotalPointE(Integer totalPointE) {
        this.totalPointE = totalPointE;
    }

    public Integer getTotalPointP() {
        return totalPointP;
    }

    public void setTotalPointP(Integer totalPointP) {
        this.totalPointP = totalPointP;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public AuditStatusEnum getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(AuditStatusEnum auditStatus) {
        this.auditStatus = auditStatus;
    }

    public PresentPackTypeEnum getPresentPackType() {
        return presentPackType;
    }

    public void setPresentPackType(PresentPackTypeEnum presentPackType) {
        this.presentPackType = presentPackType;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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
}
