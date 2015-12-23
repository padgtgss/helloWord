/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.biz.impl;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.biz.AccountRelationDao;
import com.pemass.persist.domain.jpa.biz.AccountRelation;
import com.pemass.persist.domain.jpa.biz.Product;
import com.pemass.persist.domain.jpa.sys.Organization;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: AccountRelationDaoImpl
 * @Author: Administrator
 * @CreateTime: 2014-12-17 09:58
 */
@Repository
public class AccountRelationDaoImpl extends JPABaseDaoImpl implements AccountRelationDao {

    /**
     * 查询所有商户
     *
     * @param name      条件查询商户名称
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public DomainPage getAllBusinesses(String name, int accountRole, Long organizationId, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;

        String sql = "select o.id id,o.organization_name name,o.city_id city,o.province_id province,o.organization_phone phone,a.audit_status statu,a.parent_organization_id pid from ";
        sql = sql + " (SELECT * from sys_organization where available = 1 and audit_status = 2 and account_role <> 5 and id <> " + organizationId;
        if (name != null && name != "") {
            sql = sql + " and organization_name LIKE '%" + name + "%'";
        }

        if (accountRole == 0 || accountRole == 1) {
            sql = sql + " ) o LEFT JOIN  (select * from biz_account_relation where available = 1 and organization_id = " + organizationId + ") a ";
            sql = sql + " on o.id = a.parent_organization_id";
        } else {
            sql = sql + " ) o LEFT JOIN  (select * from biz_account_relation where available = 1 and parent_organization_id = " + organizationId + ") a ";
            sql = sql + " on o.id = a.organization_id";
        }


        sql = sql + " order by a.update_time desc";
        Query query = em.createNativeQuery(sql);
        //查询总记录
        Long totalCount = (long) query.getResultList().size();

        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List<Product> resultList = query.getResultList();

        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);
        return domainPage;
    }

    @Override
    public DomainPage getAllProduct(long organizationId, Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, Map<String, Object> compoundFieldNameValueMap, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;
        StringBuffer sb = new StringBuffer();
        sb.append(" select p.preview_image,p.product_name,p.market_price,p.stock_number,p.sale_number,c.id,c.organization_name,c.audit_status,c.update_time,p.id pid from biz_product p LEFT  JOIN ");
        sb.append(" (select o.id,o.organization_name,b.audit_status,b.update_time from ");
        sb.append(" (select g.id,g.organization_name from sys_organization g where g.audit_status = 2 and g.available = 1 and g.account_role <> 5) o LEFT JOIN  ");
        sb.append(" (select * from biz_account_relation r where r.organization_id = " + organizationId + " and  r.available = 1 order by r.update_time) b ");
        sb.append(" on o.id = b.parent_organization_id group by o.id ) c on p.organization_id = c.id where c.id <> " + organizationId + " and p.available = 1 and p.parent_product_id = 0 and p.product_status = 2 and p.is_distribution = 1");

        Set<String> fieldNames = fieldNameValueMap.keySet();
        Iterator<String> iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            sb.append(" and c." + fieldName + " = ?" + i);
        }

        Set<String> fuzzyFieldNames = fuzzyFieldNameValueMap.keySet();
        Iterator<String> fuzzyIterator = fuzzyFieldNames.iterator();
        int fuzzyBegin = fieldNames.size() == 0 ? 0 : fieldNames.size();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            sb.append(" and p." + fuzzyFieldName + " like ?" + (fuzzyBegin + i));
        }
        if (compoundFieldNameValueMap != null) {
            if (compoundFieldNameValueMap.get("startPrice") != null) {
                sb.append(" and p.market_price >= " + (Float) compoundFieldNameValueMap.get("startPrice"));
            }
            if (compoundFieldNameValueMap.get("endPrice") != null) {
                sb.append(" and p.market_price <= " + (Float) compoundFieldNameValueMap.get("endPrice"));
            }
            if (compoundFieldNameValueMap.get("startSaleNumber") != null) {
                sb.append(" and p.sale_number >= " + (Integer) compoundFieldNameValueMap.get("startSaleNumber"));
            }
            if (compoundFieldNameValueMap.get("endSaleNumber") != null) {
                sb.append(" and p.sale_number <= " + (Integer) compoundFieldNameValueMap.get("endSaleNumber"));
            }
        }
        sb.append(" order by c.update_time desc");
        Query query = em.createNativeQuery(sb.toString());

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

        Long totalCount = (long) query.getResultList().size();

        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List resultList = query.getResultList();

        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);
        return domainPage;
    }

    @Override
    public List<Organization> getAllMerchants(long organizationId) {
        String sql = "select o.id,o.organization_name from sys_organization o where o.id <> " + organizationId + " and o.audit_status = 2 and o.available = 1 and o.account_role <> 5  ORDER BY o.update_time";

        Query query = em.createNativeQuery(sql);

        List<Organization> list = query.getResultList();
        return list;
    }

    @Override
    public DomainPage getPricingRequestList(long organizationId, Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;

        StringBuffer sb = new StringBuffer();
        sb.append("select * from biz_account_relation b where b.available = 1 and b.discount_status <> 0");

        Set<String> fieldNames = fieldNameValueMap.keySet();
        Iterator<String> iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            sb.append(" and b." + fieldName + " = ?" + i);
        }

        Set<String> fuzzyFieldNames = fuzzyFieldNameValueMap.keySet();
        Iterator<String> fuzzyIterator = fuzzyFieldNames.iterator();
        int fuzzyBegin = fieldNames.size() == 0 ? 0 : fieldNames.size();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            sb.append(" and b." + fuzzyFieldName + " like ?" + (fuzzyBegin + i));
        }
        sb.append(" ORDER BY b.update_time");

        Query query = em.createNativeQuery(sb.toString(), AccountRelation.class);

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

        Long totalCount = (long) query.getResultList().size();

        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);

        List<AccountRelation> list = query.getResultList();
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(list);
        return domainPage;
    }

    @Override
    public DomainPage getAccountRelation(Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap,
                                         Map<String, Object> compoundFieldNameValueMap, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;

        StringBuffer sb = new StringBuffer();
        sb.append("select r.id rid,o.province_id pid,o.city_id cid from biz_account_relation r,sys_organization o");
        if (compoundFieldNameValueMap.get("flag").equals("0")) {
            sb.append(" where r.organization_id = o.id");
        } else {
            sb.append(" where r.parent_organization_id = o.id");
        }
        sb.append(" and r.available = 1 and o.available = 1 and o.audit_status = 2");
        Set<String> fieldNames = fieldNameValueMap.keySet();
        Iterator<String> iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            sb.append(" and r." + fieldName + " = ?" + i);
        }


        sb.append(" ORDER BY r.update_time");

        Query query = em.createNativeQuery(sb.toString());

        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            query.setParameter(i, fieldNameValueMap.get(fieldName));
        }


        Long totalCount = (long) query.getResultList().size();

        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);

        List<AccountRelation> list = query.getResultList();
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(list);
        return domainPage;
    }

    @Override
    public DomainPage getPricingRequestList(Map<String, Object> fuzzyFieldNameValueMap, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT r.id rid,o.id oid FROM biz_account_relation r,sys_organization o WHERE");
        sb.append(" r.available = 1 AND o.available AND o.audit_status = 2 AND r.parent_organization_id = o.id ");
        sb.append(" and r.discount_status = 1");
        sb.append(" ORDER BY r.update_time");

        Query query = em.createNativeQuery(sb.toString());

        Long totalCount = (long) query.getResultList().size();

        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);

        List<AccountRelation> list = query.getResultList();
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(list);
        return domainPage;
    }

    @Override
    public DomainPage getEntitiesByFieldList(Class clazz, Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, long pageIndex, long pageSize) {
        /**
         *查询满足条件的结果
         */
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;

        String sql = "select c from Organization o," + clazz.getName() + " c where c.available = 1 and o.auditStatus = 2 and o.available = 1 and c.organizationId = o.id";


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
                sql = sql + " and o." + fuzzyFieldName + " like ?" + parameterIndex++;
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

    @Override
    public List<Product> selectDistributionOfGoods(long organizationId, long parentOrganizationId) {
        String sql = "select * from biz_product where available = 1 and parent_product_id in ";
        sql += "(select id from biz_product where available = 1 and is_distribution = 1 and organization_id =" + parentOrganizationId + ") and organization_id = " + organizationId;
        Query query = em.createNativeQuery(sql, Product.class);
        List<Product> list = query.getResultList();
        return list;
    }

    @Override
    public DomainPage selectDistributorByPage(Map<String, Object> map, long organizationId, int auditStatus, int discountStatus, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;
        StringBuffer sb = new StringBuffer();

        sb.append("select r.id rid,o.id oid from biz_account_relation r , sys_organization o where o.id= r.organization_id and r.parent_organization_id = " + organizationId);
        sb.append(" and r.audit_status = " + auditStatus + " and r.available = 1 and o.available = 1 and o.audit_status = 2 and o.account_role != 5");

        if (map != null && map.size() > 0) {
            String organizationName = map.get("organizationName").toString();
            sb.append(" and o.organization_name LIKE '%" + organizationName + "%'");
        }
        if (discountStatus == 1) {
            sb.append(" and r.discount_status = 1");
        }
        sb.append(" order by r.update_time desc ");
        Query query = em.createNativeQuery(sb.toString());

        Long totalCount = (long) query.getResultList().size();

        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);

        List<AccountRelation> list = query.getResultList();
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(list);
        return domainPage;
    }

    @Override
    public DomainPage selectSupplierByPage(Map<String, Object> map, long organizationId, int auditStatus, int discountStatus, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;
        StringBuffer sb = new StringBuffer();

        sb.append("select r.id rid,o.id oid from biz_account_relation r , sys_organization o where o.id= r.parent_organization_id and r.organization_id = " + organizationId);
        if (auditStatus != -1) {
            sb.append(" and r.audit_status = " + auditStatus + " and r.available = 1 and o.available = 1 and o.audit_status = 2 and o.account_role != 5");
        } else {
            sb.append("  and r.available = 1 and o.available = 1 and o.audit_status = 2 and o.account_role != 5");
        }

        if (map != null && map.size() > 0) {
            String organizationName = map.get("organizationName").toString();
            sb.append(" and o.organization_name LIKE '%" + organizationName + "%'");
        }
        if (discountStatus == 1) {
            sb.append(" and r.discount_status = 1");
        }
        sb.append(" order by r.update_time desc ");
        Query query = em.createNativeQuery(sb.toString());

        Long totalCount = (long) query.getResultList().size();

        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);

        List<AccountRelation> list = query.getResultList();
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(list);
        return domainPage;
    }

}