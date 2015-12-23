/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.poi.impl;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.poi.PresentRecordDao;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

/**
 * @Description: PresentRecordDaoImpl
 * @Author: zhou hang
 * @CreateTime: 2015-07-01 10:32
 */
@Repository(value = "presentRecordDao")
public class PresentRecordDaoImpl extends JPABaseDaoImpl implements PresentRecordDao {
    @Override
    public DomainPage selectByFromUserId(Long uid, long pageIndex, long pageSize) {
        StringBuffer sb = new StringBuffer("select p.id,pr.giving_time,p.present_name,su.username from poi_present_record pr left join poi_present p on p.id=pr.present_id ");
        sb.append(" left join sys_user su on su.id=pr.to_user_id where pr.available=1 and p.available=1 and su.available=1 ");
        sb.append(" and pr.from_user_id=?1 order by pr.giving_time Desc");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, uid);
        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List<Object[]> list = query.getResultList();
        long totalCount = getByIdListCount(uid).longValue();
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.setDomains(list);
        return domainPage;
    }

    private BigInteger getByIdListCount(Long uid) {
        StringBuffer sb = new StringBuffer("select count(pr.id) from poi_present_record pr left join poi_present p on p.id=pr.present_id ");
        sb.append(" left join sys_user su on su.id=pr.to_user_id where pr.available=1 and p.available=1 and su.available=1 ");
        sb.append(" and pr.from_user_id=?1");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, uid);
        return (BigInteger) query.getResultList().get(0);
    }
}