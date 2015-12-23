/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.sys;
import com.pemass.persist.enumeration.AuditStatusEnum;
import com.pemass.persist.enumeration.PayStatusEnum;

import java.util.Date;

/**
 * @Description: CreditRecordPojo
 * @Author: lin.shi
 * @CreateTime: 2015-08-05 19:11
 */
public class CreditRecordPojo {


    private Long userId;    //所属用户

    private String name;    //姓名

    private String telephone;   //联系电话

    private String bankAccount; //银行卡号

    private String officeLocation;  //工作地址

    private String location;    //详细地址

    private String legalIdcard; //身份证号码

    private Date startTime; //授信开始时间

    private Date endTime;   //授信结束时间

    private Double creditLimit;    //授信额度

    private Double useableCreditLimit;   //可用授信额度

    private AuditStatusEnum auditStatus; //授信审核状态

    private PayStatusEnum payStatus;   //还款状态


    //===============================getter and setter=============================================

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