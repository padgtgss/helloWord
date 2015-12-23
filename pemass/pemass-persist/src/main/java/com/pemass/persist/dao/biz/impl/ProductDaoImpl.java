package com.pemass.persist.dao.biz.impl;

import com.google.common.base.Preconditions;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.dao.biz.ProductDao;
import com.pemass.persist.domain.jpa.biz.Product;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: ProductDaoImpl
 * @Author: luoc
 * @CreateTime: 2014-11-04 16:57
 */
@Repository
public class ProductDaoImpl extends JPABaseDaoImpl implements ProductDao {

    public <T extends BaseDomain> DomainPage getEntitiesPagesByFieldList(Class<T> clazz, Long orgId,
                                                                         Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, Map<String, Object> compoundFieldNameValueMap, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;

        Date date = new Date();
        SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = dd.format(date);

        //查询满足条件的结果
        String sql = "select c from " + clazz.getName() + " c where ";
        sql = sql + " c.organizationId in (select parentOrganizationId from AccountRelation where organizationId=" + orgId + " and auditStatus = 2 and available = 1)";
//        sql = sql + " date_format(c.expiry_time,'%y-%m-%d') > date_format(?1,'%y-%m-%d')";
        sql = sql + " and c.available = 1";
        Set<String> fieldNames = fieldNameValueMap.keySet();
        Iterator<String> iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            if (i == 1) {
                sql = sql + " and c." + fieldName + " = ?" + i;
            } else {
                sql = sql + " and c." + fieldName + " = ?" + i;
            }
        }

        Set<String> fuzzyFieldNames = fuzzyFieldNameValueMap.keySet();
        Iterator<String> fuzzyIterator = fuzzyFieldNames.iterator();
        int fuzzyBegin = fieldNames.size() == 0 ? 0 : fieldNames.size();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            if (i == 1) {
                sql = sql + " and c." + fuzzyFieldName + " like ?" + (fuzzyBegin + i);
            } else {
                sql = sql + " and c." + fuzzyFieldName + " like ?" + (fuzzyBegin + i);
            }
        }
        if (compoundFieldNameValueMap != null) {
            if (compoundFieldNameValueMap.get("startPrice") != null) {
                sql = sql + " and c.marketPrice >= " + (Float) compoundFieldNameValueMap.get("startPrice");
            }
            if (compoundFieldNameValueMap.get("endPrice") != null) {
                sql = sql + " and c.marketPrice <= " + (Float) compoundFieldNameValueMap.get("endPrice");
            }
            if (compoundFieldNameValueMap.get("startSaleNumber") != null) {
                sql = sql + " and c.saleNumber >= " + (Integer) compoundFieldNameValueMap.get("startSaleNumber");
            }
            if (compoundFieldNameValueMap.get("endSaleNumber") != null) {
                sql = sql + " and c.saleNumber <= " + (Integer) compoundFieldNameValueMap.get("endSaleNumber");
            }
            if (compoundFieldNameValueMap.get("type") != null) {
                if (compoundFieldNameValueMap.get("type").equals("0")) {
                    sql = sql + " and c.parentProductId = 0";
                } else {
                    sql = sql + " and c.parentProductId <> 0";
                }
            }
            sql = sql + " and c.parentProductId = 0";
        }
        sql = sql + " order by c.updateTime desc";
        Query query = em.createQuery(sql);
//        query.setParameter(1, nowDate);
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

        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List<T> resultList = query.getResultList();

        //查询总记录
        sql = "select count(c) from " + clazz.getName() + " c where ";
        sql = sql + " c.organizationId in (select parentOrganizationId from AccountRelation where organizationId=" + orgId + " and auditStatus = 2 and available = 1)";
//        sql = sql + " date_format(c.expiry_time,'%y-%m-%d') > date_format(?1,'%y-%m-%d')";
        sql = sql + " and c.available = 1 ";
        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            if (i == 1) {
                sql = sql + " and c." + fieldName + " = ?" + i;
            } else {
                sql = sql + " and c." + fieldName + " = ?" + i;
            }
        }

        fuzzyIterator = fuzzyFieldNames.iterator();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            if (i == 1) {
                sql = sql + " and c." + fuzzyFieldName + " like ?" + (fuzzyBegin + i);
            } else {
                sql = sql + " and c." + fuzzyFieldName + " like ?" + (fuzzyBegin + i);
            }
        }
        if (compoundFieldNameValueMap != null) {
            if (compoundFieldNameValueMap.get("startPrice") != null) {
                sql = sql + " and c.marketPrice >= " + (Float) compoundFieldNameValueMap.get("startPrice");
            }
            if (compoundFieldNameValueMap.get("endPrice") != null) {
                sql = sql + " and c.marketPrice <= " + (Float) compoundFieldNameValueMap.get("endPrice");
            }
            if (compoundFieldNameValueMap.get("startSaleNumber") != null) {
                sql = sql + " and c.saleNumber >= " + (Integer) compoundFieldNameValueMap.get("startSaleNumber");
            }
            if (compoundFieldNameValueMap.get("endSaleNumber") != null) {
                sql = sql + " and c.saleNumber <= " + (Integer) compoundFieldNameValueMap.get("endSaleNumber");
            }
            if (compoundFieldNameValueMap.get("type") != null) {
                if (compoundFieldNameValueMap.get("type").equals("0")) {
                    sql = sql + " and c.parentProductId = 0";
                } else {
                    sql = sql + " and c.parentProductId <> 0";
                }
            }
            sql = sql + " and c.parentProductId = 0";
        }
        sql = sql + " order by c.updateTime desc";
        query = em.createQuery(sql);
//        query.setParameter(1, nowDate);
        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            query.setParameter((i), fieldNameValueMap.get(fieldName));
        }

        fuzzyIterator = fuzzyFieldNames.iterator();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            query.setParameter((fuzzyBegin + i), "%" + fuzzyFieldNameValueMap.get(fuzzyFieldName) + "%");
        }

        Long totalCount = (Long) query.getResultList().get(0);

        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);
        return domainPage;
    }

    /**
     * 多条件查询
     *
     * @param clazz
     * @param fieldNameValueMap
     * @param fuzzyFieldNameValueMap
     * @param compoundFieldNameValueMap
     * @param pageIndex
     * @param pageSize
     * @param <T>
     * @return
     */
    @Override
    public <T extends BaseDomain> DomainPage getProductsByCompoundConditions(Class<T> clazz, Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, Map<String, Object> compoundFieldNameValueMap, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;
        //查询满足条件的结果
        String sql = "select c from " + clazz.getName() + " c where ";
        sql = sql + " c.available = 1";
        Set<String> fieldNames = fieldNameValueMap.keySet();
        Iterator<String> iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            sql = sql + " and c." + fieldName + " = ?" + i;
        }

        Set<String> fuzzyFieldNames = fuzzyFieldNameValueMap.keySet();
        Iterator<String> fuzzyIterator = fuzzyFieldNames.iterator();
        int fuzzyBegin = fieldNames.size() == 0 ? 0 : fieldNames.size();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            sql = sql + " and c." + fuzzyFieldName + " like ?" + (fuzzyBegin + i);
        }
        if (compoundFieldNameValueMap != null) {
            if (compoundFieldNameValueMap.get("startPrice") != null) {
                sql = sql + " and c.marketPrice >= " + (Float) compoundFieldNameValueMap.get("startPrice");
            }
            if (compoundFieldNameValueMap.get("endPrice") != null) {
                sql = sql + " and c.marketPrice <= " + (Float) compoundFieldNameValueMap.get("endPrice");
            }
            if (compoundFieldNameValueMap.get("startSaleNumber") != null) {
                sql = sql + " and c.saleNumber >= " + (Integer) compoundFieldNameValueMap.get("startSaleNumber");
            }
            if (compoundFieldNameValueMap.get("endSaleNumber") != null) {
                sql = sql + " and c.saleNumber <= " + (Integer) compoundFieldNameValueMap.get("endSaleNumber");
            }
            if (compoundFieldNameValueMap.get("organization.accountRole") != null) {
                sql = sql + " and c.organization.accountRole <> " + compoundFieldNameValueMap.get("organization.accountRole");
            }
            if (compoundFieldNameValueMap.get("productStatus") != null) {
                if ((Integer) compoundFieldNameValueMap.get("productStatus") == -1) {
                    sql = sql + " and c.productStatus in (1,2)";
                } else {
                    sql = sql + " and c.productStatus = " + (Integer) compoundFieldNameValueMap.get("productStatus");
                }
            }
            if (compoundFieldNameValueMap.get("type") != null) {
                if (compoundFieldNameValueMap.get("type").equals("0")) {
                    sql = sql + " and c.parentProductId = 0";
                } else {
                    sql = sql + " and c.parentProductId <> 0";
                }
            }
            if (compoundFieldNameValueMap.get("oneProductApplyStatus") != null) {
                sql = sql + " and c.oneProductApplyStatus <> 0";
            }
        }

        sql = sql + " order by c.updateTime desc";
        Query query = em.createQuery(sql);

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
        List<T> resultList = query.getResultList();

        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);
        return domainPage;
    }

    @Override
    public <T extends BaseDomain> DomainPage getProductsByCompoundConditions(Class clazz, Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, Map<String, Object> compoundFieldNameValueMap, String fieldName, String sortIn, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;
        //查询满足条件的结果
        String sql = "select DISTINCT c from " + clazz.getName() + " c , Site s where s.available = 1 and  c.available = 1 and c.organizationId = s.organizationId";
        Set<String> fieldNames = fieldNameValueMap.keySet();
        Iterator<String> iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldname = iterator.next();
            if ("cityId".equals(fieldname) || "districtId".equals(fieldname)) {
                sql = sql + " and s." + fieldname + " = ?" + i;
            } else {
                sql = sql + " and c." + fieldname + " = ?" + i;
            }
        }

        Set<String> fuzzyFieldNames = fuzzyFieldNameValueMap.keySet();
        Iterator<String> fuzzyIterator = fuzzyFieldNames.iterator();
        int fuzzyBegin = fieldNames.size() == 0 ? 0 : fieldNames.size();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            sql = sql + " and c." + fuzzyFieldName + " like ?" + (fuzzyBegin + i);
        }
        if (compoundFieldNameValueMap != null) {

            if (compoundFieldNameValueMap.get("productStatus") != null) {
                if ((Integer) compoundFieldNameValueMap.get("productStatus") == -1) {
                    sql = sql + " and c.productStatus in (1,0)";
                } else {
                    sql = sql + " and c.productStatus = " + compoundFieldNameValueMap.get("productStatus");
                }
            }

            if (compoundFieldNameValueMap.get("productCategoryIds") != null) {
                if (StringUtils.isNotBlank(compoundFieldNameValueMap.get("productCategoryIds").toString())) {
                    sql = sql + " and c.productCategoryId in (:productCategoryIds)";
                }
            }
        }
        if (fieldName != null && sortIn != null) {
            sql = sql + " order by c." + fieldName + " " + sortIn;
        } else {
            sql = sql + " order by c.updateTime desc";
        }

        Query query = em.createQuery(sql);

        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldname = iterator.next();
            query.setParameter(i, fieldNameValueMap.get(fieldname));
        }

        fuzzyIterator = fuzzyFieldNames.iterator();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            query.setParameter((fuzzyBegin + i), "%" + fuzzyFieldNameValueMap.get(fuzzyFieldName) + "%");
        }

        if (compoundFieldNameValueMap.get("productCategoryIds") != null) {
            List<Long> productCategoryIds = (List<Long>) compoundFieldNameValueMap.get("productCategoryIds");
            query.setParameter("productCategoryIds", productCategoryIds);
        }

        Long totalCount = (long) query.getResultList().size();
        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List<T> resultList = query.getResultList();

        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);
        return domainPage;
    }

    /**
     * 查询商户分销的商品
     *
     * @param clazz
     * @param parentAccountId
     * @param accountId
     * @param pageIndex
     * @param pageSize
     * @param <T>
     * @return
     */
    @Override
    public <T extends BaseDomain> DomainPage getDistributionProduct(Class clazz, Long parentAccountId, Long accountId, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;

        String sql = "select b2.* from ";
        sql = sql + " (select * from biz_product where organization_id=" + parentAccountId + ") as b1,";
        sql = sql + " (select * from biz_product where organization_id=" + accountId + ") as b2 where";
        sql = sql + " b2.parent_product_id = b1.id and ";
        sql = sql + " b2.available = 1 and b1.available = 1";

        sql = sql + " order by b2.update_time desc";
        Query query = em.createNativeQuery(sql, Product.class);

        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List<Product> resultList = query.getResultList();

        //查询总记录
        sql = "select b2.* from ";
        sql = sql + " (select * from biz_product where organization_id=" + parentAccountId + ") as b1,";
        sql = sql + " (select * from biz_product where organization_id=" + accountId + ") as b2 where";
        sql = sql + " b2.parent_product_id = b1.id and ";
        sql = sql + " b2.available = 1 and b2.product_status = 2";


        sql = sql + " order by b2.update_time desc";
        query = em.createNativeQuery(sql);

        Long totalCount = (long) query.getResultList().size();

        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);
        return domainPage;
    }

    @Override
    public List<Product> getShelvesOfProducts(Map<String, Object> map) {
        Date date = new Date();
        SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = dd.format(date);
        String sql = "select c from Product c where ";
//        sql = sql + " date_format(c.expiry_time,'%y-%m-%d') > date_format(?1,'%y-%m-%d')";
        sql = sql + "  c.available = 1";
        Set<String> fieldNames = map.keySet();
        Iterator<String> iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            sql = sql + " and c." + fieldName + " = ?" + i;
        }

        sql = sql + " order by c.updateTime desc";
        Query query = em.createQuery(sql);
//        query.setParameter(1, nowDate);

        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            query.setParameter(i, map.get(fieldName));
        }

        return query.getResultList();
    }

    @Override
    public List<Product> getRelatedProducts(Long productId) {
        String sql = "select * from biz_product  where path LIKE '%/" + productId + "/%' and available = 1";
        Query query = em.createNativeQuery(sql, Product.class);
        List<Product> resultList = query.getResultList();
        return resultList;
    }

    @Override
    public List getDistributionProduct(Long parentAccountId, Long accountId) {
        String sql = "select b2.id,b1.market_price from (select * from biz_product where organization_id = " + parentAccountId + " and available = 1) b1,(select * from biz_product where organization_id = " + accountId + " and available = 1) b2";
        sql += " where b2.parent_product_id = b1.id";
        Query query = em.createNativeQuery(sql);
        List list = query.getResultList();
        return list;
    }

    @Override
    public DomainPage getProductDistributionDetail(Class clazz, Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, long pageIndex, long pageSize) {
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
    public DomainPage getVirtualProductByConditions(Map<String, Object> conditions, DomainPage domainPage) {
        Preconditions.checkNotNull(conditions);
        Preconditions.checkNotNull(domainPage);
        Long organizationId = (Long) conditions.get("organizationId");
        Integer productStatus = (Integer) conditions.get("productStatus");
        long pageIndex = domainPage.getPageIndex();
        long pageSize = domainPage.getPageSize();

        StringBuilder sql = new StringBuilder("SELECT p.id,p.product_identifier,p.product_name,p.stock_number,p.out_price");
        sql.append(" FROM biz_product p,biz_product_category pc");
        sql.append(" WHERE p.available = 1 AND p.product_category_id = pc.id");
        sql.append(" AND pc.is_create_ticket = 1"); // 虚拟商品
        if (organizationId != null) sql.append(" AND p.organization_id = " + organizationId);
        if (productStatus != null) sql.append(" AND p.product_status = " + productStatus);
        sql.append(" ORDER BY p.update_time DESC");
        sql.append(" LIMIT " + (pageIndex - 1) * pageSize + "," + pageIndex * pageSize);
        Query query = em.createNativeQuery(sql.toString());

        List resultList = query.getResultList();
        long totalCount = getVirtualProductTotalCount(organizationId, productStatus);
        DomainPage returnDomainPage = new DomainPage(pageSize, pageIndex, totalCount);
        returnDomainPage.setDomains(resultList);
        return returnDomainPage;
    }

    @Override
    public <T extends BaseDomain> void updateProductSaleNumber(Class<T> clazz, List<String> params, Integer amount) {
        StringBuffer sql = new StringBuffer();  //  update biz_product p set p.sale_number = p.sale_number + 1 where p.id in ();
        sql.append("update ");
        sql.append(clazz.getName());
        sql.append(" p set p.saleNumber = (p.saleNumber + " + amount + ") where p.id in (");
        for (int i = 0; i < params.size(); i++) {
            sql.append(" '");
            sql.append(params.get(i));
            sql.append(" ' ");
            if (i < params.size() - 1) {
                sql.append(" ,");
            }
        }
        sql.append(" )");
        Query query = em.createQuery(sql.toString());
        query.executeUpdate();
    }

    @Override
    public DomainPage<Product> getHasDistributionProductList(Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, Map<String, Object> compoundFieldNameValueMap, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;

        StringBuffer sb = new StringBuffer();
        long organizationId = Long.valueOf(fieldNameValueMap.get("organizationId").toString());
        long parentOrganizationId = Long.valueOf(fieldNameValueMap.get("parentOrganizationId").toString());
        int productStatus = Integer.valueOf(fieldNameValueMap.get("productStatus").toString());
        sb.append("SELECT * FROM biz_product WHERE organization_id = " + organizationId + " AND parent_product_id IN ");
        sb.append("(SELECT id FROM biz_product where available = 1  AND organization_id IN ( ");
        sb.append("SELECT r.parent_organization_id FROM biz_account_relation r WHERE r.organization_id = " + organizationId + " AND ");
        if (parentOrganizationId == 0) {
            sb.append(" r.audit_status = 2 and r.available = 1)) and available = 1 ");
        } else {
            sb.append(" r.parent_organization_id = " + parentOrganizationId + " and r.audit_status = 2 and r.available = 1)) and available = 1 ");
        }
        if (productStatus != -1) {
            sb.append(" and product_status = " + productStatus);
        }
        if (fuzzyFieldNameValueMap != null && fuzzyFieldNameValueMap.size() > 0) {
            String productName = fuzzyFieldNameValueMap.get("productName").toString();
            sb.append(" and product_name like '%" + productName + "%'");
        }
        if (compoundFieldNameValueMap != null) {
            if (compoundFieldNameValueMap.get("startPrice") != null) {
                sb.append(" and market_price >= " + (Float) compoundFieldNameValueMap.get("startPrice"));
            }
            if (compoundFieldNameValueMap.get("endPrice") != null) {
                sb.append(" and market_price <= " + (Float) compoundFieldNameValueMap.get("endPrice"));
            }
            if (compoundFieldNameValueMap.get("startSaleNumber") != null) {
                sb.append(" and sale_number >= " + (Integer) compoundFieldNameValueMap.get("startSaleNumber"));
            }
            if (compoundFieldNameValueMap.get("endSaleNumber") != null) {
                sb.append(" and sale_number <= " + (Integer) compoundFieldNameValueMap.get("endSaleNumber"));
            }
        }
        sb.append(" order by update_time desc");
        Query query = em.createNativeQuery(sb.toString(), Product.class);
        Long totalCount = (long) query.getResultList().size();
        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List<Product> resultList = query.getResultList();

        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);
        return domainPage;
    }

    @Transactional
    @Override
    public void updateProductStockNumber(Long id, Integer amount) {
        StringBuilder sql = new StringBuilder("UPDATE biz_product t ");
        sql.append("SET t.stock_number = t.stock_number - ?1 ");
        sql.append("WHERE t.id = ?2 ");
        sql.append("and t.available = 1 ");
        sql.append("and t.stock_number >= ?3 ");

        Query query = em.createNativeQuery(sql.toString());
        query.setParameter(1, amount)
                .setParameter(2, id)
                .setParameter(3, amount).executeUpdate();

    }

    @Override
    public Long getShelvesProductAmount() {
        StringBuffer sql = new StringBuffer(" SELECT COUNT(p.id) FROM biz_product p where p.available = 1 and p.product_status = 2 ");

        Query query = em.createNativeQuery(sql.toString());
        List result = query.getResultList();

        Long totalCount = 0L;
        if (result.get(0) != null) {
            totalCount = Long.parseLong(result.get(0).toString());
        }
        return totalCount;
    }

    private long getVirtualProductTotalCount(Long organizationId, Integer productStatus) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(p.id) from biz_product p,biz_product_category pc");
        sql.append(" WHERE p.available = 1 AND p.product_category_id = pc.id");
        sql.append(" AND pc.is_create_ticket = 1"); // 虚拟商品
        if (organizationId != null) sql.append(" AND p.organization_id = " + organizationId);
        if (productStatus != null) sql.append(" AND p.product_status = " + productStatus);

        Query query = em.createNativeQuery(sql.toString());
        List result = query.getResultList();
        long totalCount = 0l;
        if (result.size() == 1) totalCount = Long.parseLong(result.get(0).toString());
        return totalCount;
    }
}
