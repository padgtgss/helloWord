/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.domain.jpa.ec;

import com.pemass.common.server.domain.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Description: 交易 - 清算 中间表
 * @Author: estn.zuo
 * @CreateTime: 2015-08-13 22:11
 */
@Entity
@Table(name = "ec_transaction_settlement")
public class TransactionSettlement extends BaseDomain {

    @Column(name = "settlement_id", nullable = false)
    private Long settlementId;  //清算ID

    @Column(name = "transaction_id", nullable = false)
    private Long transactionId;    //交易ID

    @Column(name = "amount", nullable = false)
    private Double amount;   //金额

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getSettlementId() {
        return settlementId;
    }

    public void setSettlementId(Long settlementId) {
        this.settlementId = settlementId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
