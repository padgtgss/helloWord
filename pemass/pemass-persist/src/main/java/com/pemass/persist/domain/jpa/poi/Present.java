/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.jpa.poi;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.AuditStatusEnum;
import com.pemass.persist.enumeration.PresentPackTypeEnum;
import com.pemass.persist.enumeration.PresentSourceTypeEnum;
import com.pemass.persist.enumeration.PresentStatusEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * @Description: 红包
 * @Author: estn.zuo
 * @CreateTime: 2014-11-25 11:27
 */

@Entity
@Table(name = "poi_present")
public class Present extends BaseDomain {

    @Column(name = "present_pack_id")
    private Long presentPackId;    //红包批次

    @Column(name = "present_name", nullable = false, length = 50)
    private String presentName;    // 红包名称 冗余字段，继承自PresentPack

    @Column(name = "user_id")
    private Long userId;  // 用户 (红包所有者，发给用户的时候)

    @Column(name = "present_source_type", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private PresentSourceTypeEnum presentSourceType;    //红包来源类型

    @Column(name = "source_name", length = 200, nullable = false)
    private String sourceName; // 来源名称(如果是商户包就是商户的名称，如果是用户就是用户的名称)

    @Column(name = "present_status", length = 1, nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private PresentStatusEnum presentStatus; // 红包状态

    @Column(name = "audit_status", length = 1, nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private AuditStatusEnum auditStatus;    // 审核状态 冗余字段，继承自PresentPack

    @Column(name = "present_pack_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PresentPackTypeEnum presentPackType;    //红包类型 冗余字段，继承自PresentPack

    @Column(name = "instruction", length = 200)
    private String instruction; // 使用说明 冗余字段，继承自PresentPack

    @Column(name = "area", length = 4000)
    private String area;    // 区域 冗余字段，继承自PresentPack

    @Column(name = "issue_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date issueTime;  //发行时间

    @Column(name = "expiry_time", nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date expiryTime;    // 过期时间 冗余字段，继承自PresentPack

    //=========getter and setter========


    public Long getPresentPackId() {
        return presentPackId;
    }

    public void setPresentPackId(Long presentPackId) {
        this.presentPackId = presentPackId;
    }

    public String getPresentName() {
        return presentName;
    }

    public void setPresentName(String presentName) {
        this.presentName = presentName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public PresentStatusEnum getPresentStatus() {
        return presentStatus;
    }

    public void setPresentStatus(PresentStatusEnum presentStatus) {
        this.presentStatus = presentStatus;
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

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
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

    public PresentSourceTypeEnum getPresentSourceType() {
        return presentSourceType;
    }

    public void setPresentSourceType(PresentSourceTypeEnum presentSourceType) {
        this.presentSourceType = presentSourceType;
    }
}
