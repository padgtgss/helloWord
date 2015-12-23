/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.service.pemass.ec;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.ec.Transaction;

import java.util.List;
import java.util.Map;

/**
 * @Description: TransactionService
 * @Author: oliver.he
 * @CreateTime: 2015-08-21 15:08
 */
public interface TransactionService {

    void insert(Transaction transaction);

    Transaction update(Transaction source);

    Transaction getById(Long transactionID);

    Transaction delete(Long transactionID);

    /**
     * 分页获取满足条件的纪录
     *
     * @param conditions 查询条件
     * @param domainPage 分页信息
     * @return 返回结果
     */
    DomainPage<Map<String, Object>> search(Map<String, Object> conditions, DomainPage domainPage);

    /**
     * 获取满足条件的所有结果
     *
     * @param conditions 查询条件
     * @return 返回结果
     */
    List<Map<String, Object>> selectAllTransaction(Map<String, Object> conditions);

    /**
     * 获取满足条件的交易表数据
     *
     * @return
     */
    List<Transaction> getTransactionListByDate();
}