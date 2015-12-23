/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.sys;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.sys.TerminalUser;

import java.util.List;
import java.util.Map;

/**
 * @Description: 收银员、检票员dao
 * @Author: cassiel.liu
 * @CreateTime: 2014-12-08 14:51
 */
public interface TerminalUserDao extends BaseDao {

    /**
     * 根据条件获取所有的收银员id
     *
     * @param organizationId 机构id
     * @param isDistribution 是否分销（Y-分销，N-直销）
     * @return
     */
    List getAllCashierIdByField(Long organizationId, String isDistribution);

    TerminalUser hasBeing(Class clazz, String name);

    /**
     * 获取满足条件的商户
     *
     * @param conditions
     * @param domainPage
     * @return
     */
    DomainPage<Object[]> getPagesByConditions(Map<String, Object> conditions, DomainPage domainPage);

}
