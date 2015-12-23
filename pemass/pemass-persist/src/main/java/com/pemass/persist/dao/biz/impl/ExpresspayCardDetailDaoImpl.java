/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.biz.impl;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.biz.ExpresspayCardDetailDao;
import com.pemass.persist.domain.jpa.biz.ExpresspayCardDetail;
import com.pemass.persist.enumeration.PutawayStatusEnum;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigInteger;
import java.util.*;

/**
 * @Description: ExpresspayCardDetailDaoImpl
 * @Author: zhou hang
 * @CreateTime: 2015-04-23 11:09
 */
@Repository
public class ExpresspayCardDetailDaoImpl extends JPABaseDaoImpl implements ExpresspayCardDetailDao {

    @Override
    public DomainPage allotSearch(Long organizationId, PutawayStatusEnum statusEnum, String condition, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT count(b.id),b.update_time,sy.organization_name as sy,");
        sb.append(" b.card_Identifier,so.organization_name as so,b.putaway_style");
        sb.append(" from biz_expresspay_card_detail b ");
        sb.append(" LEFT JOIN sys_organization sy on sy.id=b.source_id  ");
        sb.append(" LEFT JOIN sys_organization so on so.id=b.organization_id  ");
        sb.append("  where b." + condition + "=?1 and b.putaway_status=?2  GROUP BY b.card_Identifier ORDER BY b.update_time desc ");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, organizationId);
        query.setParameter(2, statusEnum.toString());
        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List<Object[]> objects = query.getResultList();
        long totalCount = selectCount(organizationId, condition, statusEnum).longValue();
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.setDomains(objects);
        return domainPage;
    }

    @Override
    public List<Object[]> getBycardIdentifier(String identifier) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT b.card_number,b.card_category,b.putaway_style from biz_expresspay_card_detail b ");
        sb.append("where b.card_Identifier=?1 ORDER BY b.card_number asc ");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, identifier);
        return query.getResultList();
    }

    @Override
    public Long allotCount(Long organizationId, String company) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT count(*) from (SELECT count(b.id) FROM biz_expresspay_card_detail b WHERE");
        sb.append(" b." + company + "=?1 and b.putaway_status=?2 GROUP BY ");
        sb.append(" b.card_Identifier) as temp");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, organizationId);
        query.setParameter(2, PutawayStatusEnum.INTRANSIT.toString());
        BigInteger count = (BigInteger) query.getResultList().get(0);
        return count.longValue();
    }

    @Override
    public boolean updatePutaways(Long organizationId, String cardIdntifier) {
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE ExpresspayCardDetail b SET b.putawayStatusEnum=?1,b.updateTime=?2  ");
        sb.append(" where b.cardIdentifier=?3 and b.organizationId=?4 ");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, PutawayStatusEnum.PUTAWAY);
        query.setParameter(2, new Date());
        query.setParameter(3, cardIdntifier);
        query.setParameter(4, organizationId);
        int num = query.executeUpdate();
        if (num > 0) {
            return true;
        }
        return false;
    }

    @Override
    public DomainPage findByPutawayList(Map<String, Object> map, String conditiond, long pageIndex, long pageSize) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT b.update_time,"+conditiond+".organization_name,t.terminal_username,count(b.id),b.putaway_status,b.card_Identifier");
        sql.append("  from biz_expresspay_card_detail b      ");
        sql.append("  LEFT JOIN sys_organization s on s.id=b.source_id  ");
        sql.append("  LEFT JOIN sys_organization o on o.id=b.organization_id  ");
        sql.append("  LEFT JOIN sys_terminal_user t  on t.id=b.cashier_user_id  ");
        sql.append("    where b.available=1   ");
        Set<String> fieldNames = map.keySet();
        Iterator<String> iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            sql.append(" and " + fieldName + " ?" + i);
        }
        sql.append(" GROUP BY b.card_Identifier ORDER BY b.update_time desc");

        Query query = em.createNativeQuery(sql.toString());
        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            query.setParameter(i, map.get(fieldName));
        }


        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List<Object[]> resultList = query.getResultList();
        //计算总条数
        StringBuffer countSql = new StringBuffer();
        countSql.append("select count(*) from(SELECT b.card_Identifier ");
        countSql.append("  from biz_expresspay_card_detail b     ");
        countSql.append(" LEFT JOIN sys_organization s on s.id=b.source_id  ");
        countSql.append(" LEFT JOIN sys_organization o on o.id=b.organization_id   ");
        countSql.append("  where b.available=1    ");
        fieldNames = map.keySet();
        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            countSql.append(" and " + fieldName + "?" + i);
        }

        countSql.append(" GROUP BY b.card_Identifier ORDER BY b.update_time desc) as temp ");

        query = em.createNativeQuery(countSql.toString());
        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            query.setParameter(i, map.get(fieldName));
        }

        BigInteger totalCount = (BigInteger) query.getResultList().get(0);

        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount.longValue());
        domainPage.getDomains().addAll(resultList);
        return domainPage;
    }

    private BigInteger selectCount(Long organizationId, String condition, PutawayStatusEnum statusEnum) {

        StringBuffer sb = new StringBuffer();
        sb.append("select count(*)  from (SELECT count(b.id) ");
        sb.append(" from biz_expresspay_card_detail b ");
        sb.append("  where  b." + condition + "=?1 and b.putaway_status=?2  GROUP BY b.card_Identifier  ) as temp   ");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, organizationId);
        query.setParameter(2, statusEnum.toString());
        return (BigInteger) query.getResultList().get(0);
    }

}