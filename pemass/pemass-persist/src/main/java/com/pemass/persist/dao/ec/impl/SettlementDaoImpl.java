/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.dao.ec.impl;

import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.ec.SettlementDao;
import com.pemass.persist.domain.jpa.ec.Settlement;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Description: SettlementDaoImpl
 * @Author: oliver.he
 * @CreateTime: 2015-08-21 15:01
 */
@Repository
public class SettlementDaoImpl extends JPABaseDaoImpl implements SettlementDao {

    @Override
    public List<Object[]> getGroupSettlementToTransaction() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = formatter.format(new Date());
        StringBuilder sql = new StringBuilder("SELECT SUM(s.target_amount) target_amount, s.target_bank_card_id,s.income_bank_card_id,s.outgo_bank_card_id,s.settlement_role_target_id,s.transaction_account_type ");
        sql.append(" FROM ec_settlement s where s.available=1 and (s.settlement_status='NONE' or s.settlement_status= 'FAIL') GROUP BY s.target_bank_card_id");
        Query query = em.createNativeQuery(sql.toString());
        List<Object[]> result = query.getResultList();
        return result;
    }

    @Override
    public List<Settlement> getSettlementListByCount(Long targetBankCardId) {
        String sql = "select s from Settlement s where s.available=1 and (s.settlementStatus='NONE' or s.settlementStatus= 'FAIL') and s.targetBankCardId="+targetBankCardId;
        Query query = em.createQuery(sql);
        List result = query.getResultList();
        return result;
    }
}