/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.poi.impl;

import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.poi.LotteryDrawDao;
import com.pemass.persist.domain.jpa.poi.LotteryDraw;
import com.pemass.persist.domain.jpa.poi.PointPurchase;
import com.pemass.persist.enumeration.AuditStatusEnum;
import com.pemass.persist.enumeration.PointTypeEnum;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Description: LotteryDrawDaoImpl
 * @Author: zhou hang
 * @CreateTime: 2015-01-27 11:28
 */
@Repository
public class LotteryDrawDaoImpl extends JPABaseDaoImpl implements LotteryDrawDao {

    @Override
    public List<LotteryDraw> getEntityById(Long uid) {
        Calendar calendar = Calendar.getInstance();
        Date beginTime = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date endTime = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String begin = formatter.format(beginTime);
        String end = formatter.format(endTime);
        StringBuffer sb = new StringBuffer();
        sb.append("from LotteryDraw l  where l.userId=?1 and l.useTime>=?2 and l.useTime<?3 ");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, uid);
        try {
            query.setParameter(2, formatter.parse(begin));
            query.setParameter(3, formatter.parse(end));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return query.getResultList();
    }

    @Override
    public List<PointPurchase> getBySystemUser(PointTypeEnum typeEnum, Long organizationId) {

        StringBuffer sb = new StringBuffer();
        sb.append("from PointPurchase p  where");
        sb.append("  p.organizationId =?1 ");
        sb.append(" and p.pointType =?2 ");
        sb.append(" and p.expiryTime > ?3 ");
        sb.append(" and p.auditStatus = ?4 ");
        sb.append(" order by p.expiryTime asc");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, organizationId);
        query.setParameter(2, typeEnum);
        query.setParameter(3, new Date());
        query.setParameter(4, AuditStatusEnum.HAS_AUDIT);

        return query.getResultList();
    }

}