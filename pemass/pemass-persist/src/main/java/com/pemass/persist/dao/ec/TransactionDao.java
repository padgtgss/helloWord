/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.dao.ec;

import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.ec.Transaction;

import java.util.List;

/**
 * @Description: Transaction
 * @Author: oliver.he
 * @CreateTime: 2015-08-21 15:03
 */
public interface TransactionDao extends BaseDao {

    /**
     * 定时任务获取满足交易条件数据
     * @return
     */
    List<Transaction> getTransactionListByDate();

    /**
     * 获取当天定时任务执行交易处理中的数据的数据
     * @return
     */
    List<Transaction> getTransactionListNone();
}