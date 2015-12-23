/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.dao.ec.impl;

import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.ec.TransactionDao;
import com.pemass.persist.domain.jpa.ec.Transaction;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

/**
 * @Description: TransactionImpl
 * @Author: oliver.he
 * @CreateTime: 2015-08-21 15:03
 */
@Repository
public class TransactionDaoImpl extends JPABaseDaoImpl implements TransactionDao {

    @Override
    public List<Transaction> getTransactionListByDate() {
        String sql = "select  t from Transaction t where t.available = 1 and t.transactionStatus='NONE'";
        Query query = em.createQuery(sql);
        List result = query.getResultList();
        return result;
    }

    @Override
    public List<Transaction> getTransactionListNone() {
        String sql = "select  t from Transaction t where t.available = 1 and t.transactionStatus = 'ING'";
        Query query = em.createQuery(sql);
        List result = query.getResultList();
        return result;
    }
}