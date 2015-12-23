/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.domain.jpa.ec;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.ClearSourceEnum;
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
 * @Description: 清分表
 * @Author: estn.zuo
 * @CreateTime: 2015-08-13 21:19
 */
@Entity
@Table(name = "ec_clearing")
public class Clearing extends BaseDomain {

    @Column(name = "clear_source", nullable = false)
    @Enumerated(EnumType.STRING)
    private ClearSourceEnum clearSource;    //清分来源

    @Column(name = "source_target_id", nullable = false)
    private Long sourceTargetId;    //清分来源ID

    @Column(name = "outgo_bank_card_id", nullable = false)
    private Long outgoBankCardId;   //出账银行卡ID

    @Column(name = "income_bank_card_id", nullable = false)
    private Long incomeBankCardId;  //入账银行卡ID

    @Column(name = "amount", nullable = false)
    private Double amount;   //金额

    @Column(name = "target_bank_card_id", nullable = false)
    private Long targetBankCardId;//商户或者积分通分公司银行id

    @Column(name = "target_amount", nullable = false)
    private Double targetAmount;//如果是商户转账到积分通公司值为负数

    @Column(name = "is_settle", nullable = false)
    private Integer isSettle;   //是否清算【0-未清算、1-已清算、2-交易成功、3-数据不全】

    @Column(name = "settlement_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private SettlementRoleEnum settlementRole;  //清分角色

    @Column(name = "settlement_role_target_id", nullable = false)
    private Long settlementRoleTargetId;    //清分角色ID

    @Column(name = "transaction_account_type")
    @Enumerated(EnumType.ORDINAL)
    private TransactionAccountTypeEnum transactionAccountTypeEnum;  //结算角色类型

    @Column(name = "province_id")
    private Long provinceId; //省

    @Column(name = "city_id")
    private Long cityId;    //市

    @Column(name = "district_id")
    private Long districtId;    //区

    @Column(name = "settlement_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date settlementDate;  //清分执行的时间

    @Column(name = "parent_id")
    private Long parentId;    //关联id

    @Column(name = "remark")
    private String remark;    //备注

    public ClearSourceEnum getClearSource() {
        return clearSource;
    }

    public void setClearSource(ClearSourceEnum clearSource) {
        this.clearSource = clearSource;
    }

    public Long getSourceTargetId() {
        return sourceTargetId;
    }

    public void setSourceTargetId(Long sourceTargetId) {
        this.sourceTargetId = sourceTargetId;
    }

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


    public Integer getIsSettle() {
        return isSettle;
    }

    public void setIsSettle(Integer isSettle) {
        this.isSettle = isSettle;
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

    public Date getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(Date settlementDate) {
        this.settlementDate = settlementDate;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(Double targetAmount) {
        this.targetAmount = targetAmount;
    }
}
