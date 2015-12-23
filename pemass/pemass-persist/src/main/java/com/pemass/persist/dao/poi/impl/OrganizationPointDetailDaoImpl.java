package com.pemass.persist.dao.poi.impl;/**
 * Created by Administrator on 2015/7/21.
 */

import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.poi.OrganizationPointDetailDao;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: OrganizationPointDetailDaoImpl
 * @Author: cassiel.liu
 * @CreateTime: 2015-07-21 15:37
 */
@Repository(value = "organizationPointDetailDao")
public class OrganizationPointDetailDaoImpl extends JPABaseDaoImpl implements OrganizationPointDetailDao {
    @Override
    public Long getSumUseableAmount(Map<String, Object> fieldNameValueMap) {

        String sql = " select sum(c.useableAmount) from OrganizationPointDetail c where c.available = 1 and c.expiryTime >= ?1";
        Set<String> fieldNames = fieldNameValueMap.keySet();
        Iterator<String> iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String name = iterator.next();
            sql = sql + " and  c." + name + " = ?" + (i + 1);
        }
        sql = sql + " order by c.updateTime desc";
        Query query = em.createQuery(sql);
        query.setParameter(1, new Date());

        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String name = iterator.next();
            query.setParameter((i + 1), fieldNameValueMap.get(name));
        }
        List result = query.getResultList();
        Long amount = 0L;
        if (result.get(0) != null) {
            amount = (Long) result.get(0);
        }
        return amount;
    }

    @Override
    public Integer getAmountByPointPoolId(Long pointPoolId) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT SUM(c.useable_amount) FROM poi_organization_point_detail c ")
                .append("LEFT JOIN poi_point_purchase p ON c.point_purchase_id = p.id ")
                .append("where c.available = 1 and p.available = 1 and p.point_pool_id = ?1 ");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, pointPoolId);
        Integer amount = 0;
        List result = query.getResultList();
        if (result.get(0) != null) {
            BigDecimal bigDecimal = (BigDecimal) result.get(0);
            amount = bigDecimal.intValue();
        }
        return amount;
    }
}