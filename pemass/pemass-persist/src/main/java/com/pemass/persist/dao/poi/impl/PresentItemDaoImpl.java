package com.pemass.persist.dao.poi.impl;

import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.common.server.enumeration.AvailableEnum;
import com.pemass.persist.dao.poi.PresentItemDao;
import com.pemass.persist.enumeration.PresentItemTypeEnum;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

/**
 * @Description: PointPoolDaoImpl
 * @Author: cassiel.liu
 * @CreateTime: 2015-09-19 16:27
 */
@Repository(value = "presentItemDao")
public class PresentItemDaoImpl extends JPABaseDaoImpl implements PresentItemDao {


    @Override
    public Integer getPresentItemByPresentPackId(Long presentPackId) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(c.id) FROM poi_present_item c ");
        sql.append(" LEFT JOIN poi_present p ON p.id = c.present_id ");
        sql.append(" LEFT JOIN poi_present_pack k ON k.id = p.present_pack_id ");
        sql.append(" WHERE c.available = " + AvailableEnum.AVAILABLE.ordinal());
        sql.append(" and p.available = " + AvailableEnum.AVAILABLE.ordinal());
        sql.append(" and k.available = " + AvailableEnum.AVAILABLE.ordinal());
        sql.append(" and k.id = " + presentPackId);

        Query query = em.createNativeQuery(sql.toString());
        List result = query.getResultList();

        Integer totalCount = 0;
        if (result.get(0) != null) {
            totalCount = Integer.parseInt(result.get(0).toString());
        }

        return totalCount;
    }

    @Override
    public Integer getTotalPointByPresentPackId(Long presentPackId, PresentItemTypeEnum presentItemType) {
        StringBuilder sql = new StringBuilder("SELECT SUM(c.amount) FROM poi_present_item c ");
        sql.append(" LEFT JOIN poi_present p ON p.id = c.present_id ");
        sql.append(" LEFT JOIN poi_present_pack k ON k.id = p.present_pack_id ");
        sql.append(" WHERE c.available = " + AvailableEnum.AVAILABLE.ordinal());
        sql.append(" and p.available = " + AvailableEnum.AVAILABLE.ordinal());
        sql.append(" and k.available = " + AvailableEnum.AVAILABLE.ordinal());
        sql.append(" and k.id = " + presentPackId);
        sql.append(" and c.present_item_type = '" + presentItemType.toString() + "'");

        Query query = em.createNativeQuery(sql.toString());
        List result = query.getResultList();

        Integer totalCount = 0;
        if (result.get(0) != null) {
            totalCount = Integer.parseInt(result.get(0).toString());
        }

        return totalCount;
    }
}
