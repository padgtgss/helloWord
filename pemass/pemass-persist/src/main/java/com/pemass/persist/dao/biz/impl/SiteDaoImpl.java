/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.biz.impl;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.dao.biz.SiteDao;
import com.pemass.persist.domain.jpa.biz.Site;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: SiteDaoImpl
 * @Author: pokl.huang
 * @CreateTime: 2014-12-10 10:37
 */
@Repository(value = "siteDao")
public class SiteDaoImpl extends JPABaseDaoImpl implements SiteDao {
    @Override
    public String getDistance(Site site, String longitude, String latitude) {
        StringBuffer sql = new StringBuffer();
        sql.append("select 12756274 * asin( ");
        sql.append("Sqrt(power(sin((" + latitude + " - latitude) * 0.008726646),2) + ");
        sql.append("Cos(" + latitude + " * 0.0174533) * Cos(latitude * 0.0174533) * power(sin((" + longitude + " - longitude) * 0.008726646),2)) ");
        sql.append(") AS distance ");
        sql.append("from biz_site where available = 1 and id =" + site.getId());
        Query query = em.createNativeQuery(sql.toString());


        return query.getResultList().get(0).toString();
    }

    @Override
    public <T extends BaseDomain> DomainPage getsiteListBydistance(String siteName, String siteType, Long cityId, Long districtId, Integer distance,
                                                                   String longitude, String latitude,
                                                                   Long pageIndex, Long pageSize) {

        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;

        StringBuffer sql = new StringBuffer();
        sql.append("select ");
        sql.append("c.id,c.site_name,c.location,o.is_one_merchant,  ");
        sql.append(" 12756274 * asin( Sqrt(power(sin((" + latitude + " - latitude) * 0.008726646),2) + ");
        sql.append("Cos(" + latitude + " * 0.0174533) * Cos(latitude * 0.0174533) * power(sin((" + longitude + " - longitude) * 0.008726646),2)) ");
        sql.append(") AS distance ");
        sql.append(" from biz_site c ,sys_organization o ");
        sql.append(" where c.available = 1 ");
        sql.append(" and o.available = 1 ");
        sql.append(" and c.organization_id = o.id  ");
        sql.append(" and o.audit_status = 2 ");
        if (StringUtils.isNotBlank(siteName)) {
            sql.append(" and c.site_name like '%" + siteName + "%' ");
        }
        if (StringUtils.isNotBlank(siteType)) {
            if ("1".equals(siteType)) {
                sql.append(" and o.is_one_merchant = 1 ");
            } else if ("0".equals(siteType)) {
                sql.append(" and o.is_one_merchant = 0 ");
            }
        }
        if (districtId != null) {
            sql.append(" and c.district_id = " + districtId + " ");
        }
        if (cityId != null) {
            sql.append(" and c.city_id = " + cityId + " ");
        }
        if (distance != null) {
            sql.append(" and 12756274 * asin( Sqrt(power(sin((" + latitude + " - latitude) * 0.008726646),2) + ");
            sql.append("Cos(" + latitude + " * 0.0174533) * Cos(latitude * 0.0174533) * power(sin((" + longitude + " - longitude) * 0.008726646),2)) ");
            sql.append(")  <= " + distance + " ");
        }
        sql.append(" order by distance asc ");
        Query query = em.createNativeQuery(sql.toString());


        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults(pageSize.intValue());
        List<Object> result = query.getResultList();


        StringBuffer sql_count = new StringBuffer();
        sql_count.append("select count(1) ");
        sql_count.append("from biz_site c ,sys_organization o " +
                "where c.available = 1 " +
                "and c.organization_id = o.id ");
        if (StringUtils.isNotBlank(siteName)) {
            sql.append(" and c.site_name like '%" + siteName + "%' ");
        }
        if (StringUtils.isNotBlank(siteType)) {
            if ("1".equals(siteType)) {
                sql.append(" and o.is_one_merchant = 1 ");
            } else if ("0".equals(siteType)) {
                sql.append(" and o.is_one_merchant = 0 ");
            }
        }
        if (districtId != null) {
            sql.append(" and c.district_id = " + districtId + " ");
        }
        if (cityId != null) {
            sql.append(" and c.city_id = " + cityId + " ");
        }
        if (distance != null) {
            sql.append(" and 12756274 * asin( Sqrt(power(sin((" + latitude + " - latitude) * 0.008726646),2) + ");
            sql.append("Cos(" + latitude + " * 0.0174533) * Cos(latitude * 0.0174533) * power(sin((" + longitude + " - longitude) * 0.008726646),2)) ");
            sql.append(")  <= " + distance + " ");
        }
        sql.append(" order by distance asc ");

        query = em.createNativeQuery(sql_count.toString());


        //处理特殊
        Long totalCount = 0L;
        if (query.getResultList().size() > 0) {
            totalCount = Long.valueOf(query.getResultList().get(0).toString());
        }

        /**封装结果**/
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(result);

        return domainPage;
    }

    @Override
    public DomainPage getEntitiesByFieldList(Class clazz, Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, long pageIndex, long pageSize) {
        /**
         *查询满足条件的结果
         */
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;

        String sql = "select c from " + clazz.getName() + " c where c.available = 1 ";


        int parameterIndex = 1;

        if (fieldNameValueMap != null) {
            Set<String> fieldNames = fieldNameValueMap.keySet();
            Iterator<String> iterator = fieldNames.iterator();
            for (int i = 1; i <= fieldNames.size(); i++) {
                String fieldName = iterator.next();
                sql = sql + " and c." + fieldName + " = ?" + parameterIndex++;
            }
        }

        if (fuzzyFieldNameValueMap != null) {
            Set<String> fuzzyFieldNames = fuzzyFieldNameValueMap.keySet();
            Iterator<String> fuzzyIterator = fuzzyFieldNames.iterator();
            for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
                String fuzzyFieldName = fuzzyIterator.next();
                sql = sql + " and c." + fuzzyFieldName + " like ?" + parameterIndex++;
            }
        }

        sql = sql + " order by c.updateTime desc";
        Query query = em.createQuery(sql);

        parameterIndex = 1;

        if (fieldNameValueMap != null) {
            Set<String> fieldNames = fieldNameValueMap.keySet();
            Iterator<String> iterator = fieldNames.iterator();
            for (int i = 1; i <= fieldNames.size(); i++) {
                String fieldName = iterator.next();
                query.setParameter(parameterIndex++, fieldNameValueMap.get(fieldName));
            }
        }

        if (fuzzyFieldNameValueMap != null) {
            Set<String> fuzzyFieldNames = fuzzyFieldNameValueMap.keySet();
            Iterator<String> fuzzyIterator = fuzzyFieldNames.iterator();
            for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
                String fuzzyFieldName = fuzzyIterator.next();
                query.setParameter(parameterIndex++, "%" + fuzzyFieldNameValueMap.get(fuzzyFieldName) + "%");
            }
        }
        long totalCount = query.getResultList().size();
        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List resultList = query.getResultList();


        /**
         * 封装返回结果
         */
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);
        return domainPage;
    }
}

