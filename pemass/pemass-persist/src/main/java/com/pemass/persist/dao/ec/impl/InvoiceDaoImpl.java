/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.ec.impl;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.ec.InvoiceDao;
import com.pemass.persist.domain.jpa.ec.Invoice;
import com.pemass.persist.enumeration.InvoiceStatusEnum;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @Description: BillDaoImpl
 * @Author: Administrator
 * @CreateTime: 2014-12-09 20:56
 */
@Repository
public class InvoiceDaoImpl extends JPABaseDaoImpl implements InvoiceDao {

    @Override
    public DomainPage<Invoice> getInvoiceList(Class clazz,Long organizationId,Map<String,Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;
        //查询满足条件的结果
        String sql = "select i.id id,p.id pId,o.id oId from";
        sql += " ec_invoice i,biz_product p,ec_order o";
        sql += " where i.product_id = p.id and i.order_id = o.id and p.organization_id = "+ organizationId;

        Set<String> fieldNames = fieldNameValueMap.keySet();
        if(fieldNameValueMap != null && fieldNameValueMap.size() > 0){
            if(fieldNameValueMap.get("invoice_status") != null){
                if(fieldNameValueMap.get("invoice_status").equals(InvoiceStatusEnum.HAS_SEND)){
                    sql = sql + " and i.invoice_status = 'HAS_SEND'";
                }else{
                    sql = sql + " and i.invoice_status = 'NONE_SEND'";
                }
            }
        }

        int fuzzyBegin = fieldNames.size() == 0 ? 0 : fieldNames.size();

        if(fuzzyFieldNameValueMap.get("product_name") != null){
            sql = sql + " and p.product_name like ?" + (fuzzyBegin + 1);
        }else if(fuzzyFieldNameValueMap.get("order_identifier") != null){
            sql = sql + " and o.order_identifier like ?" + (fuzzyBegin + 2);
        }

        sql += " and i.available = 1";
        sql += " and p.available = 1";
        sql += " and o.available = 1";
        sql = sql + " order by i.update_time desc";
        Query query = em.createNativeQuery(sql);


        if(fuzzyFieldNameValueMap.get("product_name") != null){
            query.setParameter((fuzzyBegin + 1), "%" + fuzzyFieldNameValueMap.get("product_name") + "%");
        }else if(fuzzyFieldNameValueMap.get("order_identifier") != null){
            query.setParameter((fuzzyBegin + 2), "%" + fuzzyFieldNameValueMap.get("order_identifier") + "%");
        }

        Long totalCount = (long) query.getResultList().size();

        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List<Invoice> resultList = query.getResultList();


        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);
        return domainPage;
    }
}