package com.pemass.service.pemass.ec.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.ec.TransactionSettlement;
import com.pemass.service.pemass.ec.TransactionSettlementService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: TransactionSettlementServiceImpl
 * @Author: cassiel.liu
 * @CreateTime: 2015-08-28 17:57
 */
@Service
public class TransactionSettlementServiceImpl implements TransactionSettlementService {

    @Resource
    private BaseDao jpaBaseDao;


    @Override
    public List<Long> selectSettlementIdsByTransactionId(Long transactionId) {
        Preconditions.checkNotNull(transactionId);
        List<Long> ids = Lists.newArrayList();
        List<TransactionSettlement> transactionSettlements = jpaBaseDao.getEntitiesByField(TransactionSettlement.class, "transactionId", transactionId);
        if (transactionSettlements.size() < 1) {
            ids.add(0L);
        } else {
            for (TransactionSettlement transactionSettlement : transactionSettlements) {
                ids.add(transactionSettlement.getSettlementId());
            }
        }
        return ids;
    }

    @Override
    public void saveTransactionSettlement(TransactionSettlement transactionSettlement) {
        Preconditions.checkNotNull(transactionSettlement);
        jpaBaseDao.persist(transactionSettlement);
    }
}