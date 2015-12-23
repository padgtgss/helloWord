/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.dao.sys.impl;

import com.pemass.common.core.util.UUIDUtil;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.common.server.domain.BaseDomain;
import com.pemass.common.server.enumeration.AvailableEnum;
import com.pemass.persist.dao.sys.BankCardDao;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;


/**
 * @Description: BankCardDaoImpl
 * @Author: vahoa.ma
 * @CreateTime: 2015-09-23 14:10
 */
@Repository
public class BankCardDaoImpl extends JPABaseDaoImpl implements BankCardDao {

    @Override
    public <T extends BaseDomain> void persist(T t) {
        t.setCreateTime(new Date());
        t.setUpdateTime(new Date());
        t.setAvailable(AvailableEnum.UNAVAILABLE);
        t.setUuid(UUIDUtil.randomWithoutBar());
        em.persist(t);
    }

    @Override
    public <T extends BaseDomain> T getEntityByField(Class<T> clazz, String fieldName, Object fieldValue) {
        String sql = "select c from " + clazz.getName() + " c where c.available = 0 and c." + fieldName + " = ?1 order by c.updateTime desc";
        Query query = em.createQuery(sql);
        query.setParameter(1, fieldValue);
        List<T> ret = query.getResultList();
        if (ret == null || ret.size() < 1) {
            return null;
        }
        //TODO how to deal with multiple record.
        return ret.get(0);
    }
}