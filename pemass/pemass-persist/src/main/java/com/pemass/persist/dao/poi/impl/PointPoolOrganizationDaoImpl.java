/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.dao.poi.impl;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.poi.PointPoolOrganizationDao;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

/**
 * @Description: PointPoolOrganizationDaoImpl
 * @Author: oliver.he
 * @CreateTime: 2015-07-17 16:33
 */
@Repository
public class PointPoolOrganizationDaoImpl extends JPABaseDaoImpl implements PointPoolOrganizationDao {
    @Override
    public List selectByOrganizationId(Long organizationId) {
        String sql = "select c from PointPoolOrganization c,PointPool p " +
                " where c.pointPoolId = p.id and c.available = 1 " +
                " and p.available =1 and c.organizationId = ?1 order by c.updateTime desc";
        Query query = em.createQuery(sql);
        query.setParameter(1, organizationId);
        List<T> ret = query.getResultList();
        if (ret == null || ret.size() < 1) {
            return null;
        }
        return ret;
    }
    @Override
    public DomainPage selectAllPointPoolOrganization(long pageIndex, long pageSize) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT t1.organization_id,t1.point_pool_id,t2.organization_name from poi_point_pool_organization t1  ");
        sb.append("  LEFT JOIN sys_organization t2 ON(t1.organization_id = t2.id)");
        sb.append(" where t1.available =1 and t2.available = 1 ");
        Query query = em.createNativeQuery(sb.toString());
        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List list = query.getResultList();
        DomainPage domainPage = new DomainPage(pageSize, pageIndex,this.selectAllPointPoolOrganizationCount());
        domainPage.setDomains(list);
        return domainPage;
    }

    private long selectAllPointPoolOrganizationCount() {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT count(*) from PointPoolOrganization   ");
        sb.append(" where available =1  ");
        Query query = em.createQuery(sb.toString());
        return  (Long)query.getResultList().get(0);

    }


}