/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.poi;

import java.util.Date;

/**
 * @Description: CollectMoneyStategyPojo
 * @Author: zhou hang
 * @CreateTime: 2014-12-25 10:48
 */
public class CollectMoneyStategyPojo {

    private Long collectMoneyStrategyId;//策略id

    private String strategyName;    //策略名

    private String username; //用户

    private Date startTime;     //策略有效期 - 起始时间

    private Date endTime;       //策略有效期 - 结束时间

    private Double collectMoneyDiscount;    //收款折扣率

    private Integer paiDeduction;  //是否进行E积分抵扣【0-不抵扣、1-抵扣】  对应isDeductionPointP

    private Long beiDeduction;  //是否进行E通币抵扣【0-不抵扣、1-抵扣】 对应isDeductionPointE

    private Integer isBeiDeduction;   //该商品贝积分抵扣数

    private Integer paiCollectMoneyScheme;  //赠送派积分数

    private Integer beiCollectMoneyScheme;   //赠送贝积分数

    private Double consumptionAmount;   //消费金额;

    private Double totalPrice;   //实付金额;

    private CollectMoneySchemePojo schemePojoP;

    private CollectMoneySchemePojo schemePojoE;

   //======================getter  setter   ========================================


    public Long getCollectMoneyStrategyId() {
        return collectMoneyStrategyId;
    }

    public void setCollectMoneyStrategyId(Long collectMoneyStrategyId) {
        this.collectMoneyStrategyId = collectMoneyStrategyId;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Double getCollectMoneyDiscount() {
        return collectMoneyDiscount;
    }

    public void setCollectMoneyDiscount(Double collectMoneyDiscount) {
        this.collectMoneyDiscount = collectMoneyDiscount;
    }

    public Integer getPaiDeduction() {
        return paiDeduction;
    }

    public void setPaiDeduction(Integer paiDeduction) {
        this.paiDeduction = paiDeduction;
    }

    public Long getBeiDeduction() {
        return beiDeduction;
    }

    public void setBeiDeduction(Long beiDeduction) {
        this.beiDeduction = beiDeduction;
    }

    public Integer getIsBeiDeduction() {
        return isBeiDeduction;
    }

    public void setIsBeiDeduction(Integer isBeiDeduction) {
        this.isBeiDeduction = isBeiDeduction;
    }

    public Integer getPaiCollectMoneyScheme() {
        return paiCollectMoneyScheme;
    }

    public void setPaiCollectMoneyScheme(Integer paiCollectMoneyScheme) {
        this.paiCollectMoneyScheme = paiCollectMoneyScheme;
    }

    public Integer getBeiCollectMoneyScheme() {
        return beiCollectMoneyScheme;
    }

    public void setBeiCollectMoneyScheme(Integer beiCollectMoneyScheme) {
        this.beiCollectMoneyScheme = beiCollectMoneyScheme;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getConsumptionAmount() {
        return consumptionAmount;
    }

    public void setConsumptionAmount(Double consumptionAmount) {
        this.consumptionAmount = consumptionAmount;
    }

    public CollectMoneySchemePojo getSchemePojoP() {
        return schemePojoP;
    }

    public void setSchemePojoP(CollectMoneySchemePojo schemePojoP) {
        this.schemePojoP = schemePojoP;
    }

    public CollectMoneySchemePojo getSchemePojoE() {
        return schemePojoE;
    }

    public void setSchemePojoE(CollectMoneySchemePojo schemePojoE) {
        this.schemePojoE = schemePojoE;
    }
}