/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.dao.poi.impl;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.poi.PointCouponDao;
import com.pemass.persist.domain.jpa.poi.PointCoupon;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: PointCouponDaoImpl
 * @Author: cassiel.liu
 * @CreateTime: 2015-05-05 11:23
 */
@Repository
public class PointCouponDaoImpl extends JPABaseDaoImpl implements PointCouponDao {


    @Override
    public String getMaxIdentifier(String dataStr) {
        StringBuffer sql = new StringBuffer("select p.pack_identifier from poi_point_coupon as p where");
        sql.append(" p.create_time like '" + dataStr + "%'");
        sql.append(" order by p.create_time DESC");
        sql.append(" limit 0,1");

        Query query = em.createNativeQuery(sql.toString());
        List list = query.getResultList();

        String returnStr = "";
        if (list.size() == 1) {
            String maxIdentifier = (String) list.get(0);
            maxIdentifier = maxIdentifier.substring(6, 12);
            Integer maxPI = Integer.parseInt(maxIdentifier);
            maxPI++;
            returnStr = StringUtils.leftPad(maxPI.toString(), 6, "0");
        } else {
            returnStr = "000000";
        }
        return returnStr;
    }

    @Override
    public DomainPage getCouponByGroup(Map<String, Object> preciseMap, Map<String, Object> fuzzyMap, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;

        String sql = "select c,o from PointCoupon c,Organization o " +
                " where c.organizationId = o.id and c.available = 1 ";

        //循环精确查询匹配参数
        Set<String> fieldNames = preciseMap.keySet();
        Iterator<String> iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String name = iterator.next();
            sql = sql + " and c." + name + " = ?" + i;
        }
        //循环模糊查询条件
        Set<String> fuzzyFieldNames = fuzzyMap.keySet();
        Iterator<String> fuzzyIterator = fuzzyFieldNames.iterator();
        int fuzzyBegin = fieldNames.size() == 0 ? 0 : fieldNames.size();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            sql = sql + " and c." + fuzzyFieldName + " like ?" + (fuzzyBegin + i);
        }


        sql = sql + " group by c.packIdentifier order by c.updateTime desc";
        Query query = em.createQuery(sql);

        //设置精确查询匹配参数的值
        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String name = iterator.next();
            query.setParameter(i, preciseMap.get(name));
        }

        //设置模糊查询匹配参数的值
        fuzzyIterator = fuzzyFieldNames.iterator();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            query.setParameter((fuzzyBegin + i), "%" + fuzzyMap.get(fuzzyFieldName) + "%");
        }

        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List resultList = query.getResultList();

        // 查询总记录数

        sql = "select count(c) from PointCoupon c where c.available = 1 ";

        //循环精确查询匹配参数
        fieldNames = preciseMap.keySet();
        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String name = iterator.next();
            sql = sql + " and c." + name + " = ?" + i;
        }

        //循环模糊查询匹配参数
        fuzzyFieldNames = fuzzyMap.keySet();
        fuzzyIterator = fuzzyFieldNames.iterator();
        fuzzyBegin = fieldNames.size() == 0 ? 0 : fieldNames.size();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            sql = sql + " and c." + fuzzyFieldName + " like ?" + (fuzzyBegin + i);
        }

        sql = sql + " group by c.packIdentifier order by c.updateTime desc ";
        query = em.createQuery(sql);

        //设置精确查询匹配参数的值
        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String name = iterator.next();
            query.setParameter(i, preciseMap.get(name));
        }

        //设置模糊查询匹配参数的值
        fuzzyIterator = fuzzyFieldNames.iterator();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            query.setParameter((fuzzyBegin + i), "%" + fuzzyMap.get(fuzzyFieldName) + "%");
        }
        Long totalCount;
        List list = query.getResultList();
        if (list.size() != 0) {
            totalCount = (Long) query.getResultList().get(0);
        } else {
            totalCount = 0L;
        }


        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);
        return domainPage;
    }

    @Override
    public DomainPage getCouponByIdentifier(Map<String, Object> preciseMap, Map<String, Object> fuzzyMap, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;

        String sql = "select c,o,p from PointCoupon c,Organization o,PointPurchase p " +
                " where c.organizationId = o.id and c.pointPurchaseId = p.id" +
                " and c.available = 1 and o.available = 1 and p.available = 1 ";

        //循环精确查询匹配参数
        Set<String> fieldNames = preciseMap.keySet();
        Iterator<String> iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String name = iterator.next();
            sql = sql + " and c." + name + " = ?" + i;
        }
        //循环模糊查询条件
        Set<String> fuzzyFieldNames = fuzzyMap.keySet();
        Iterator<String> fuzzyIterator = fuzzyFieldNames.iterator();
        int fuzzyBegin = fieldNames.size() == 0 ? 0 : fieldNames.size();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            sql = sql + " and c." + fuzzyFieldName + " like ?" + (fuzzyBegin + i);
        }


        sql = sql + " order by c.updateTime desc";
        Query query = em.createQuery(sql);

        //设置精确查询匹配参数的值
        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String name = iterator.next();
            query.setParameter(i, preciseMap.get(name));
        }

        //设置模糊查询匹配参数的值
        fuzzyIterator = fuzzyFieldNames.iterator();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            query.setParameter((fuzzyBegin + i), "%" + fuzzyMap.get(fuzzyFieldName) + "%");
        }

        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List resultList = query.getResultList();

        // 查询总记录数

        sql = "select count(c) from PointCoupon c,Organization o,PointPurchase p " +
                " where c.organizationId = o.id and c.pointPurchaseId = p.id " +
                "and c.available = 1 and o.available = 1 and p.available = 1 ";

        //循环精确查询匹配参数
        fieldNames = preciseMap.keySet();
        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String name = iterator.next();
            sql = sql + " and c." + name + " = ?" + i;
        }

        //循环模糊查询匹配参数
        fuzzyFieldNames = fuzzyMap.keySet();
        fuzzyIterator = fuzzyFieldNames.iterator();
        fuzzyBegin = fieldNames.size() == 0 ? 0 : fieldNames.size();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            sql = sql + " and c." + fuzzyFieldName + " like ?" + (fuzzyBegin + i);
        }

        sql = sql + "  order by c.updateTime desc ";
        query = em.createQuery(sql);

        //设置精确查询匹配参数的值
        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String name = iterator.next();
            query.setParameter(i, preciseMap.get(name));
        }

        //设置模糊查询匹配参数的值
        fuzzyIterator = fuzzyFieldNames.iterator();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            query.setParameter((fuzzyBegin + i), "%" + fuzzyMap.get(fuzzyFieldName) + "%");
        }
        Long totalCount;
        List list = query.getResultList();
        if (list.size() != 0) {
            totalCount = (Long) query.getResultList().get(0);
        } else {
            totalCount = 0L;
        }


        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);
        return domainPage;
    }

    @Override
    public Long getCountCouponByIdentifier(String identifier) {
        String sql = "select count(c) from PointCoupon c where c.available = 1 and c.packIdentifier = ?1";
        Query query = em.createQuery(sql);
        query.setParameter(1, identifier);
        Long count;
        List list = query.getResultList();
        if (list.size() != 0) {
            count = (Long) list.get(0);
        } else {
            count = 0L;
        }
        return count;
    }

    @Override
    public PointCoupon selectCouponByFileds(String packIdentifier, String cardSecret) {
        String sql = "select c from PointCoupon c where c.packIdentifier =?1 and c.cardSecret = ?2";
        Query query = em.createQuery(sql);
        query.setParameter(1, packIdentifier);
        query.setParameter(2, cardSecret);
        List<PointCoupon> list = query.getResultList();
        if (list != null && list.size() >= 1)
            return list.get(0);
        return null;
    }
}
