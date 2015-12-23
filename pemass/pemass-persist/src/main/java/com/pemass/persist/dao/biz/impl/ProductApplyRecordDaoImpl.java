package com.pemass.persist.dao.biz.impl;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.biz.ProductApplyRecordDao;
import com.pemass.persist.enumeration.AuditStatusEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: ProductApplyRecordDaoImpl
 * @Author: luoc
 * @CreateTime: 2015-06-01 15:38
 */
@Service
public class ProductApplyRecordDaoImpl extends JPABaseDaoImpl implements ProductApplyRecordDao {

    @Override
    public DomainPage selectApplyRecordByConditions(Map<String, Object> conditions, DomainPage domainPage) {
        // 获取参数
        String productName = (String) conditions.get("productName");
        Long organizationId = (Long) conditions.get("organizationId");
        Date startTime = (Date) conditions.get("startTime");
        Date endTime = (Date) conditions.get("endTime");
        AuditStatusEnum auditStatus = (AuditStatusEnum) conditions.get("auditStatus");
        long pageIndex = domainPage.getPageIndex();
        long pageSize = domainPage.getPageSize();

        // 拼HQL
        StringBuilder sql = new StringBuilder("SELECT par.id parId FROM biz_product_apply_record par, biz_product product");
        sql.append(" WHERE par.available = 1 AND product.available = 1 AND par.product_id = product.id");
        sql.append(" AND par.parent_product_id IN (");
        sql.append(" SELECT p.id FROM biz_product p WHERE");
        sql.append(" p.available = 1 AND p.organization_id = ?1");
        sql.append(" )");
        if (auditStatus != null) {
            if(auditStatus.equals(AuditStatusEnum.HAS_AUDIT)){
                sql.append(" AND par.audit_status = "+ 2);
            }else if(auditStatus.equals(AuditStatusEnum.NONE_AUDIT)){
                sql.append(" AND par.audit_status = "+ 0);
            }else if(auditStatus.equals(AuditStatusEnum.ING_AUDIT)){
                sql.append(" AND par.audit_status = "+ 1);
            }else{
                sql.append(" AND par.audit_status = "+ 3);
            }
        }
        if (startTime != null && endTime != null) sql.append(" AND par.create_time BETWEEN ?3 AND ?4");
        if (StringUtils.isNotBlank(productName)) sql.append(" AND product.product_name like ?5");
        sql.append(" ORDER BY par.update_time DESC");

        Query query = em.createNativeQuery(sql.toString());

        // 设置值
        query.setParameter(1, organizationId);
        if (startTime != null && endTime != null) {
            query.setParameter(3, startTime);
            query.setParameter(4, endTime);
        }
        if (StringUtils.isNotBlank(productName)) query.setParameter(5, "%" + productName + "%");
        long totalCount = (long)query.getResultList().size();

        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List resultList = query.getResultList();

        // 封装返回结果
        DomainPage resultDomainPage = new DomainPage(pageSize, pageIndex, totalCount);
        resultDomainPage.setDomains(resultList);

        return resultDomainPage;
    }

    @Override
    public DomainPage selectApplyRecordByConditions(Long organizationId, String productName, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;
        //查询满足条件的结果
        String sql = "select r.id rid,p.id pid from";
        sql += " biz_product_apply_record r,biz_product p";
        sql += " where r.product_id = p.id and r.organization_id = " + organizationId;

        if (productName != null && productName != "") {
            sql += " and p.product_name like " + "'%" + productName + "%'";
        }
        sql += " and r.available = 1";
        sql += " and p.available = 1";
        sql = sql + " order by r.update_time desc";
        Query query = em.createNativeQuery(sql);

        Long totalCount = (long) query.getResultList().size();

        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List resultList = query.getResultList();


        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);
        return domainPage;
    }

    /**
     * 获取满足条件的总纪录
     *
     * @param organizationId
     * @param startTime
     * @param endTime
     * @param auditStatus
     * @return
     */
    private Long getTotalCount(Long organizationId, Date startTime, Date endTime, AuditStatusEnum auditStatus) {
        // 拼HQL
        StringBuilder sql = new StringBuilder(" SELECT COUNT(par.id) FROM ProductApplyRecord par");
        sql.append(" WHERE par.available = 1");
        sql.append(" AND par.parentProduct.id in");
        sql.append(" ( SELECT p.id FROM Product p");
        sql.append(" WHERE p.available = 1");
        sql.append(" AND p.organization.id = ?1");
        sql.append(" )");
        if (auditStatus != null) {
            sql.append(" AND par.auditStatus = ?2");
        }
        if (startTime != null && endTime != null) {
            sql.append(" AND par.createTime BETWEEN ?3 AND ?4");
        }
        Query query = em.createQuery(sql.toString());

        // 设置值
        query.setParameter(1, organizationId);
        if (auditStatus != null) query.setParameter(2, auditStatus);
        if (startTime != null && endTime != null) {
            query.setParameter(3, startTime);
            query.setParameter(4, endTime);
        }

        List list = query.getResultList();
        Long totalCount = 0L;
        if (list.size() == 1) {
            totalCount = Long.parseLong(list.get(0).toString());
        }
        return totalCount;
    }

}
