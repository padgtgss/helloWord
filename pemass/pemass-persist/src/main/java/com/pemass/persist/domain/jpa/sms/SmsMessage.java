/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.jpa.sms;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.SmsTypeEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * @Description: SmsMessage
 * @Author: zhou hang
 * @CreateTime: 2015-01-20 09:54
 */
@Entity
@Table(name = "bas_sms_message")
public class SmsMessage extends BaseDomain {
    //接收号码、短信内容、短信验证码、验证码分类、过期时间、是否使用、发送成功与否、接口返回原始数据】
    @Column(name = "receive_number", nullable = false, length = 50)
    private String receiveNumber; //接收号码

    @Column(name = "content",  length = 1000)
    private String content; //短信内容

    @Column(name = "validate_code", length = 12)
    private String validateCode; //短信验证码

    @Column(name = "sms_type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private SmsTypeEnum smsType; //信息分类

    @Column(name = "expiry_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryTime; //过期时间

    @Column(name = "is_used", nullable = false)
    private Integer isUsed; //是否使用  [0:未使用、1:使用]

    @Column(name = "is_success", nullable = false)
    private Integer isSuccess; //发送成功与否  [0:失败、1:成功]

    @Column(name = "original_data",  length = 1000)
    private String originalData; //接口返回原始数据

    //==================getter setter==============================

    public String getReceiveNumber() {
        return receiveNumber;
    }

    public void setReceiveNumber(String receiveNumber) {
        this.receiveNumber = receiveNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }

    public SmsTypeEnum getSmsType() {
        return smsType;
    }

    public void setSmsType(SmsTypeEnum smsType) {
        this.smsType = smsType;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    public Integer getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Integer isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getOriginalData() {
        return originalData;
    }

    public void setOriginalData(String originalData) {
        this.originalData = originalData;
    }
}