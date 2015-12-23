/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.poi;

/**
 * @Description: CollectMoneySchemePojo
 * @Author: zhou hang
 * @CreateTime: 2014-12-25 16:43
 */
public class CollectMoneySchemePojo {


    private Integer miniAmount;   //最低消费数（达到最低消费才执行送积分）

    private Integer miniGiveAmount;   //达到最低消费是赠送的积分（实送积分按照 minGiveAmount/minAmount 进行计算）

    private Double conversionFactor;    //此方案所送积分与实付款的折算率

    private Integer immobilizationPresented; //固定赠送积分

    private String schemeType;//方案类型


    //========================getter setter=====================


    public Integer getMiniAmount() {
        return miniAmount;
    }

    public void setMiniAmount(Integer miniAmount) {
        this.miniAmount = miniAmount;
    }

    public Integer getMiniGiveAmount() {
        return miniGiveAmount;
    }

    public void setMiniGiveAmount(Integer miniGiveAmount) {
        this.miniGiveAmount = miniGiveAmount;
    }

    public Double getConversionFactor() {
        return conversionFactor;
    }

    public void setConversionFactor(Double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public Integer getImmobilizationPresented() {
        return immobilizationPresented;
    }

    public void setImmobilizationPresented(Integer immobilizationPresented) {
        this.immobilizationPresented = immobilizationPresented;
    }

    public String getSchemeType() {
        return schemeType;
    }

    public void setSchemeType(String schemeType) {
        this.schemeType = schemeType;
    }
}