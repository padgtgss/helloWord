package com.pemass.service.pemass.ec;

import com.pemass.persist.domain.jpa.ec.TransactionSettlement;

import java.util.List;

/**
 * @Description: TransactionSettlementService
 * @Author: cassiel.liu
 * @CreateTime: 2015-08-28 17:57
 */
public interface TransactionSettlementService {

    /**
     *   根据“交易”id查询和器相关的“结算”id
     * @param transactionId
     * @return
     */
    List<Long> selectSettlementIdsByTransactionId(Long transactionId);

    /**
     * 保存结算-交易中间表信息
     * @param transactionSettlement
     */
    void saveTransactionSettlement(TransactionSettlement transactionSettlement);
}
