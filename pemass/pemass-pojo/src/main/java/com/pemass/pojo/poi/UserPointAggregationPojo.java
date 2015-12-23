/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.pojo.poi;

import com.pemass.persist.enumeration.AuditStatusEnum;

import java.util.Date;

/**
 * @Description: 积分聚合POJO
 * @Author: zhou hang
 * @CreateTime: 2014-11-25 18:14
 */
public class UserPointAggregationPojo {

    private Long userId;//C端用户ID
    private String username;

    /*-- E通币统计信息 --*/
    private Integer useableAmountE = 0;    //总可用E通币

    private Integer freezeUseableAmountE = 0; //E通币冻结数

    /*-- E积分统计信息 --*/
    private Integer useableAmountP = 0;   //总可用E积分（定向+通用）  generalAmountP + directionAmountP

    private Integer generalAmountP = 0;   //通用E积分

    private Integer directionAmountP = 0;  //定向E积分

    private Integer freezeUseableAmountP = 0; //E积分冻结数  freezeGeneralAmountP + freezeDirectionAmountP

    private Integer freezeGeneralAmountP = 0;  //通用E积分冻结数

    private Integer freezeDirectionAmountP = 0;  //定向E积分冻结数

    /*-- 一元购积分统计信息 --*/
    private Integer useableAmountO = 0;    //壹购E积分余额

    private Integer givingUseableAmountO = 0;  //朋友赠送壹购E积分余额

    private Integer freezeGivingUseableAmountO = 0;  //朋友赠送壹购E积分冻结数量

    private Double creditAmount;   //总贷款

    private Double onePointProfitTotal;   //已赚还款数

    private Date endTime;   //授信结束日

    private AuditStatusEnum auditStatus;  //授信审核状态

    //======================getter and setter =================


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getUseableAmountE() {
        return useableAmountE;
    }

    public void setUseableAmountE(Integer useableAmountE) {
        this.useableAmountE = useableAmountE;
    }

    public Integer getFreezeUseableAmountE() {
        return freezeUseableAmountE;
    }

    public void setFreezeUseableAmountE(Integer freezeUseableAmountE) {
        this.freezeUseableAmountE = freezeUseableAmountE;
    }

    public Integer getUseableAmountP() {
        return useableAmountP;
    }

    public void setUseableAmountP(Integer useableAmountP) {
        this.useableAmountP = useableAmountP;
    }

    public Integer getGeneralAmountP() {
        return generalAmountP;
    }

    public void setGeneralAmountP(Integer generalAmountP) {
        this.generalAmountP = generalAmountP;
    }

    public Integer getDirectionAmountP() {
        return directionAmountP;
    }

    public void setDirectionAmountP(Integer directionAmountP) {
        this.directionAmountP = directionAmountP;
    }

    public Integer getFreezeUseableAmountP() {
        return freezeUseableAmountP;
    }

    public void setFreezeUseableAmountP(Integer freezeUseableAmountP) {
        this.freezeUseableAmountP = freezeUseableAmountP;
    }

    public Integer getFreezeGeneralAmountP() {
        return freezeGeneralAmountP;
    }

    public void setFreezeGeneralAmountP(Integer freezeGeneralAmountP) {
        this.freezeGeneralAmountP = freezeGeneralAmountP;
    }

    public Integer getFreezeDirectionAmountP() {
        return freezeDirectionAmountP;
    }

    public void setFreezeDirectionAmountP(Integer freezeDirectionAmountP) {
        this.freezeDirectionAmountP = freezeDirectionAmountP;
    }

    public Integer getUseableAmountO() {
        return useableAmountO;
    }

    public void setUseableAmountO(Integer useableAmountO) {
        this.useableAmountO = useableAmountO;
    }

    public Integer getGivingUseableAmountO() {
        return givingUseableAmountO;
    }

    public void setGivingUseableAmountO(Integer givingUseableAmountO) {
        this.givingUseableAmountO = givingUseableAmountO;
    }

    public Integer getFreezeGivingUseableAmountO() {
        return freezeGivingUseableAmountO;
    }

    public void setFreezeGivingUseableAmountO(Integer freezeGivingUseableAmountO) {
        this.freezeGivingUseableAmountO = freezeGivingUseableAmountO;
    }

    public Double getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(Double creditAmount) {
        this.creditAmount = creditAmount;
    }

    public Double getOnePointProfitTotal() {
        return onePointProfitTotal;
    }

    public void setOnePointProfitTotal(Double onePointProfitTotal) {
        this.onePointProfitTotal = onePointProfitTotal;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public AuditStatusEnum getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(AuditStatusEnum auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}