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
 * @Description: 清算 - 清分中间表
 * @Author: estn.zuo
 * @CreateTime: 2015-08-13 21:57
 */
@Entity
@Table(name = "ec_settlement_clearing")
public class SettlementClearing  extends BaseDomain{

    @Column(name = "settlement_id", nullable = false)
    private Long settlementId;  //清算ID

    @Column(name = "clearing_id", nullable = false)
    private Long clearingId;    //清分ID

    @Column(name = "amount", nullable = false)
    private Double amount;   //金额

    public Long getSettlementId() {
        return settlementId;
    }

    public void setSettlementId(Long settlementId) {
        this.settlementId = settlementId;
    }

    public Long getClearingId() {
        return clearingId;
    }

    public void setClearingId(Long clearingId) {
        this.clearingId = clearingId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
