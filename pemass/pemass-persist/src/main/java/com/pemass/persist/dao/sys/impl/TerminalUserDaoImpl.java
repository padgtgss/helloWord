/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.sys.impl;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.sys.TerminalUserDao;
import com.pemass.persist.domain.jpa.sys.TerminalUser;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @Description: TerminalUserDaoImpl
 * @Author: cassiel.liu
 * @CreateTime: 2014-12-08 14:52
 */
@Repository(value = "terminalUserDao")
public class TerminalUserDaoImpl extends JPABaseDaoImpl implements TerminalUserDao {
    @Override
    public List getAllCashierIdByField(Long organizationId, String isDistribution) {
        Query query = null;
        String sql = "select tu.id from TerminalUser tu " +
                " where tu.available = 1 and tu.authority='ROLE_CASHIER' ";
        if ("Y".equalsIgnoreCase(isDistribution.trim())) { //如果是分销
            sql = sql + " and tu.organizationId in (SELECT ar.organizationId from  AccountRelation ar " +
                    " where ar.available = 1 and ar.path LIKE ?1 )";
            query = em.createQuery(sql);
            query.setParameter(1, "/" + organizationId + "/%");
        }
        if ("N".equalsIgnoreCase(isDistribution)) {  //如果是直销
            sql = sql + " and tu.organizationId = ?1";
            query = em.createQuery(sql);
            query.setParameter(1, organizationId);
        }
        List resultList = query.getResultList();
        return resultList;
    }

    @Override
    public TerminalUser hasBeing(Class clazz, String name) {
        String sql = "select c from " + clazz.getName() + " c where c.terminalUsername ='" + name + "'";
        Query query = em.createQuery(sql);
        List<TerminalUser> terminalUsers = query.getResultList();
        if (terminalUsers.size() < 1 || terminalUsers == null) {
            return null;
        } else {
            return terminalUsers.get(0);
        }
    }

    @Override
    public DomainPage<Object[]> getPagesByConditions(Map<String, Object> conditions, DomainPage domainPage) {
        checkNotNull(conditions);
        checkNotNull(domainPage);

        long pageIndex = domainPage.getPageIndex();
        long pageSize = domainPage.getPageSize();
        String terminalUsername = (String) conditions.get("terminalUsername");
        String authority = (String) conditions.get("authority");
        Long provinceId = (Long) conditions.get("provinceId");
        Long organizationId = (Long) conditions.get("organizationId");
        @SuppressWarnings("unchecked") List<Long> provinceIds = (List<Long>) conditions.get("provinceIds");

        StringBuilder sql = new StringBuilder("SELECT tu.id,tu.terminal_username,tu.authority,p.province_name,tu.nickname,");
        sql.append(" o.organization_name,s.site_name,tu.account_status,p.id pid");
        sql.append(" FROM sys_terminal_user tu, sys_organization o, bas_province p, biz_site s");
        sql.append(" WHERE tu.available = 1 AND tu.organization_id = o.id AND o.province_id = p.id AND tu.site_id = s.id");
        if (StringUtils.isNotBlank(terminalUsername))
            sql.append(" AND tu.terminal_username like '%" + terminalUsername + "%'");
        if (StringUtils.isNotBlank(authority)) sql.append(" AND tu.authority = '" + authority + "'");
        if (provinceId != null) sql.append(" AND o.province_id = " + provinceId);
        if (organizationId != null) sql.append(" AND o.id = " + organizationId);
        if (provinceIds != null) {
            sql.append(" AND o.province_id in( ");
            for (int i = 0; i < provinceIds.size(); i++) {
                if (i == 0) sql.append(provinceIds.get(i));
                else sql.append("," + provinceIds.get(i));
            }
            sql.append(")");
        }

        sql.append(" ORDER BY tu.update_time DESC");
        sql.append(" LIMIT " + (pageIndex - 1) * pageSize + "," + pageIndex * pageSize);

        Query query = em.createNativeQuery(sql.toString());
        List<Object[]> resultList = query.getResultList();
        long totalCount = getTerminaUserTotalCount(terminalUsername, authority, provinceId, organizationId, provinceIds);
        DomainPage<Object[]> returnDomainPage = new DomainPage<Object[]>(pageSize, pageIndex, totalCount);
        returnDomainPage.setDomains(resultList);
        return returnDomainPage;
    }

    private long getTerminaUserTotalCount(String terminalUsername, String authority, Long provinceId, Long organizationId, List<Long> provinceIds) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(tu.id)");
        sql.append(" FROM sys_terminal_user tu, sys_organization o");
        sql.append(" WHERE tu.available = 1 AND tu.organization_id = o.id");
        if (StringUtils.isNotBlank(terminalUsername))
            sql.append(" AND tu.terminal_username like '%" + terminalUsername + "%'");
        if (StringUtils.isNotBlank(authority)) sql.append(" AND tu.authority = '" + authority + "'");
        if (provinceId != null) sql.append(" AND o.province_id = " + provinceId);
        if (organizationId != null) sql.append(" AND o.id = " + organizationId);
        if (provinceIds != null) {
            sql.append(" AND o.province_id in( ");
            for (int i = 0; i < provinceIds.size(); i++) {
                if (i == 0) sql.append(provinceIds.get(i));
                else sql.append("," + provinceIds.get(i));
            }
            sql.append(")");
        }

        Query query = em.createNativeQuery(sql.toString());
        List resultList = query.getResultList();

        if (resultList.size() == 1) return Long.parseLong(resultList.get(0).toString());
        else return 0;
    }

}
