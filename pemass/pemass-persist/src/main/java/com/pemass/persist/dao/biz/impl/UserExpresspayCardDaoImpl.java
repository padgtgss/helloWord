/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.biz.impl;

import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.biz.UserExpresspayCardDao;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;

/**
 * @Description: UserExpresspayCardDaoImpl
 * @Author: lin.shi
 * @CreateTime: 2015-08-26 18:50
 */
@Repository
public class UserExpresspayCardDaoImpl extends JPABaseDaoImpl implements UserExpresspayCardDao {

    @Override
    public boolean unBindUserExpresspayCard(Long userId, String cardNumber) {
        String sql = "UPDATE UserExpresspayCard SET available = 0 WHERE userId = ?1 and cardNumber = ?2";
        Query query = em.createQuery(sql);
        query.setParameter(1,userId);
        query.setParameter(2,cardNumber);
        query.executeUpdate();
        return true;
    }
}