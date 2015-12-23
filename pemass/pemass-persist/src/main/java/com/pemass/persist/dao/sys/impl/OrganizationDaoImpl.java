/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.sys.impl;

import com.google.common.collect.Lists;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.sys.OrganizationDao;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.enumeration.AccountRoleEnum;
import com.pemass.persist.enumeration.AuditStatusEnum;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

/**
 * @Description: OrganizationDaoImpl
 * @Author: zhou hang
 * @CreateTime: 2015-05-08 16:59
 */
@Repository
public class OrganizationDaoImpl extends JPABaseDaoImpl implements OrganizationDao {
    @Override
    public List<Organization> getOrganizationByArea(Long id) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * from sys_organization s WHERE (SELECT FIND_IN_SET(s.province_id,c.area) from sys_company c where c.id=?1)");
        Query query = em.createNativeQuery(sb.toString(), Organization.class);
        query.setParameter(1, id);
        return query.getResultList();
    }

    public List<Organization> getOrganizationByNoIds(Long id, Long provinceId) {
        StringBuffer sb = new StringBuffer();
        sb.append("from Organization o where o.available=1 and o.id<>?1 and o.accountRole =?2 and o.auditStatus=?3 and o.provinceId=?4");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, id);
        query.setParameter(2, AccountRoleEnum.ROLE_SUPPLIER);
        query.setParameter(3, AuditStatusEnum.HAS_AUDIT);
        query.setParameter(4, provinceId);
        return query.getResultList();
    }

    @Override
    public DomainPage selectWithPointPool(Long pointPoolId, long pageIndex, long pageSize) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT DISTINCT( so.organization_name )from poi_point_purchase ppp  ")
                .append("LEFT JOIN sys_organization so ON (so.id = ppp.organization_id) ")
                .append(" WHERE ppp.point_pool_id =?1 and so.available =1");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, pointPoolId);
        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, this.getWithPointPoolCount(pointPoolId));
        domainPage.setDomains(query.getResultList());
        return domainPage;
    }

    @Override
    @SuppressWarnings("all")
    public List<Long> getOrganizationIDByAccountRole(AccountRoleEnum accountRole) {
        StringBuilder sql = new StringBuilder("SELECT o.id FROM sys_organization o");
        sql.append(" WHERE o.available = 1");
        sql.append(" AND o.account_role = " + accountRole.ordinal());

        Query query = em.createNativeQuery(sql.toString());
        List<Object> result = query.getResultList();
        List<Long> organizationIDs = Lists.newArrayList();
        for (Object object : result) organizationIDs.add(Long.parseLong(object.toString()));

        return organizationIDs;
    }

    private long getWithPointPoolCount(Long pointPoolId) {
        StringBuffer sb = new StringBuffer("select count(*) from ( ");
        sb.append("SELECT DISTINCT( so.organization_name )from poi_point_purchase ppp  ")
                .append("LEFT JOIN sys_organization so ON (so.id = ppp.organization_id) ")
                .append(" WHERE ppp.point_pool_id =?1 and so.available =1 ) t");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, pointPoolId);
        return ((BigInteger) query.getResultList().get(0)).longValue();
    }


}