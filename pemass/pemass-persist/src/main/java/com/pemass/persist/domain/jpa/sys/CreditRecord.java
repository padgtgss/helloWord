/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.domain.jpa.sys;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.AuditStatusEnum;
import com.pemass.persist.enumeration.PayStatusEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @Description: 授信申请记录表
 * @Author: estn.zuo
 * @CreateTime: 2015-07-13 11:11
 */
@Entity
@Table(name = "sys_credit_record")
public class CreditRecord extends BaseDomain {

    @Column(name = "user_id", nullable = false)
    private Long userId;    //所属用户

    @Column(name = "name", length = 50, nullable = false)
    private String name;    //姓名

    @Column(name = "telephone", length = 20, nullable = false)
    private String telephone;   //联系电话

    @Column(name = "bank_account", length = 100, nullable = false)
    private String bankAccount; //银行卡号

    @Column(name = "office_location", length = 50, nullable = false)
    private String officeLocation;  //工作地址

    @Column(name = "location", length = 50, nullable = false)
    private String location;    //详细地址

    @Column(name = "legal_idcard", length = 50, nullable = false)
    private String legalIdcard; //身份证号码

    @Column(name = "legal_idcard_url", length = 200, nullable = false)
    private String legalIdcardUrl;  //身份证正面

    @Column(name = "legal_idcard_back_url", length = 200, nullable = false)
    private String legalIdcardBackUrl;  //身份证反面

    @Column(name = "face_url", length = 200, nullable = false)
    private String faceUrl; //脸部照片

    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime; //授信开始时间

    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;   //授信结束时间

    @Column(name = "credit_limit")
    private Double creditLimit;    //授信额度

    @Column(name = "useable_credit_limit")
    private Double useableCreditLimit;   //可用授信额度

    @Column(name = "is_valid", nullable = false)
    private Integer isValid;    //是否有效，针对每一个用户只有一条记录有效

    @Column(name = "audit_status", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private AuditStatusEnum auditStatus; //授信审核状态

    @Enumerated(EnumType.STRING)
    @Column(name = "pay_status", length = 50, nullable = false)
    private PayStatusEnum payStatus;   //还款状态


    //=================== getter and setter =========


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLegalIdcard() {
        return legalIdcard;
    }

    public void setLegalIdcard(String legalIdcard) {
        this.legalIdcard = legalIdcard;
    }

    public String getLegalIdcardUrl() {
        return legalIdcardUrl;
    }

    public void setLegalIdcardUrl(String legalIdcardUrl) {
        this.legalIdcardUrl = legalIdcardUrl;
    }

    public String getLegalIdcardBackUrl() {
        return legalIdcardBackUrl;
    }

    public void setLegalIdcardBackUrl(String legalIdcardBackUrl) {
        this.legalIdcardBackUrl = legalIdcardBackUrl;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Double getUseableCreditLimit() {
        return useableCreditLimit;
    }

    public void setUseableCreditLimit(Double useableCreditLimit) {
        this.useableCreditLimit = useableCreditLimit;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public AuditStatusEnum getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(AuditStatusEnum auditStatus) {
        this.auditStatus = auditStatus;
    }

    public PayStatusEnum getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(PayStatusEnum payStatus) {
        this.payStatus = payStatus;
    }
}
