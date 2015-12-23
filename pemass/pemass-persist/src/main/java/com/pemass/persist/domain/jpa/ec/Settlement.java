/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.domain.jpa.ec;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.OperateStatusEnum;
import com.pemass.persist.enumeration.SettlementRoleEnum;
import com.pemass.persist.enumeration.TransactionAccountTypeEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @Description: 清算表
 * @Author: estn.zuo
 * @CreateTime: 2015-08-13 21:41
 */
@Entity
@Table(name = "ec_settlement")
public class Settlement  extends BaseDomain{

    @Column(name = "outgo_bank_card_id", nullable = false)
    private Long outgoBankCardId;   //出账银行卡ID

    @Column(name = "income_bank_card_id", nullable = false)
    private Long incomeBankCardId;  //入账银行卡ID

    @Column(name = "payable_amount", nullable = false)
    private Double payableAmount;   //应付金额

    @Column(name = "amount", nullable = false)
    private Double amount;   //实付金额

    @Column(name = "target_bank_card_id", nullable = false)
    private Long targetBankCardId;//商户或者积分通分公司银行id

    @Column(name = "target_amount", nullable = false)
    private Double targetAmount;//如果是商户转账到积分通公司值为负数

    @Column(name = "settlement_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date settlementTime;    //清算时间

    @Column(name = "settlement_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OperateStatusEnum settlementStatus;    //清算状态

    @Column(name = "settlement_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private SettlementRoleEnum settlementRole;  //结算角色

    @Column(name = "settlement_role_target_id", nullable = false)
    private Long settlementRoleTargetId;    //结算角色ID

    @Column(name = "transaction_account_type")
    @Enumerated(EnumType.ORDINAL)
    private TransactionAccountTypeEnum transactionAccountTypeEnum;  //结算角色类型

    @Column(name = "province_id")
    private Long provinceId; //省

    @Column(name = "city_id")
    private Long cityId;    //市

    @Column(name = "district_id")
    private Long districtId;    //区

    @Column(name = "parent_id")
    private Long parentId;    //关联id

    @Column(name = "remark")
    private String remark;    //备注

    public Long getOutgoBankCardId() {
        return outgoBankCardId;
    }

    public void setOutgoBankCardId(Long outgoBankCardId) {
        this.outgoBankCardId = outgoBankCardId;
    }

    public Long getIncomeBankCardId() {
        return incomeBankCardId;
    }

    public void setIncomeBankCardId(Long incomeBankCardId) {
        this.incomeBankCardId = incomeBankCardId;
    }

    public Date getSettlementTime() {
        return settlementTime;
    }

    public void setSettlementTime(Date settlementTime) {
        this.settlementTime = settlementTime;
    }

    public OperateStatusEnum getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(OperateStatusEnum settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public Double getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(Double payableAmount) {
        this.payableAmount = payableAmount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setTargetAmount(Double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public SettlementRoleEnum getSettlementRole() {
        return settlementRole;
    }

    public void setSettlementRole(SettlementRoleEnum settlementRole) {
        this.settlementRole = settlementRole;
    }

    public Long getSettlementRoleTargetId() {
        return settlementRoleTargetId;
    }

    public void setSettlementRoleTargetId(Long settlementRoleTargetId) {
        this.settlementRoleTargetId = settlementRoleTargetId;
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

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getTargetBankCardId() {
        return targetBankCardId;
    }

    public void setTargetBankCardId(Long targetBankCardId) {
        this.targetBankCardId = targetBankCardId;
    }

    public TransactionAccountTypeEnum getTransactionAccountTypeEnum() {
        return transactionAccountTypeEnum;
    }

    public void setTransactionAccountTypeEnum(TransactionAccountTypeEnum transactionAccountTypeEnum) {
        this.transactionAccountTypeEnum = transactionAccountTypeEnum;
    }

    public Double getTargetAmount() {
        return targetAmount;
    }
}
