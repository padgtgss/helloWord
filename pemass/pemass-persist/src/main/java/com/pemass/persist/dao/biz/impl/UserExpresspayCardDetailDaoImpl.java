/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.biz.impl;

import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.biz.UserExpresspayCardDetailDao;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;

/**
 * @Description: UserExpresspayCardDetailDaoImpl
 * @Author: lin.shi
 * @CreateTime: 2015-08-27 15:31
 */
@Repository
public class UserExpresspayCardDetailDaoImpl extends JPABaseDaoImpl implements UserExpresspayCardDetailDao {


    @Override
    public Object[] getDepositMoneyCount(Long uid) {
        String sql ="select COALESCE(sum(t.money),0) from UserExpresspayCardDetail t where t.userId = ?1";
        Query query = em.createQuery(sql);
        query.setParameter(1,uid);
        return query.getResultList().toArray();
    }
}