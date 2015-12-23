/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.poi.impl;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.core.util.DateUtil;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.poi.PresentSquareDao;
import com.pemass.persist.enumeration.StrategyTypeEnum;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * @Description: PresentSquareDaoImpl
 * @Author: zhou hang
 * @CreateTime: 2014-12-10 10:38
 */
@Repository
public class PresentSquareDaoImpl extends JPABaseDaoImpl implements PresentSquareDao {


    @Override
    public List<Object[]> selectByUserId(Long uid) {
        String date = DateUtil.gap(new Date(), "yyyy-MM-dd hh:mm:ss");
        StringBuffer sb = new StringBuffer();
        sb.append("select pis.organization_id,pp.uid from poi_present_square pps    ");
        sb.append("  LEFT JOIN poi_issue_strategy pis on pis.id=pps.issue_strategy_id     ");
        sb.append(" LEFT JOIN poi_present pp on pp.id=pps.present_id ");
        sb.append("   where pps.available=1 and pis.available=1 and  ");
        sb.append("   pps.expiry_time>'" + date + "' and pis.strategy_type='CONTENT_PRESENT_NOSHOW' ");
        sb.append("   and pp.uid =" + uid + "  and pps.is_claim=1 ORDER BY pps.expiry_time desc   ");
        Query query = em.createNativeQuery(sb.toString());

        return query.getResultList();
    }


    @Override
    public DomainPage selectPresentSquare(long pageIndex, long pageSize) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT o.organization_name,s.strategy_name,p.end_time,count(p.present_id),s.id,o.logo ")
                .append(" FROM poi_issue_strategy s ")
                .append(" LEFT JOIN poi_present_square p ON p.issue_strategy_id = s.id ")
                .append(" LEFT JOIN sys_organization o ON s.organization_id = o.id")
                .append(" WHERE 1 = 1  ")
                .append(" and p.available = 1 ")
                .append(" and s.available = 1 ")
                .append(" and o.available = 1 ")
//                .append(" and p.is_claim = ?1 ")
                .append(" and s.strategy_type = ?1")
                .append(" and p.start_time < NOW() ")
                .append(" and s.strategy_status = 'EXECUTED'")
                .append(" and p.end_time > NOW() ")
                .append(" GROUP BY o.organization_name,s.strategy_name,p.end_time,s.id,o.logo  ");
        Query query = em.createNativeQuery(sb.toString());
//        query.setParameter(1, 0);
        query.setParameter(1, StrategyTypeEnum.SEND_PRESENT_SQUARE.toString());
        List<Object[]> list = query.getResultList();
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, query.getResultList().size());
        domainPage.setDomains(list);
        return domainPage;
    }

    @Override
    public Integer getTotalByIssueStrategyId(Long issueStrategyId) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT count(s.present_id) ")
                .append(" FROM poi_present_square AS s ,poi_issue_strategy AS i ")
                .append(" WHERE s.issue_strategy_id = i.id ")
                .append(" AND s.is_claim = 0")
                .append(" AND i.start_time < NOW()  ")
                .append(" AND i.end_time > NOW()")
                .append(" and i.available = 1 ")
                .append(" and s.available = 1 ")
                .append(" and i.id = " + issueStrategyId)
                .append(" GROUP BY i.id  ");
        Query query = em.createNativeQuery(sb.toString());
        if (query.getResultList().size()>0){
            return Integer.valueOf(query.getResultList().get(0).toString());
        }else {
            return 0;
        }

    }

}