/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.dao.poi.impl;

import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.poi.UserConsumeDetailDao;
import com.pemass.persist.enumeration.ConsumeTypeEnum;
import com.pemass.persist.enumeration.PointTypeEnum;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: 用户积分消耗明细
 * @Author: estn.zuo
 * @CreateTime: 2015-07-23 20:40
 */
@Repository
public class UserConsumeDetailDaoImpl extends JPABaseDaoImpl implements UserConsumeDetailDao {

    @Override
    public boolean updateAmount(ConsumeTypeEnum consumeType, Long consumeTargetId) {
        String updateSql = "UPDATE UserConsumeDetail t SET t.amount = t.payableAmount WHERE t.consumeType = ?1 AND t.consumeTargetId = ?2";
        Query query = em.createQuery(updateSql);
        query.setParameter(1, consumeType);
        query.setParameter(2, consumeTargetId);
        return query.executeUpdate() != 0;
    }

    @Override
    public Integer getPointConsumeAmountByPointPoolId(Long pointPoolId) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT sum(c.amount) FROM poi_user_consume_detail c ")
                .append("LEFT JOIN poi_user_point_detail p ON c.user_point_detail_id = p.id ")
                .append("LEFT JOIN poi_point_purchase pp ON p.point_purchase_id = pp.id ")
                .append("WHERE c.available = 1 AND c.consume_type =?1 AND pp.point_pool_id = ?2");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, ConsumeTypeEnum.ORDER.toString());
        query.setParameter(2, pointPoolId);
        Integer amount = 0;
        List result = query.getResultList();
        if (result.get(0) != null) {
            BigDecimal bigDecimal = (BigDecimal) query.getResultList().get(0);
            amount = bigDecimal.intValue();
        }
        return amount;
    }

    @Override
    public Integer getCongelaTionAmount(Long uid, PointTypeEnum pointType, Boolean isGeneral) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT COALESCE(sum(c.payable_amount),0) from poi_user_consume_detail c  ")
                .append(" LEFT JOIN poi_user_point_detail t ON(c.user_point_detail_id = t.id)")
                .append(" where c.available = 1 and c.amount <=0 and c.payable_amount >0 ")
                .append(" and t.point_type =?1 and t.user_id = ?2  ");
        if(isGeneral){
            sb.append(" and t.area ='00:00:00:00:00'");
        }else
            sb.append(" and t.area <>'00:00:00:00:00'");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1,pointType.toString());
        query.setParameter(2,uid);
        return Integer.valueOf(query.getResultList().get(0).toString());
    }
}
