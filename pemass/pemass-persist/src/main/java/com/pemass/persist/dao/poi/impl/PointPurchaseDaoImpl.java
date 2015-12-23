/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.poi.impl;

import com.google.common.base.Preconditions;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.dao.poi.PointPurchaseDao;
import com.pemass.persist.domain.jpa.poi.PointPurchase;
import com.pemass.persist.enumeration.AuditStatusEnum;
import com.pemass.persist.enumeration.PointTypeEnum;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigInteger;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: PointDaoImpl
 * @Author: cassiel.liu
 * @CreateTime: 2014-10-20 15:02
 */
@Repository(value = "pointPurchaseDao")
public class PointPurchaseDaoImpl extends JPABaseDaoImpl implements PointPurchaseDao {


    @Override
    public DomainPage getEntitiesPages(Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;

        //查询满足条件的记录
        String sql = "select c,o from PointPurchase c,Organization o " +
                " where c.organizationId = o.id and c.available = 1 and o.available = 1";
        //循环精确查询匹配参数
        Set<String> fieldNames = fieldNameValueMap.keySet();
        Iterator<String> iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            sql = sql + " and c." + fieldName + " = ?" + i;
        }

        //循环模糊查询匹配参数
        Set<String> fuzzyFieldNames = fuzzyFieldNameValueMap.keySet();
        Iterator<String> fuzzyIterator = fuzzyFieldNames.iterator();
        int fuzzyBegin = fieldNames.size() == 0 ? 0 : fieldNames.size();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            if ("purchaseIdentifier".equals(fuzzyFieldName)) {
                sql = sql + " and c." + fuzzyFieldName + " like ?" + (fuzzyBegin + i);
            } else if ("organizationName".equals(fuzzyFieldName)) {
                sql = sql + " and o." + fuzzyFieldName + " like ?" + (fuzzyBegin + i);
            } else {
                sql = sql + " and c." + fuzzyFieldName + " like ?" + (fuzzyBegin + i);
            }
        }

        sql = sql + " order by c.updateTime desc";
        Query query = em.createQuery(sql);

        //设置精确查询匹配参数的值
        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            query.setParameter(i, fieldNameValueMap.get(fieldName));
        }

        //设置模糊查询匹配参数的值
        fuzzyIterator = fuzzyFieldNames.iterator();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            query.setParameter((fuzzyBegin + i), "%" + fuzzyFieldNameValueMap.get(fuzzyFieldName) + "%");
        }

        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List resultList = query.getResultList();

        //查询总记录
        sql = "select count(c) from PointPurchase c,Organization o " +
                " where c.organizationId = o.id and c.available = 1 and o.available = 1 ";
        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            sql = sql + " and c." + fieldName + " = ?" + i;
        }

        fuzzyIterator = fuzzyFieldNames.iterator();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            if ("purchaseIdentifier".equals(fuzzyFieldName)) {
                sql = sql + " and c." + fuzzyFieldName + " like ?" + (fuzzyBegin + i);
            } else if ("organizationName".equals(fuzzyFieldName)) {
                sql = sql + " and o." + fuzzyFieldName + " like ?" + (fuzzyBegin + i);
            } else {
                sql = sql + " and c." + fuzzyFieldName + " like ?" + (fuzzyBegin + i);
            }
        }

        sql = sql + " order by c.updateTime desc";
        query = em.createQuery(sql);

        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            query.setParameter(i, fieldNameValueMap.get(fieldName));
        }

        fuzzyIterator = fuzzyFieldNames.iterator();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            query.setParameter((fuzzyBegin + i), "%" + fuzzyFieldNameValueMap.get(fuzzyFieldName) + "%");
        }

        Long totalCount = 0L;
        if (query.getResultList().size() > 0) {
            totalCount = (Long) query.getResultList().get(0);
        }

        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);
        return domainPage;
    }

    @Override
    public DomainPage getAuditRecordByPages(Map<String, Object> fuzzyMap, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;

        String sql = "select c,o from PointPurchase c,Organization o " +
                " where c.organizationId = o.id and c.available = 1 and o.available = 1 and c.auditStatus <> ?1 ";

        if (fuzzyMap != null) {
            if (fuzzyMap.get("purchaseIdentifier") != null) {
                sql = sql + " and c.purchaseIdentifier like ?2 ";
            }
            if (fuzzyMap.get("organizationName") != null) {
                sql = sql + " and o.organizationName like ?3 ";
            }
            if (fuzzyMap.get("pointType") != null) {
                sql = sql + " and c.pointType = ?4 ";
            }
            if (fuzzyMap.get("startDate") != null && fuzzyMap.get("endDate") != null) {
                sql = sql + " and DATE_FORMAT(c.purchaseTime,'%y-%m-%d') between DATE_FORMAT(?5,'%y-%m-%d') and DATE_FORMAT(?6,'%y-%m-%d') ";
            }
        }
        sql = sql + " order by c.updateTime desc";

        Query query = em.createQuery(sql);
        query.setParameter(1, AuditStatusEnum.NONE_AUDIT);

        if (fuzzyMap != null) {
            if (fuzzyMap.get("purchaseIdentifier") != null) {
                query.setParameter(2, "%" + fuzzyMap.get("purchaseIdentifier") + "%");
            }
            if (fuzzyMap.get("organizationName") != null) {
                query.setParameter(3, "%" + fuzzyMap.get("organizationName") + "%");
            }
            if (fuzzyMap.get("pointType") != null) {
                query.setParameter(4, fuzzyMap.get("pointType"));
            }
            if (fuzzyMap.get("startDate") != null && fuzzyMap.get("endDate") != null) {
                query.setParameter(5, fuzzyMap.get("startDate"));
                query.setParameter(6, fuzzyMap.get("endDate"));
            }
        }

        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List resultList = query.getResultList();

        sql = "select count(c) from PointPurchase c,Organization o " +
                " where c.organizationId = o.id and c.available = 1 and o.available = 1 and c.auditStatus <> ?1 ";

        if (fuzzyMap != null) {
            if (fuzzyMap.get("purchaseIdentifier") != null) {
                sql = sql + " and c.purchaseIdentifier like ?2 ";
            }
            if (fuzzyMap.get("organizationName") != null) {
                sql = sql + " and o.organizationName like ?3 ";
            }
            if (fuzzyMap.get("pointType") != null) {
                sql = sql + " and c.pointType = ?4 ";
            }
            if (fuzzyMap.get("startDate") != null && fuzzyMap.get("endDate") != null) {
                sql = sql + " and DATE_FORMAT(c.purchaseTime,'%y-%m-%d') between DATE_FORMAT(?5,'%y-%m-%d') and DATE_FORMAT(?6,'%y-%m-%d') ";
            }
        }
        sql = sql + " order by c.updateTime desc";

        query = em.createQuery(sql);
        query.setParameter(1, AuditStatusEnum.NONE_AUDIT);

        if (fuzzyMap != null) {
            if (fuzzyMap.get("purchaseIdentifier") != null) {
                query.setParameter(2, "%" + fuzzyMap.get("purchaseIdentifier") + "%");
            }
            if (fuzzyMap.get("organizationName") != null) {
                query.setParameter(3, "%" + fuzzyMap.get("organizationName") + "%");
            }
            if (fuzzyMap.get("pointType") != null) {
                query.setParameter(4, fuzzyMap.get("pointType"));
            }
            if (fuzzyMap.get("startDate") != null && fuzzyMap.get("endDate") != null) {
                query.setParameter(5, fuzzyMap.get("startDate"));
                query.setParameter(6, fuzzyMap.get("endDate"));
            }
        }

        List result = query.getResultList();
        Long totalCount = (Long) result.get(0);

        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);

        return domainPage;
    }

    /**
     * 查询商家所有可用积分
     *
     * @param organizationId 组织机构id
     * @param pointType      积分类型
     * @return
     */
    @Override
    public List<PointPurchase> getJudgeUseableAmountDao(Long organizationId, PointTypeEnum pointType) {
        StringBuffer sb = new StringBuffer();
        sb.append("from PointPurchase p  where");
        sb.append(" p.available = 1 ");
        sb.append(" and p.organizationId =?1 ");
        sb.append(" and pointType =?2 ");
        sb.append(" and expiryTime > ?3 ");
        sb.append(" and auditStatus = ?4 ");
        sb.append(" and p.useableAmount>0 ");
        sb.append(" order by p.expiryTime asc");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, organizationId);
        query.setParameter(2, pointType);
        query.setParameter(3, new Date());
        query.setParameter(4, AuditStatusEnum.HAS_AUDIT);

        return query.getResultList();

    }

    @Override
    public DomainPage getEntitiesPagesByTime(Class clazz, String fieldName, Object start, Object end, Map<String, Object> filedMap, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;

        Query query = null;
        String sql = "select c from " + clazz.getName() + " c where c.available = 1 ";

        Set<String> fieldNames = filedMap.keySet();
        Iterator<String> iterator = fieldNames.iterator();

        if (end == null) {
            sql = sql + " and date_format(c." + fieldName + ",'%y-%m-%d') = date_format(?1 ,'%y-%m-%d') ";
        } else {
            sql = sql + " and date_format(c." + fieldName + ",'%y-%m-%d') >= date_format(?1 ,'%y-%m-%d') " +
                    " and date_format(c." + fieldName + ",'%y-%m-%d') <= date_format(?2 ,'%y-%m-%d') ";
        }

        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fuzzyFieldName = iterator.next();
            if (end == null) {
                sql = sql + " and c." + fuzzyFieldName + "= ?" + (i + 1);
            } else {
                sql = sql + " and c." + fuzzyFieldName + "= ?" + (i + 2);
            }
        }
        sql = sql + " order by c.updateTime desc ";

        query = em.createQuery(sql);
        query.setParameter(1, start);
        if (end != null) {
            query.setParameter(2, end);
        }

        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String filed = iterator.next();
            if (end == null) {
                query.setParameter((i + 1), filedMap.get(filed));
            } else {
                query.setParameter((i + 2), filedMap.get(filed));
            }
        }

        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List resultList = query.getResultList();

        sql = "select count(c) from " + clazz.getName() + " c where c.available = 1 ";

        if (end == null) {
            sql = sql + " and date_format(c." + fieldName + ",'%y-%m-%d') = date_format(?1 ,'%y-%m-%d') ";
        } else {
            sql = sql + " and date_format(c." + fieldName + ",'%y-%m-%d') >= date_format(?1 ,'%y-%m-%d') " +
                    " and date_format(c." + fieldName + ",'%y-%m-%d') <= date_format(?2 ,'%y-%m-%d') ";
        }

        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fuzzyFieldName = iterator.next();
            if (end == null) {
                sql = sql + " and c." + fuzzyFieldName + "= ?" + (i + 1);
            } else {
                sql = sql + " and c." + fuzzyFieldName + "= ?" + (i + 2);
            }
        }
        sql = sql + " order by c.updateTime desc ";

        query = em.createQuery(sql);
        query.setParameter(1, start);
        if (end != null) {
            query.setParameter(2, end);
        }

        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String filed = iterator.next();
            if (end == null) {
                query.setParameter((i + 1), filedMap.get(filed));
            } else {
                query.setParameter((i + 2), filedMap.get(filed));
            }
        }

        List result = query.getResultList();
        Long totalCount = (Long) result.get(0);

        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);

        return domainPage;
    }

    @Override
    public List getPointPaymentDetails(String pointType, Long organizationId, Map<String, Object> fieldMap, long pageIndex, long pageSize) {
        String sql = "SELECT * FROM (" +
                " ( SELECT pp.id AS id,pp.amount AS amount,'PURCHASE' AS type,pp.purchase_time AS t " +
                " FROM poi_point_purchase pp " +
                " WHERE pp.available = 1" +
                " AND pp.point_type = '" + pointType + "'" +
                " AND pp.organization_id = " + organizationId +
                " AND DATE_FORMAT(pp.expiry_time, '%y-%m-%d') > DATE_FORMAT(NOW(), '%y-%m-%d')" +
                " ) UNION ( " +
                " SELECT c.consume_target_id AS id,c.amount AS amount,c.consume_type AS type,c.create_time AS t " +
                " FROM poi_consume_detail c LEFT JOIN poi_point_purchase p ON p.id = c.point_purchase_id " +
                " WHERE c.available = 1 AND c.point_type = '" + pointType + "'  AND p.organization_id = " + organizationId +
                " )) AS temp ";

        if (fieldMap.size() != 0 || fieldMap != null) {
            if (fieldMap.get("startTime") != null && fieldMap.get("endTime") != null) {
                if (fieldMap.get("startTime") == fieldMap.get("endTime")) {
                    sql = sql + " where DATE_FORMAT(t,'%y-%m-%d') = DATE_FORMAT('" + fieldMap.get("startTime") + "','%y-%m-%d') ";
                } else {
                    sql = sql + " where DATE_FORMAT(t, '%y-%m-%d') BETWEEN DATE_FORMAT('" + fieldMap.get("startTime") + "', '%y-%m-%d') " +
                            " and DATE_FORMAT('" + fieldMap.get("endTime") + "', '%y-%m-%d')";
                }
            }
        }
        sql = sql + " ORDER BY t DESC limit " + (pageIndex - 1) * pageSize + "," + pageSize * pageIndex;
        Query query = em.createNativeQuery(sql);
        List resultList = query.getResultList();
        return resultList;
    }

    @Override
    public Long getPointPaymentDetailsCount(String pointType, Long organizationId, Map<String, Object> fieldMap) {
        String sql = "SELECT count(*) FROM (" +
                " ( SELECT pp.id AS id,pp.amount AS amount,'PURCHASE' AS type,pp.purchase_time AS t " +
                " FROM poi_point_purchase pp " +
                " WHERE pp.available = 1" +
                " AND pp.point_type = '" + pointType + "'" +
                " AND pp.organization_id = " + organizationId +
                " AND DATE_FORMAT(pp.expiry_time, '%y-%m-%d') > DATE_FORMAT(NOW(), '%y-%m-%d')" +
                " ) UNION ( " +
                " SELECT c.consume_target_id AS id,c.amount AS amount,c.consume_type AS type,c.create_time AS t " +
                " FROM poi_consume_detail c LEFT JOIN poi_point_purchase p ON p.id = c.point_purchase_id " +
                " WHERE c.available = 1 AND c.point_type = '" + pointType + "'  AND p.organization_id = " + organizationId +
                " )) AS temp ";
        if (fieldMap.size() != 0 || fieldMap != null) {
            if (fieldMap.get("startTime") != null && fieldMap.get("endTime") != null) {
                if (fieldMap.get("startTime") == fieldMap.get("endTime")) {
                    sql = sql + " where DATE_FORMAT(t,'%y-%m-%d') = DATE_FORMAT('" + fieldMap.get("startTime") + "','%y-%m-%d') ";
                } else {
                    sql = sql + " where DATE_FORMAT(t, '%y-%m-%d') BETWEEN DATE_FORMAT('" + fieldMap.get("startTime") + "', '%y-%m-%d') " +
                            " and DATE_FORMAT('" + fieldMap.get("endTime") + "', '%y-%m-%d')";
                }
            }
        }
        sql = sql + " ORDER BY t DESC ";
        Query query = em.createNativeQuery(sql);
        List resultList = query.getResultList();
        BigInteger count = (BigInteger) resultList.get(0);
        return count.longValue();
    }


    @Override
    public List getPointPurchaseIdByOrganizationId(Long organizationId) {
        Date nowDate = new Date();

        String sql = "select p.id from PointPurchase p where p.organizationId = ?1 and p.available = 1 and p.auditStatus = ?2 " +
                " and p.expiryTime > ?3 order by p.updateTime desc";
        Query query = em.createQuery(sql);
        query.setParameter(1, organizationId);
        query.setParameter(2, AuditStatusEnum.HAS_AUDIT);
        query.setParameter(3, nowDate);
        List result = query.getResultList();
        return result;
    }

    @Override
    public <T extends BaseDomain> DomainPage getPointUsedDetailsByFiled(Class<T> clazz, PointPurchase pointPurchase, long pageIndex, long pageSize) {

        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;

        //查询数据
        String sql = "select c from " + clazz.getName() + " c where c.available = 1 " +
                " and c.pointPurchaseId = ?1 and c.pointType = ?2 order by c.updateTime desc";

        Query query = em.createQuery(sql);
        query.setParameter(1, pointPurchase.getId());
        query.setParameter(2, pointPurchase.getPointType());

        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List result = query.getResultList();

        //获取总记录数
        sql = "select count(c) from " + clazz.getName() + " c where c.available = 1 " +
                " and c.pointPurchaseId = ?1 and c.pointType = ?2 order by c.updateTime desc";

        query = em.createQuery(sql);
        query.setParameter(1, pointPurchase.getId());
        query.setParameter(2, pointPurchase.getPointType());
        List list = query.getResultList();
        Long totalCount = (Long) list.get(0);

        //分页封装
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(result);
        return domainPage;
    }

    @Override
    public DomainPage getValidPointPurchase(Map<String, Object> conditions, DomainPage domainPage) {
        Preconditions.checkNotNull(conditions);
        Preconditions.checkNotNull(domainPage);

        Long organizationId = (Long) conditions.get("organizationId");
        PointTypeEnum pointType = (PointTypeEnum) conditions.get("pointType");
        AuditStatusEnum auditStatus = (AuditStatusEnum) conditions.get("auditStatus");
        long pageIndex = domainPage.getPageIndex();
        long pageSize = domainPage.getPageSize();
        DateTime nowTime = DateTime.now();

        /*-- 拼SQL --*/
        StringBuilder sql = new StringBuilder("SELECT p.id,p.purchase_identifier,p.amount,po.useable_amount,p.point_type,po.area");
        sql.append(" FROM poi_point_purchase p,poi_organization_point_detail po");
        sql.append(" WHERE p.available = 1 AND p.id = po.point_purchase_id");
        sql.append(" AND po.useable_amount > 0");
        sql.append(" AND p.is_automatic = 0");
        sql.append(" AND p.expiry_time > '" + nowTime.toString("yyyy-MM-dd HH:mm:ss") + "'");
        if (organizationId != null) sql.append(" AND p.organization_id = " + organizationId);
        if (pointType != null) sql.append(" AND p.point_type = '" + pointType + "'");
        if (auditStatus != null) sql.append(" AND p.audit_status = " + auditStatus.ordinal());
        sql.append(" ORDER BY p.update_time DESC");
        sql.append(" LIMIT " + (pageIndex - 1) * pageSize + "," + pageIndex * pageSize + ";");

        Query query = em.createNativeQuery(sql.toString());
        List resultList = query.getResultList();
        Long totalCount = getPointPurchaseTotalCount(organizationId, pointType, auditStatus, nowTime);

        DomainPage returnDomainPage = new DomainPage(pageSize, pageIndex, totalCount);
        returnDomainPage.setDomains(resultList);

        return returnDomainPage;
    }

    @Override
    public List<PointPurchase> getValidPointPurchaseList(Map<String, Object> conditions) {
        Preconditions.checkNotNull(conditions);

        Long organizationId = (Long) conditions.get("organizationId");
        PointTypeEnum pointType = (PointTypeEnum) conditions.get("pointType");
        AuditStatusEnum auditStatus = (AuditStatusEnum) conditions.get("auditStatus");
        DateTime nowTime = DateTime.now();
        /*-- 拼SQL --*/
        StringBuilder sql = new StringBuilder("SELECT p.id,p.purchase_identifier,p.amount,po.useable_amount,p.point_type");
        sql.append(" FROM poi_point_purchase p,poi_organization_point_detail po");
        sql.append(" WHERE p.available = 1 AND p.id = po.point_purchase_id");
        sql.append(" AND po.useable_amount > 0");
        sql.append(" AND p.is_automatic = 0");
        sql.append(" AND p.expiry_time > '" + nowTime.toString("yyyy-MM-dd HH:mm:ss") + "'");
        if (organizationId != null) sql.append(" AND p.organization_id = " + organizationId);
        if (pointType != null) sql.append(" AND p.point_type = '" + pointType + "'");
        if (auditStatus != null) sql.append(" AND p.audit_status = " + auditStatus.ordinal());
        sql.append(" ORDER BY p.update_time DESC");

        Query query = em.createNativeQuery(sql.toString());
        return query.getResultList();
    }

    @Override
    public Long getPurchaseAmountOfDayByType(PointTypeEnum pointType) {
        //创建最小值和最大值
        DateTime minDate = DateTime.now().minusDays(1).millisOfDay().withMinimumValue();
        DateTime maxDate = DateTime.now().minusDays(1).millisOfDay().withMaximumValue();

        StringBuffer sql = new StringBuffer(" SELECT SUM(pp.amount) FROM poi_point_purchase pp  ")
                .append(" WHERE pp.available = 1 ")
                .append(" AND pp.point_type= '" + pointType.toString() + "' ")
                .append(" AND pp.purchase_time >= ?1 AND pp.purchase_time <= ?2 ");

        Query query = em.createNativeQuery(sql.toString());
        query.setParameter(1, minDate.toString("yyyy-MM-dd HH:mm:ss"));
        query.setParameter(2, maxDate.toString("yyyy-MM-dd HH:mm:ss"));
        List result = query.getResultList();

        Long totalCount = 0L;
        if (result.get(0) != null) {
            totalCount = Long.parseLong(result.get(0).toString());
        }
        return totalCount;
    }

    private Long getPointPurchaseTotalCount(Long organizationId, PointTypeEnum pointType, AuditStatusEnum auditStatus, DateTime nowTime) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(p.id) ");
        sql.append(" FROM poi_point_purchase p,poi_organization_point_detail po");
        sql.append(" WHERE p.available = 1 AND p.id = po.point_purchase_id");
        sql.append(" AND po.useable_amount > 0");
        sql.append(" AND p.is_automatic = 0");
        sql.append(" AND p.expiry_time > '" + nowTime.toString("yyyy-MM-dd HH:mm:ss") + "'");
        if (organizationId != null) sql.append(" AND p.organization_id = " + organizationId);
        if (pointType != null) sql.append(" AND p.point_type = '" + pointType + "'");
        if (auditStatus != null) sql.append(" AND p.audit_status = " + auditStatus.ordinal());

        Query query = em.createNativeQuery(sql.toString());

        long totalCount = 0l;
        List list = query.getResultList();
        if (list.size() == 1) totalCount = Long.parseLong(list.get(0).toString());
        return totalCount;
    }


}
