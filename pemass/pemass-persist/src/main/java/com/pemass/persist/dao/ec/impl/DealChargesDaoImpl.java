/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.ec.impl;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.dao.ec.DealChargesDao;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: DealChargesDaoImpl
 * @Author: cassiel.liu
 * @CreateTime: 2014-12-12 09:28
 */
@Repository(value = "dealChargesDao")
public class DealChargesDaoImpl extends JPABaseDaoImpl implements DealChargesDao {


    @Override
    public <T extends BaseDomain> DomainPage selectAllEntitiesByTimeQuantum(Class<T> clazz, Date startDate, Date endDate, Map<String, Object> fieldNameValueMap, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;

        //分页查询记录
        Date nowDate = new Date();
        String sql = "select c,oi from " + clazz.getName() + " c,OrderItem oi where oi.order.id = c.order.id  and c.available = 1 and oi.available = 1 "
                + " and DATE_FORMAT(c.order.payTime,'%y-%m-%d') between DATE_FORMAT(?1,'%y-%m-%d') and DATE_FORMAT(?2,'%y-%m-%d')";

        Set<String> fieldNames = fieldNameValueMap.keySet();
        Iterator<String> iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String name = iterator.next();
            if (i == 1) {
                sql = sql + " and  c." + name + " = ?" + (i + 2);
            } else {
                sql = sql + " and c." + name + " = ?" + (i + 2);
            }
        }
        sql = sql + " group by c.id order by c.order.payTime desc ";
        Query query = em.createQuery(sql);
        if (startDate != null && endDate != null) {
            query.setParameter(1, startDate);
            query.setParameter(2, endDate);
        }
        if (startDate == null && endDate == null) {
            query.setParameter(1, nowDate);
            query.setParameter(2, nowDate);
        }
        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String name = iterator.next();
            query.setParameter((i + 2), fieldNameValueMap.get(name));
        }
        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List result = query.getResultList();

        //查询总记录数
        sql = "select count(distinct c.id) from " + clazz.getName() + " c,OrderItem oi where oi.order.id = c.order.id  and c.available = 1 and oi.available = 1 "
                + " and DATE_FORMAT(c.order.payTime,'%y-%m-%d') between DATE_FORMAT(?1,'%y-%m-%d') and DATE_FORMAT(?2,'%y-%m-%d')";
        fieldNames = fieldNameValueMap.keySet();
        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String name = iterator.next();
            if (i == 1) {
                sql = sql + " and  c." + name + " = ?" + (i + 2);
            } else {
                sql = sql + " and c." + name + " = ?" + (i + 2);
            }
        }
        sql = sql + " order by c.order.payTime desc ";
        query = em.createQuery(sql);
        if (startDate != null && endDate != null) {
            query.setParameter(1, startDate);
            query.setParameter(2, endDate);
        }
        if (startDate == null && endDate == null) {
            query.setParameter(1, nowDate);
            query.setParameter(2, nowDate);
        }
        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String name = iterator.next();
            query.setParameter((i + 2), fieldNameValueMap.get(name));
        }
        Long totalCount = 0L;
        if(query.getResultList().get(0) != null){
            totalCount = (Long) query.getResultList().get(0);
        }
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(result);
        return domainPage;
    }

    @Override
    public <T extends BaseDomain> Float getSumCharge(Class<T> clazz, String sumFieldName, Map<String, Object> fieldNameValueMap) {

        String sql = "select sum(c." + sumFieldName + ") from " + clazz.getName() + " c where c.available = 1 ";
        Set<String> fieldNames = fieldNameValueMap.keySet();
        Iterator<String> iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String name = iterator.next();
            if (i == 1) {
                sql = sql + " and  c." + name + " = ?" + i;
            } else {
                sql = sql + " and c." + name + " = ?" + i;
            }
        }
        Query query = em.createQuery(sql);

        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String name = iterator.next();
            query.setParameter(i, fieldNameValueMap.get(name));
        }
        List result = query.getResultList();
        float charge = 0;
        if (result.get(0) != null) {
            charge = Float.parseFloat(String.valueOf(result.get(0)));
        }
        return charge;
    }

}
