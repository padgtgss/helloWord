/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.dao.sys;

import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.domain.BaseDomain;

/**
 * @Description: BankCardDao
 * @Author: vahoa.ma
 * @CreateTime: 2015-09-23 14:05
 */
public interface BankCardDao extends BaseDao{

    /**
     * 插入一条银行卡信息
     *
     * @param t
     * @param <T>
     */
    public abstract <T extends BaseDomain> void persist(T t);

    /**
     * 根据targetUUID查询未被通过的银行卡
     *
     * @param clazz
     * @param fieldName
     * @param fieldValue
     * @param <T>
     * @return
     */
    public abstract <T extends BaseDomain> T getEntityByField(Class<T> clazz, String fieldName, Object fieldValue);
}