/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.domain.jpa.poi;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.CollectMoneySchemeEnum;
import com.pemass.persist.enumeration.PointTypeEnum;

import javax.persistence.*;

/**
 * @Description: 收款策略赠送积分所使用的方案（对新增收款策略方案具体内容的一个记录）
 * @Author: he jun cheng
 * @CreateTime: 2014-12-24 16:05
 */
@Entity
@Table(name = "poi_collect_money_scheme")
public class CollectMoneyScheme extends BaseDomain {

    @Column(name = "point_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PointTypeEnum pointType;    //赠送的积分类型

    @Column(name = "scheme_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CollectMoneySchemeEnum schemeType;  //方案类型

    @Column(name = "mini_amount")
    private Integer miniAmount;   //最低消费数（达到最低消费才执行送积分）

    @Column(name = "mini_give_amount")
    private Integer miniGiveAmount;   //达到最低消费是赠送的积分（实送积分按照 minGiveAmount/minAmount 进行计算）

    @Column(name = "conversion_factor")
    private Double conversionFactor;    //此方案所送积分与实付款的折算率

    @Column(name = "immobilization_presented")
    private Integer immobilizationPresented; //固定赠送积分


    //======================== getter and setter ========================\\

    public PointTypeEnum getPointType() {
        return pointType;
    }

    public void setPointType(PointTypeEnum pointType) {
        this.pointType = pointType;
    }

    public CollectMoneySchemeEnum getSchemeType() {
        return schemeType;
    }

    public void setSchemeType(CollectMoneySchemeEnum schemeType) {
        this.schemeType = schemeType;
    }

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
}