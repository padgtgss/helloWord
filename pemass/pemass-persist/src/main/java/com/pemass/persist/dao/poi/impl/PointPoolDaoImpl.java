/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.dao.poi.impl;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.poi.PointPoolDao;
import com.pemass.persist.domain.jpa.poi.PointPool;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: PointPoolDaoImpl
 * @Author: cassiel.liu
 * @CreateTime: 2015-06-30 16:27
 */
@Repository
public class PointPoolDaoImpl extends JPABaseDaoImpl implements PointPoolDao {

    @Override
    public void recycle(Long pointPoolId, Integer amount) {
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE poi_point_pool t SET t.amount = t.amount - ?1 ")
                .append(" WHERE t.available = 1 and t.id = ?2");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, amount);
        query.setParameter(2, pointPoolId);
        query.executeUpdate();
    }

    @Override
    public void recycle(Integer amount) {
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE poi_point_pool t SET t.amount = t.amount - ?1 ")
                .append(" WHERE t.available = 1 and t.point_type =  'O'");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, amount);
        query.executeUpdate();
    }

    @Override
    public PointPool getPointPool() {
        StringBuffer sb = new StringBuffer();
        sb.append("from PointPool p where p.available = 1 and p.area ='00:00:00:00:?'");
        Query query = em.createQuery(sb.toString());
        if (query.getResultList().size()>0){
            return (PointPool) query.getResultList().get(0);
        }
        return null;
    }

    @Override
    public DomainPage getAllPointPoolByPages(Map<String, Object> fuzzyFieldNameValueMap, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;
        /**查询记录*/
        String sql = "select p from PointPool p where p.available = 1 ";

        Set<String> fieldNames = fuzzyFieldNameValueMap.keySet();
        Iterator<String> iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            sql = sql + " and p." + fieldName + " like  ?" + i;
        }
        sql = sql + " order by p.updateTime desc";
        Query query = em.createQuery(sql);
        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            query.setParameter(i, "%" + fuzzyFieldNameValueMap.get(fieldName) + "%");
        }
        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List resultList = query.getResultList();
        /**查询总记录数*/
        sql = "select count(p) from PointPool p where p.available = 1 ";

        fieldNames = fuzzyFieldNameValueMap.keySet();
        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            sql = sql + " and  p." + fieldName + " like  ?" + i;
        }
        sql = sql + " order by p.updateTime desc";
        query = em.createQuery(sql);
        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            query.setParameter(i, "%" + fuzzyFieldNameValueMap.get(fieldName) + "%");
        }
        Long totalCount = 0L;
        if (query.getResultList().size() > 0) {
            totalCount = (Long) query.getResultList().get(0);
        }
        /**创建DomainPage*/
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);
        return domainPage;
    }


}
