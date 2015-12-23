/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.poi.impl;

import com.google.common.base.Preconditions;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.core.util.DateUtil;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.common.server.domain.BaseDomain;
import com.pemass.common.server.enumeration.AvailableEnum;
import com.pemass.persist.dao.poi.PresentPackDao;
import com.pemass.persist.domain.jpa.biz.Site;
import com.pemass.persist.domain.jpa.poi.PresentPack;
import com.pemass.persist.domain.jpa.sys.Account;
import com.pemass.persist.enumeration.AuditStatusEnum;
import com.pemass.persist.enumeration.PresentPackTypeEnum;
import com.pemass.persist.enumeration.PresentStatusEnum;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: PresentPackDaoImpl
 * @Author: he jun cheng
 * @CreateTime: 2014-10-22 10:42
 */
@Repository
public class PresentPackDaoImpl extends JPABaseDaoImpl implements PresentPackDao {

    @Override
    public <T extends BaseDomain> DomainPage<T> getEntitiesPagesByFieldList(Class<T> clazz, Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, long pageIndex, long pageSize) {
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
        List<T> resultList = query.getResultList();


        /**
         * 封装返回结果
         */
        DomainPage<T> domainPage = new DomainPage<T>(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);
        return domainPage;
    }

    @Override
    public <T extends BaseDomain> DomainPage getEntitiesPagesByFieldList(Class<T> clazz, Map<String, Object> conditions, Map<String, Object> fuzzyConditions, Map<String, List<Object>> collectionConditions, Map<String, Object[]> intervalConditions, Map<String, OrderBy> orderByConditions, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;
        String sql = "select c from " + clazz.getName() + " c where c.available = 1";

        /**参数的Index**/
        int parameterIndex = 1;

        /**精确查询条件**/
        if (conditions != null) {
            Set<String> fieldNames = conditions.keySet();
            Iterator<String> iterator = fieldNames.iterator();
            for (int i = 1; i <= fieldNames.size(); i++) {
                String fieldName = iterator.next();
                sql = sql + " and c." + fieldName + " = ?" + parameterIndex;
                parameterIndex++;
            }
        }

        /**模糊查询条件**/
        if (fuzzyConditions != null) {
            Set<String> fuzzyFieldNames = fuzzyConditions.keySet();
            Iterator<String> fuzzyIterator = fuzzyFieldNames.iterator();
            for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
                String fuzzyFieldName = fuzzyIterator.next();
                sql = sql + " and c." + fuzzyFieldName + " like ?" + parameterIndex;
                parameterIndex++;
            }
        }

        /**区间查询条件**/
        if (intervalConditions != null) {
            Set<String> intervalFileNames = intervalConditions.keySet();
            Iterator<String> intervalIterator = intervalFileNames.iterator();
            for (int i = 1; i <= intervalFileNames.size(); i++) {
                String intervalFileName = intervalIterator.next();
                sql = sql + " and c." + intervalFileName + " between " + intervalConditions.get(intervalFileName)[0] + " and " + intervalConditions.get(intervalFileName)[1];
            }
        }

        /**集合查询条件 对于IN**/
        if (collectionConditions != null) {
            Set<String> collectionFileNames = collectionConditions.keySet();
            Iterator<String> collectionIterator = collectionFileNames.iterator();
            for (int i = 1; i <= collectionFileNames.size(); i++) {
                String collectionFileName = collectionIterator.next();
                sql = sql + " and c." + collectionFileName + " in (";
                List<Object> invalues = collectionConditions.get(collectionFileName);
                for (int m = 1; m <= invalues.size(); m++) {
                    if (m == 1) {
                        sql = sql + collectionConditions.get(collectionFileName).get(m - 1);
                    } else {
                        sql = sql + "," + collectionConditions.get(collectionFileName).get(m - 1);
                    }
                }
                sql = sql + ")";
            }
        }

        /**排序条件**/
        if (orderByConditions != null) {
            Set<String> orderByNames = orderByConditions.keySet();
            Iterator<String> orderByIterator = orderByNames.iterator();
            sql += " order by ";
            for (int i = 1; i <= orderByNames.size(); i++) {
                String orderByName = orderByIterator.next();
                if (i == 1) {
                    sql += orderByName + " " + orderByConditions.get(orderByName);
                } else {
                    sql += "," + orderByName + " " + orderByConditions.get(orderByName);
                }
            }
        } else {
            sql = sql + " order by c.updateTime desc ";
        }
        Query query = em.createQuery(sql);


        /**设置值的Index**/
        int parameterValueIndex = 1;

        /**设置精确查询值**/
        if (conditions != null) {
            Set<String> fieldNames = conditions.keySet();
            Iterator<String> iterator = fieldNames.iterator();
            for (int i = 1; i <= fieldNames.size(); i++) {
                String fieldName = iterator.next();
                query.setParameter(parameterValueIndex, conditions.get(fieldName));
                parameterValueIndex++;
            }
        }

        /**设置模糊查询值**/
        if (fuzzyConditions != null) {
            Set<String> fuzzyFieldNames = fuzzyConditions.keySet();
            Iterator<String> fuzzyIterator = fuzzyFieldNames.iterator();
            for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
                String fuzzyFieldName = fuzzyIterator.next();
                query.setParameter(parameterValueIndex, "%" + fuzzyConditions.get(fuzzyFieldName) + "%");
                parameterValueIndex++;
            }
        }

        /**分页查询**/
        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List<T> resultList = query.getResultList();

        /**查询总记录**/
        sql = "select count(c) from " + clazz.getName() + " c where c.available = 1 ";

        //拼SQL
        parameterIndex = 1;
        if (conditions != null) {
            Set<String> fieldNames = conditions.keySet();
            Iterator<String> iterator = fieldNames.iterator();
            for (int i = 1; i <= fieldNames.size(); i++) {
                String fieldName = iterator.next();
                sql = sql + " and c." + fieldName + " = ?" + parameterIndex;
                parameterIndex++;
            }
        }

        if (fuzzyConditions != null) {
            Set<String> fuzzyFieldNames = fuzzyConditions.keySet();
            Iterator<String> fuzzyIterator = fuzzyFieldNames.iterator();
            for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
                String fuzzyFieldName = fuzzyIterator.next();
                sql = sql + " and c." + fuzzyFieldName + " like ?" + parameterIndex;
                parameterIndex++;
            }
        }

        if (intervalConditions != null) {
            Set<String> intervalFileNames = intervalConditions.keySet();
            Iterator<String> intervalIterator = intervalFileNames.iterator();
            for (int i = 1; i <= intervalFileNames.size(); i++) {
                String intervalFileName = intervalIterator.next();
                sql = sql + " and c." + intervalFileName + " between " + intervalConditions.get(intervalFileName)[0] + " and " + intervalConditions.get(intervalFileName)[1];
            }
        }

        if (collectionConditions != null) {
            Set<String> collectionFileNames = collectionConditions.keySet();
            Iterator<String> collectionIterator = collectionFileNames.iterator();
            for (int i = 1; i <= collectionFileNames.size(); i++) {
                String collectionFileName = collectionIterator.next();
                sql = sql + " and c." + collectionFileName + " in (";
                for (int m = 1; m <= collectionConditions.get(collectionFileName).size(); m++) {
                    if (m == 1) {
                        sql = sql + collectionConditions.get(collectionFileName).get(m - 1);
                    } else {
                        sql = sql + "," + collectionConditions.get(collectionFileName).get(m - 1);
                    }
                }
                sql = sql + ")";
            }
        }

        query = em.createQuery(sql);

        //设置值
        parameterValueIndex = 1;
        if (conditions != null) {
            Set<String> fieldNames = conditions.keySet();
            Iterator<String> iterator = fieldNames.iterator();
            for (int i = 1; i <= fieldNames.size(); i++) {
                String fieldName = iterator.next();
                query.setParameter(parameterValueIndex, conditions.get(fieldName));
                parameterValueIndex++;
            }
        }
        if (fuzzyConditions != null) {
            Set<String> fuzzyFieldNames = fuzzyConditions.keySet();
            Iterator<String> fuzzyIterator = fuzzyFieldNames.iterator();
            for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
                String fuzzyFieldName = fuzzyIterator.next();
                query.setParameter(parameterValueIndex, "%" + fuzzyConditions.get(fuzzyFieldName) + "%");
                parameterValueIndex++;
            }
        }

        //处理特殊
        Long totalCount = 0L;
        if (query.getResultList().size() > 0) {
            totalCount = (Long) query.getResultList().get(0);
        }

        /**封装结果**/
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);

        return domainPage;
    }


    @Override
    public <T extends BaseDomain> Long getEntityTotalCount(Class<T> clazz, Map<String, Object> conditions) {
        String sql = "select count(c) from " + clazz.getName() + " c where c.available = 1";

        /**
         * 拼出完整的SQL与完成赋值
         */
        int parameIndex = 1;
        if (conditions != null) {
            Set<String> fileNames = conditions.keySet();
            Iterator<String> fileNameIterator = fileNames.iterator();
            for (int i = 1; i <= fileNames.size(); i++) {
                String fileName = fileNameIterator.next();
                sql = sql + " and c." + fileName + " =?" + parameIndex;
                parameIndex++;
            }
        }
        Query query = em.createQuery(sql);

        parameIndex = 1;
        if (conditions != null) {
            Set<String> fileNames = conditions.keySet();
            Iterator<String> fileNameIterator = fileNames.iterator();
            for (int i = 1; i <= fileNames.size(); i++) {
                String fileName = fileNameIterator.next();
                query.setParameter(parameIndex, conditions.get(fileName));
                parameIndex++;
            }
        }

        List result = query.getResultList();

        /**
         * 处理结果并返回
         */
        Long totalCount = 0L;
        if (result.size() > 0) {
            totalCount = (Long) result.get(0);
        }
        return totalCount;
    }

    @Override
    public DomainPage getAuditRecordByPages(Map<String, Object> fuzzyMap, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;

        String sql = "select c,o from PresentPack c,Organization o " +
                " where c.organizationId = o.id and c.available = 1 and o.available = 1 and c.auditStatus <> ?1 ";

        Set<String> fuzzyFieldNames = fuzzyMap.keySet();
        Iterator<String> fuzzyIterator = fuzzyFieldNames.iterator();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            if ("packIdentifier".equals(fuzzyFieldName)) {
                sql = sql + " and c.packIdentifier like ?" + (i + 1);
            } else if ("organizationName".equals(fuzzyFieldName)) {
                sql = sql + " and o.organizationName like ?" + (i + 1);
            } else {
                sql = sql + " and c." + fuzzyFieldName + " like ?" + (i + 1);
            }
        }
        sql = sql + " order by c.updateTime desc";

        Query query = em.createQuery(sql);
        query.setParameter(1, AuditStatusEnum.NONE_AUDIT);

        fuzzyIterator = fuzzyFieldNames.iterator();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            query.setParameter((i + 1), "%" + fuzzyMap.get(fuzzyFieldName) + "%");
        }

        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List resultList = query.getResultList();

        sql = "select count(c) from PresentPack c,Organization o " +
                " where c.organizationId = o.id and c.available = 1 and o.available = 1 and c.auditStatus <> ?1 ";

        fuzzyIterator = fuzzyFieldNames.iterator();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            if ("packIdentifier".equals(fuzzyFieldName)) {
                sql = sql + " and c.packIdentifier like ?" + (i + 1);
            } else if ("organizationName".equals(fuzzyFieldName)) {
                sql = sql + " and o.organizationName like ?" + (i + 1);
            } else {
                sql = sql + " and c." + fuzzyFieldName + " like ?" + (i + 1);
            }
        }
        sql = sql + " order by c.updateTime desc";

        query = em.createQuery(sql);
        query.setParameter(1, AuditStatusEnum.NONE_AUDIT);

        fuzzyIterator = fuzzyFieldNames.iterator();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            query.setParameter((i + 1), "%" + fuzzyMap.get(fuzzyFieldName) + "%");
        }

        List result = query.getResultList();
        Long totalCount = 0L;
        if (result.size() > 0) {
            totalCount = (Long) result.get(0);
        }


        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);

        return domainPage;
    }

    @Override
    public DomainPage getPresentPackByPages(Map<String, Object> fuzzyFieldNameValueMap, long pageIndex, long pageSize) {
        /**
         *查询满足条件的结果
         */
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;

        String sql = "select c,o from PresentPack c,Organization o " +
                " where c.organizationId = o.id and  c.available = 1 and o.available= 1 " +
                " and c.auditStatus = ?1 ";

        if (fuzzyFieldNameValueMap != null) {
            Set<String> fuzzyFieldNames = fuzzyFieldNameValueMap.keySet();
            Iterator<String> fuzzyIterator = fuzzyFieldNames.iterator();
            for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
                String fuzzyFieldName = fuzzyIterator.next();
                if ("packIdentifier".equals(fuzzyFieldName)) {
                    sql = sql + " and c.packIdentifier like ?" + (i + 1);
                } else if ("organizationName".equals(fuzzyFieldName)) {
                    sql = sql + " and o.organizationName like ?" + (i + 1);
                } else {
                    sql = sql + " and c." + fuzzyFieldName + " like ?" + (i + 1);
                }
            }
        }

        sql = sql + " order by c.updateTime desc";
        Query query = em.createQuery(sql);
        query.setParameter(1, AuditStatusEnum.NONE_AUDIT);

        if (fuzzyFieldNameValueMap != null) {
            Set<String> fuzzyFieldNames = fuzzyFieldNameValueMap.keySet();
            Iterator<String> fuzzyIterator = fuzzyFieldNames.iterator();
            for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
                String fuzzyFieldName = fuzzyIterator.next();
                query.setParameter((i + 1), "%" + fuzzyFieldNameValueMap.get(fuzzyFieldName) + "%");
            }
        }

        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List<T> resultList = query.getResultList();

        /**
         * 查询满足该条件的总记录
         */
        sql = "select count(c) from PresentPack c,Organization o " +
                " where c.organizationId = o.id and  c.available = 1 and o.available= 1 " +
                " and c.auditStatus = ?1 ";


        if (fuzzyFieldNameValueMap != null) {
            Set<String> fuzzyFieldNames = fuzzyFieldNameValueMap.keySet();
            Iterator<String> fuzzyIterator = fuzzyFieldNames.iterator();
            for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
                String fuzzyFieldName = fuzzyIterator.next();
                if ("packIdentifier".equals(fuzzyFieldName)) {
                    sql = sql + " and c.packIdentifier like ?" + (i + 1);
                } else if ("organizationName".equals(fuzzyFieldName)) {
                    sql = sql + " and o.organizationName like ?" + (i + 1);
                } else {
                    sql = sql + " and c." + fuzzyFieldName + " like ?" + (i + 1);
                }
            }
        }

        sql = sql + " order by c.updateTime desc";
        query = em.createQuery(sql);
        query.setParameter(1, AuditStatusEnum.NONE_AUDIT);

        if (fuzzyFieldNameValueMap != null) {
            Set<String> fuzzyFieldNames = fuzzyFieldNameValueMap.keySet();
            Iterator<String> fuzzyIterator = fuzzyFieldNames.iterator();
            for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
                String fuzzyFieldName = fuzzyIterator.next();
                query.setParameter((i + 1), "%" + fuzzyFieldNameValueMap.get(fuzzyFieldName) + "%");
            }
        }

        Long totalCount = 0L;
        if (query.getResultList().size() > 0) {
            totalCount = (Long) query.getResultList().get(0);
        }

        /**
         * 封装返回结果
         */
        DomainPage<T> domainPage = new DomainPage<T>(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);
        return domainPage;
    }

    @Override
    public String getMaxPackIdentifier(String dataStr) {
        StringBuffer sql = new StringBuffer("select pp.pack_identifier from poi_present_pack as pp where");
        sql.append(" pp.create_time like '" + dataStr + "%'");
        sql.append(" order by pp.create_time DESC ");
        sql.append(" limit 0,1");

        Query query = em.createNativeQuery(sql.toString());
        List list = query.getResultList();

        String returnStr = "";
        if (list.size() == 1) {
            String maxPackIdentifier = (String) list.get(0);
            maxPackIdentifier = maxPackIdentifier.substring(7, 13);
            Integer maxPI = Integer.parseInt(maxPackIdentifier);
            maxPI++;
            returnStr = StringUtils.leftPad(maxPI.toString(), 6, "0");
        } else {
            returnStr = "000000";
        }
        return returnStr;
    }


    @Override
    public DomainPage getPackRecord(DomainPage domainPage, Map<String, Object> conditions) {
        Date nowTime = new Date();
        /**
         * 查询满足条件的记录
         */
        StringBuilder sql = new StringBuilder("select p from PresentPack p where p.available = 1");
        int parameterIndex = 1;
        if (conditions != null) {
            Set<String> keys = conditions.keySet();
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                sql.append(" and p." + key + " = ?" + parameterIndex++);
            }
        }
        sql.append(" and p.expiryTime > ?" + parameterIndex++);    // 过期时间大于系统当前时间
        sql.append(" order by p.updateTime desc");  // 默认排序
        // 获取结果
        Query query = em.createQuery(sql.toString());
        query.setFirstResult((int) ((domainPage.getPageIndex() - 1) * domainPage.getPageSize()));
        query.setMaxResults((int) domainPage.getPageSize());
        parameterIndex = 1;
        if (conditions != null) {
            Set<String> keys = conditions.keySet();
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                query.setParameter(parameterIndex++, conditions.get(key));
            }
        }
        query.setParameter(parameterIndex++, nowTime);//当前时间

        List<PresentPack> resultList = query.getResultList();

        /**
         * 查询出总页数
         */
        sql = new StringBuilder("select count(id) from PresentPack p where p.available = 1");
        parameterIndex = 1;
        if (conditions != null) {
            Set<String> keys = conditions.keySet();
            Iterator<String> iterator = keys.iterator();

            while (iterator.hasNext()) {
                String key = iterator.next();
                sql.append(" and p." + key + " = ?" + parameterIndex++);
            }
        }
        sql.append(" and p.expiryTime > ?" + parameterIndex++);
        // 获取结果
        query = em.createQuery(sql.toString());
        parameterIndex = 1;
        if (conditions != null) {
            Set<String> keys = conditions.keySet();
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                query.setParameter(parameterIndex++, conditions.get(key));
            }
        }
        query.setParameter(parameterIndex++, nowTime);//当前时间
        List result = query.getResultList();
        Long totalCount = 0L;
        if (result.size() > 0) totalCount = (Long) result.get(0);


        /**
         * 封装结果
         */
        DomainPage returnDomainPage = new DomainPage(domainPage.getPageSize(), domainPage.getPageIndex(), totalCount);
        returnDomainPage.setDomains(resultList);
        return returnDomainPage;
    }

    @Override
    public DomainPage getValidPack(DomainPage domainPage, Map<String, Object> conditions) {
        Long pageIndex = domainPage.getPageIndex();
        Long pageSize = domainPage.getPageSize();

        Long organizationId = (Long) conditions.get("organizationId");
        AuditStatusEnum auditStatus = (AuditStatusEnum) conditions.get("auditStatus");
        PresentPackTypeEnum presentPackType = (PresentPackTypeEnum) conditions.get("presentPackType");

        // 获取满足条件的结果
        StringBuilder sql = new StringBuilder("SELECT pp.* FROM poi_present_pack pp");
        sql.append(" WHERE pp.available = 1 AND pp.id IN (");
        sql.append(" SELECT DISTINCT(p.present_pack_id)");
        sql.append(" FROM poi_present p");
        sql.append(" WHERE p.audit_status = 2");
        sql.append(" AND p.present_status = 0");
        sql.append(" )");
        sql.append(" AND pp.present_pack_type = '" + presentPackType.toString() + "'");
        sql.append(" AND pp.organization_id = " + organizationId);
        sql.append(" AND pp.audit_status = " + auditStatus.ordinal());
        sql.append(" AND pp.expiry_time > '" + DateUtil.gap(new Date(), "yyyy-MM-dd HH:mm:ss") + "'");
        sql.append(" LIMIT " + (pageIndex - 1) * pageSize + "," + pageIndex * pageSize + ";");
        Query query = em.createNativeQuery(sql.toString(), PresentPack.class);
        List<PresentPack> resultList = query.getResultList();

        // 获取满足条件的总记录数
        sql = new StringBuilder("SELECT COUNT(pp.id) FROM poi_present_pack pp");
        sql.append(" WHERE pp.available = 1 AND pp.id IN (");
        sql.append(" SELECT DISTINCT(p.present_pack_id)");
        sql.append(" FROM poi_present p");
        sql.append(" WHERE p.audit_status = 2");
        sql.append(" AND p.present_status = 0");
        sql.append(" )");
        sql.append(" AND pp.present_pack_type = '" + presentPackType.toString() + "'");
        sql.append(" AND pp.organization_id = " + organizationId);
        sql.append(" AND pp.audit_status = " + auditStatus.ordinal());
        sql.append(" AND pp.expiry_time > '" + DateUtil.gap(new Date(), "yyyy-MM-dd HH:mm:ss") + "';");
        Long totalCount = 0L;
        query = em.createNativeQuery(sql.toString());
        List result = query.getResultList();
        if (result.size() == 1) {
            totalCount = Long.parseLong(result.get(0).toString());
        }

        DomainPage returnDomainPage = new DomainPage(pageSize, pageIndex, totalCount);
        returnDomainPage.setDomains(resultList);
        return returnDomainPage;
    }

    @Override
    public String getEntityByDistributors(int i, String distributionCode) {
        StringBuffer sql = new StringBuffer("select o.organization_name from sys_organization as o where");
        sql.append(" o.account_role = " + i);
        sql.append(" and o.distribution_code = " + distributionCode);
        String maxPackIdentifier = "";
        if (distributionCode != null && distributionCode != "") {
            Query query = em.createNativeQuery(sql.toString());
            List list = query.getResultList();
            System.out.println(list.size());
            if (list.size() == 1) {
                maxPackIdentifier = (String) list.get(0);
            } else {
                maxPackIdentifier = "无";
            }
        }
        return maxPackIdentifier;

    }


    @Override
    public DomainPage getEntitiesPagesByList(Map<String, Object> fuzzyConditions, DomainPage domainPage) {
        long pageIndex = domainPage.getPageIndex();
        long pageSize = domainPage.getPageSize();

        String organizationName = (String) fuzzyConditions.get("organizationName");
        StringBuilder sql = new StringBuilder("select * from sys_account a ,(");
        sql.append(" select id from sys_organization where account_role = 5");
        if (StringUtils.isNotBlank(organizationName)) {
            sql.append(" and organization_name like '%" + organizationName + "%'");
        }
        sql.append(" ) b ");
        sql.append("  where a.organization_id = b.id");
        sql.append("  and a.position_role = 'POSITION_ROLE_ADMIN'");
        sql.append("  order by a.update_time desc");
        sql.append(" LIMIT " + (pageIndex - 1) * pageSize + "," + pageIndex * pageSize + ";");

        Query query = em.createNativeQuery(sql.toString(), Account.class);
        List<T> resultList = query.getResultList();

        /**
         * 查询满足该条件的总记录
         */
        sql = new StringBuilder("select COUNT(a.id) from sys_account a ,(");
        sql.append(" select id from sys_organization where account_role = 5");
        if (StringUtils.isNotBlank(organizationName)) {
            sql.append(" and organization_name like '%" + organizationName + "%'");
        }
        sql.append(" ) b ");
        sql.append("  where a.organization_id = b.id");
        sql.append("  and a.position_role = 'POSITION_ROLE_ADMIN'");

        long totalCount = 0L;
        query = em.createNativeQuery(sql.toString());
        if (query.getResultList().size() == 1) {
            totalCount = Long.parseLong(query.getResultList().get(0).toString());
        }

        /**
         * 封装返回结果
         */
        DomainPage returnDomainPage = new DomainPage(pageSize, pageIndex, totalCount);
        returnDomainPage.setDomains(resultList);

        return returnDomainPage;
    }

    @Override
    @SuppressWarnings("all")
    public long getPresentPackByConditions(Map<String, Object> conditions) {
        Long presentPackId = (Long) conditions.get("presentPackId");
        PresentStatusEnum presentStatus = (PresentStatusEnum) conditions.get("presentStatus");

        StringBuilder sql = new StringBuilder("SELECT COUNT(p.id) FROM poi_present p");
        sql.append(" WHERE p.available = " + AvailableEnum.AVAILABLE.ordinal());
        if (presentPackId != null) sql.append(" and p.present_pack_id = " + presentPackId);
        if (presentStatus != null) sql.append(" and p.present_status = " + presentStatus.ordinal());

        Query query = em.createNativeQuery(sql.toString());
        List result = query.getResultList();

        long totalCount = 0;
        if (result.size() == 1) {
            totalCount = Long.parseLong(result.get(0).toString());
        }
        return totalCount;
    }

    @Override
    public DomainPage getTheBusinessPremises(Map<String, Object> conditions, Map<String, Object> fuzzyConditions, Map<String, List<Object>> collectionConditions, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;

        Long provinceId = (Long) conditions.get("provinceId");
        Long organizationId = (Long) conditions.get("organizationId");
        String siteName = (String) fuzzyConditions.get("siteName");

        StringBuilder sql = new StringBuilder("select * from biz_site c ,(select id from sys_organization ");

        queryConditionsSplice(conditions, collectionConditions, provinceId, organizationId, siteName, sql);

        sql.append("  order by c.update_time desc");
        sql.append("  LIMIT " + (pageIndex - 1) * pageSize + "," + pageIndex * pageSize + ";");

        Query query = em.createNativeQuery(sql.toString(), Site.class);
        List<T> resultList = query.getResultList();

        /**
         * 查询满足该条件的总记录
         */
        sql = new StringBuilder("select count(c.id) from biz_site c ,(select id from sys_organization ");

        queryConditionsSplice(conditions, collectionConditions, provinceId, organizationId, siteName, sql);
        long totalCount = 0L;
        query = em.createNativeQuery(sql.toString());
        if (query.getResultList().size() == 1) {
            totalCount = Long.parseLong(query.getResultList().get(0).toString());
        }

        /**
         * 封装返回结果
         */
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.setDomains(resultList);

        return domainPage;

    }

    private void queryConditionsSplice(Map<String, Object> conditions, Map<String, List<Object>> collectionConditions, Long provinceId, Long organizationId, String siteName, StringBuilder sql) {
        if (collectionConditions != null) {
            Set<String> collectionFileNames = collectionConditions.keySet();
            Iterator<String> collectionIterator = collectionFileNames.iterator();
            for (int i = 1; i <= collectionFileNames.size(); i++) {
                String collectionFileName = collectionIterator.next();
                sql.append(" where " + collectionFileName + " in ( ");
                List<Object> invalues = collectionConditions.get(collectionFileName);
                for (int m = 1; m <= invalues.size(); m++) {
                    if (m == 1) {
                        sql.append(collectionConditions.get(collectionFileName).get(m - 1));
                    } else {
                        sql.append("," + collectionConditions.get(collectionFileName).get(m - 1));
                    }
                }
                sql.append(") ) o");
            }
        }

        sql.append("  where c.organization_id = o.id");

        /**精确查询条件**/
        if (conditions != null) {
            if (provinceId != null)
                sql.append(" and c.province_id = " + provinceId);
            if (organizationId != null)
                sql.append(" and c.organization_id = " + organizationId);
        }

        /**模糊查询条件**/
        if (StringUtils.isNotBlank(siteName)) {
            sql.append(" and c.site_name like '%" + siteName + "%'");
        }
    }


}