/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.vo;

import com.pemass.persist.enumeration.SmsTypeEnum;

import java.util.List;

/**
 * @Description: SmsMessageVo
 * @Author: zhou hang
 * @CreateTime: 2015-05-04 10:45
 */
public class SmsMessageVo {

    private String receiveNumber; //接收号码

    private SmsTypeEnum smsType; //验证码分类

    private List<Object> list;

    private String uuid;

    //=======================getter and setter =======================


    public String getReceiveNumber() {
        return receiveNumber;
    }

    public void setReceiveNumber(String receiveNumber) {
        this.receiveNumber = receiveNumber;
    }

    public SmsTypeEnum getSmsType() {
        return smsType;
    }

    public void setSmsType(SmsTypeEnum smsType) {
        this.smsType = smsType;
    }

    public List<Object> getList() {
        return list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}