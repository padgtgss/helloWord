/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.dao.poi.impl;

import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.common.server.enumeration.AvailableEnum;
import com.pemass.persist.dao.poi.OrganizationConsumeDetailDao;
import com.pemass.persist.enumeration.ConsumeTypeEnum;
import com.pemass.persist.enumeration.PresentStatusEnum;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: OrganizationConsumeDetailDaoImpl
 * @Author: estn.zuo
 * @CreateTime: 2015-08-04 20:05
 */
@Repository
public class OrganizationConsumeDetailDaoImpl extends JPABaseDaoImpl implements OrganizationConsumeDetailDao {

    @Override
    public boolean updateAmount(ConsumeTypeEnum consumeType, Long consumeTargetId) {
        String updateSql = "UPDATE OrganizationConsumeDetail t SET t.amount = t.payableAmount WHERE t.consumeType = ?1 AND t.consumeTargetId = ?2";
        Query query = em.createQuery(updateSql);
        query.setParameter(1, consumeType);
        query.setParameter(2, consumeTargetId);
        return query.executeUpdate() != 0;
    }

    @Override
    public boolean updateByPresentPackId(Integer auditStatus, ConsumeTypeEnum consumeType, Long presentPackId) {
        String updateSql = "UPDATE OrganizationConsumeDetail t ";
        //红包审核失败
        if (0 == auditStatus) {
            updateSql = updateSql + " SET t.available = 0 ";
        }
        //红包审核成功
        if (1 == auditStatus) {
            updateSql = updateSql + " SET t.amount = t.payableAmount ";
        }
        updateSql = updateSql + " WHERE t.consumeType = ?1 and t.consumeTargetId IN (SELECT p.id FROM Present p WHERE p.presentPackId = ?2)";
        Query query = em.createQuery(updateSql);
        query.setParameter(1, consumeType);
        query.setParameter(2, presentPackId);
        return query.executeUpdate() != 0;
    }

    @Override
    public Integer getPointUnsuccessfulConsumeAmount(Long pointPoolId) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT SUM(c.amount) FROM poi_organization_consume_detail c ")
                .append("LEFT JOIN poi_present p ON c.consume_target_id = p.id ")
                .append("LEFT JOIN poi_point_purchase pp ON c.point_purchase_id = pp.id ")
                .append("WHERE c.available = 1 AND p.available = 1 AND pp.available = 1 ")
                .append("AND c.consume_type = ?1 AND p.present_status <> ?2 ")
                .append("AND pp.point_pool_id = ?3 ");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, ConsumeTypeEnum.PRESENT.toString());
        query.setParameter(2, PresentStatusEnum.HAS_RECEIVE.ordinal());
        query.setParameter(3, pointPoolId);
        Integer amount = 0;
        List result = query.getResultList();
        if (result.get(0) != null) {
            BigDecimal bigDecimal = (BigDecimal) result.get(0);
            amount = bigDecimal.intValue();
        }
        return amount;
    }

    @Override
    public Integer getCountConsumeAmountByPresentPackId(Long presentPackId, ConsumeTypeEnum consumeType) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(c.id) FROM poi_organization_consume_detail c ")
                .append(" LEFT JOIN poi_present p ON p.id = c.consume_target_id ")
                .append(" LEFT JOIN poi_present_pack k ON k.id = p.present_pack_id ")
                .append(" WHERE c.available = " + AvailableEnum.AVAILABLE.ordinal())
                .append(" AND p.available = " + AvailableEnum.AVAILABLE.ordinal())
                .append(" AND k.available = " + AvailableEnum.AVAILABLE.ordinal())
                .append(" AND k.id = " + presentPackId)
                .append(" AND c.consume_type = '" + consumeType.toString() + "'");

        Query query = em.createNativeQuery(sql.toString());
        List result = query.getResultList();

        Integer amount = 0;
        if (result.get(0) != null) {
            amount = Integer.parseInt(result.get(0).toString());
        }

        return amount;
    }
}
