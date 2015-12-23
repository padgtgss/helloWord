/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.ec.impl;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.dao.ec.DealPointDao;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * @Description: DealPointDaoImpl
 * @Author: cassiel.liu
 * @CreateTime: 2014-12-12 14:20
 */
@Repository(value = "dealPointDao")
public class DealPointDaoImpl extends JPABaseDaoImpl implements DealPointDao {


    public <T extends BaseDomain> DomainPage selectAllEntitiesByTimeQuantum(Class<T> clazz, Long organizationId, Integer dealType, Date startDate, Date endDate, long pageIndex, long pageSize) {

        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;

        //分页查询记录
        Date nowDate = new Date();
        String sql = "select c from " + clazz.getName() + " c where c.available = 1 and c.dealType = ?1 "
                + " and DATE_FORMAT(c.order.payTime,'%y-%m-%d') between DATE_FORMAT(?2,'%y-%m-%d') and DATE_FORMAT(?3,'%y-%m-%d') ";
        if (organizationId != null && organizationId != 0) {
            sql = sql + " and c.sourceOrganization.id = ?4 ";
        }
        sql = sql + " group by c.id order by c.order.payTime desc  ";

        Query query = em.createQuery(sql);
        query.setParameter(1, dealType);
        if (startDate != null && endDate != null) {
            query.setParameter(2, startDate);
            query.setParameter(3, endDate);
        }
        if (startDate == null && endDate == null) {
            query.setParameter(2, nowDate);
            query.setParameter(3, nowDate);
        }
        if (organizationId != null && organizationId != 0) {
            query.setParameter(4, organizationId);
        }
        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List result = query.getResultList();

        //查询总记录数
        sql = "select count(c) from " + clazz.getName() + " c where c.available = 1 and c.dealType = ?1"
                + " and DATE_FORMAT(c.order.payTime,'%y-%m-%d') between DATE_FORMAT(?2,'%y-%m-%d') and DATE_FORMAT(?3,'%y-%m-%d')";
        if (organizationId != null && organizationId != 0) {
            sql = sql + " and c.sourceOrganization.id = ?4 ";
        }
        sql = sql + " order by c.order.payTime desc ";
        query = em.createQuery(sql);
        query.setParameter(1, dealType);
        if (startDate != null && endDate != null) {
            query.setParameter(2, startDate);
            query.setParameter(3, endDate);
        }
        if (startDate == null && endDate == null) {
            query.setParameter(2, nowDate);
            query.setParameter(3, nowDate);
        }
        if (organizationId != null && organizationId != 0) {
            query.setParameter(4, organizationId);
        }
        Long totalCount = 0L;
        if(query.getResultList().get(0) != null){
            totalCount = (Long) query.getResultList().get(0);
        }
        //封装DomainPage
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(result);
        return domainPage;
    }
}
