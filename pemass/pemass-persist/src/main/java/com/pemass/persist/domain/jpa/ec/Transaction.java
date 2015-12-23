/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.domain.jpa.ec;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.enumeration.OperateStatusEnum;
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
 * @Description: 交易表
 * @Author: estn.zuo
 * @CreateTime: 2015-08-13 21:59
 */
@Entity
@Table(name = "ec_transaction")
public class Transaction extends BaseDomain {

    @Column(name = "transaction_identifier", length = 20)
    private String transactionIdentifier;   //交易编号

    @Column(name = "outgo_bank_card_id", nullable = false)
    private Long outgoBankCardId;   //出账银行卡ID

    @Column(name = "income_bank_card_id", nullable = false)
    private Long incomeBankCardId;  //入账银行卡ID

    @Column(name = "target_bank_card_id", nullable = false)
    private Long targetBankCardId;//商户或者积分通分公司银行id

    @Column(name = "payable_amount", nullable = false)
    private Double payableAmount;   //应付金额

    @Column(name = "amount", nullable = false)
    private Double amount;   //实付金额

    @Column(name = "settlement_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date settlementTime;    //清算时间

    @Column(name = "request_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestTime;   //交易开始时间

    @Column(name = "response_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date responseTime;  //交易结束时间

    @Column(name = "transaction_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OperateStatusEnum transactionStatus;    //交易状态

    @Column(name = "transaction_account_type")
    @Enumerated(EnumType.ORDINAL)
    private TransactionAccountTypeEnum transactionAccountTypeEnum;  //交易角色类型

    @Column(name = "transaction_account_type_id")
    private Long transactionAccountTypeId;    //交易角色ID

    @Column(name = "charge")
    private Double charge;   //手续费

    @Column(name = "original_data", length = 4000)
    private String originalData;    //交易返回原始数据

    @Column(name = "fail_reason", length = 50)
    private String failReason;  //交易失败原因

    @Column(name = "province_id")
    private Long provinceId; //省

    @Column(name = "city_id")
    private Long cityId;    //市

    @Column(name = "district_id")
    private Long districtId;    //区


    public String getTransactionIdentifier() {
        return transactionIdentifier;
    }

    public void setTransactionIdentifier(String transactionIdentifier) {
        this.transactionIdentifier = transactionIdentifier;
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

    public Date getSettlementTime() {
        return settlementTime;
    }

    public void setSettlementTime(Date settlementTime) {
        this.settlementTime = settlementTime;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public Date getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }

    public OperateStatusEnum getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(OperateStatusEnum transactionStatus) {
        this.transactionStatus = transactionStatus;
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

    public Double getCharge() {
        return charge;
    }

    public void setCharge(Double charge) {
        this.charge = charge;
    }

    public String getOriginalData() {
        return originalData;
    }

    public void setOriginalData(String originalData) {
        this.originalData = originalData;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
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

    public Long getTransactionAccountTypeId() {
        return transactionAccountTypeId;
    }

    public void setTransactionAccountTypeId(Long transactionAccountTypeId) {
        this.transactionAccountTypeId = transactionAccountTypeId;
    }
}
