/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.domain.jpa.sys;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.AuditStatusEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * @Description: 银行卡信息
 * @Author: estn.zuo
 * @CreateTime: 2015-07-31 11:12
 */
@Entity
@Table(name = "sys_bank_card")
public class BankCard extends BaseDomain {

    @Column(name = "target_uuid", length = 32, nullable = false)
    private String targetUUID;    //目标UUID

    @Column(name = "cardholder_name", length = 50)
    private String cardholderName;  //持卡人姓名

    @Column(name = "card_no", length = 100, nullable = false)
    private String cardNo; //卡号(加密存储)

    @Column(name = "bank_name", length = 50)
    private String bankName;    //开户行名称

    @Column(name = "bank_id", length = 4)
    private String bankId;  //开户ID

    @Column(name = "province_id", length = 50)
    private Long provinceId;    //开户地(省)

    @Column(name = "city_id", length = 50)
    private Long cityId;    //开户地(市)

    @Column(name = "cert_type", length = 2)
    private String certType;    //证件类型

    @Column(name = "cert_no", length = 100, nullable = false)
    private String certNo;      //证件号(加密存储)

    @Column(name = "agreement_url", length = 200)
    private String agreementUrl;    //扣款协议URL

    @Column(name = "is_public_account", length = 1)
    private Integer isPublicAccount;    //是否为对公账户【0-对公账户、1-个人账户】

    @Column(name = "cp_audit_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AuditStatusEnum cpAuditStatus; //CP审核状态

    //=================== getter and setter =========

    public String getTargetUUID() {
        return targetUUID;
    }

    public void setTargetUUID(String targetUUID) {
        this.targetUUID = targetUUID;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getAgreementUrl() {
        return agreementUrl;
    }

    public void setAgreementUrl(String agreementUrl) {
        this.agreementUrl = agreementUrl;
    }

    public Integer getIsPublicAccount() {
        return isPublicAccount;
    }

    public void setIsPublicAccount(Integer isPublicAccount) {
        this.isPublicAccount = isPublicAccount;
    }

    public AuditStatusEnum getCpAuditStatus() {
        return cpAuditStatus;
    }

    public void setCpAuditStatus(AuditStatusEnum cpAuditStatus) {
        this.cpAuditStatus = cpAuditStatus;
    }
}
