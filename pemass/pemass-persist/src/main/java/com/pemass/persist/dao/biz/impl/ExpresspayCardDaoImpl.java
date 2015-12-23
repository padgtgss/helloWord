/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.biz.impl;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.biz.ExpresspayCardDao;
import com.pemass.persist.domain.jpa.biz.ExpresspayCard;
import com.pemass.persist.domain.jpa.sys.TerminalUser;
import com.pemass.persist.enumeration.LogisticsStatusEnum;
import com.pemass.persist.enumeration.OrderItemStatusEnum;
import com.pemass.persist.enumeration.PutawayStyleEnum;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * @Description: ExpresspayCardDaoImpl
 * @Author: zhou hang
 * @CreateTime: 2015-04-20 17:05
 */
@Repository
public class ExpresspayCardDaoImpl extends JPABaseDaoImpl implements ExpresspayCardDao {
    @Override
    public ExpresspayCard getEntityByid(String cardNumber, Long organizationId) {
        StringBuffer sb = new StringBuffer();
        sb.append(" from ExpresspayCard e where e.available=1 and e.cardNumber=?1");
        sb.append(" and e.organizationId=?2  and e.useStatus=?3 and e.shippingStatus=?4 ");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, cardNumber);
        query.setParameter(2, organizationId);
        query.setParameter(3, OrderItemStatusEnum.UNUSED);
        query.setParameter(4, LogisticsStatusEnum.PUTAWAY);
        List<ExpresspayCard> list = query.getResultList();
        if (list.size() <= 0) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public int updateAllots(TerminalUser terminalUserId, Long organizationId, String beginNumber, String endNumber, String cardIdntifier) {
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE biz_expresspay_card b SET b.shipping_status=?1  ");
        sb.append(" ,b.card_Identifier=?2,b.organization_id=?3,b.source_id=?4,b.update_time=?5,b.putaway_style=?6 ");
        sb.append(" where  b.card_number BETWEEN ?7 AND ?8  and b.organization_id=?9 and b.use_status=?10 and b.shipping_status=?11 ");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, LogisticsStatusEnum.ALLOT.toString());
        query.setParameter(2, cardIdntifier);
        query.setParameter(3, organizationId);
        query.setParameter(4, terminalUserId.getOrganizationId());
        query.setParameter(5, new Date());
        query.setParameter(6, PutawayStyleEnum.BATCH_QUANTITY.toString());
        query.setParameter(7, Long.valueOf(beginNumber));
        query.setParameter(8, Long.valueOf(endNumber));
        query.setParameter(9, terminalUserId.getOrganizationId());
        query.setParameter(10, OrderItemStatusEnum.UNUSED.toString());
        query.setParameter(11, LogisticsStatusEnum.PUTAWAY.toString());
        return query.executeUpdate();
    }

    @Override
    public Long onSaleCount(Long organizationId, OrderItemStatusEnum statusEnum, LogisticsStatusEnum logisticsStatusEnum) {
        StringBuffer sb = new StringBuffer();
        sb.append("select count(e) from ExpresspayCard e where e.available=1  ");
        sb.append(" and e.organizationId=?1  and e.useStatus=?2 and e.shippingStatus=?3 ");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, organizationId);
        query.setParameter(2, statusEnum);
        query.setParameter(3, logisticsStatusEnum);
        List result = query.getResultList();
        if (result != null) {
            return (Long) result.get(0);
        }
        return null;
    }

    @Override
    public List<Object[]> getBycardIdentifier(String identifier) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT b.card_number,b.card_category from biz_expresspay_card b ");
        sb.append("where b.card_Identifier=?1 ORDER BY b.card_number asc ");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, identifier);
        return query.getResultList();
    }

    @Override
    public boolean updatePutaways(Long organizationId, String cardIdntifier) {
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE ExpresspayCard b SET b.shippingStatus=?1,b.updateTime=?2  ");
        sb.append(" where b.cardIdentifier=?3 and b.organizationId=?4 ");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, LogisticsStatusEnum.PUTAWAY);
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
    public DomainPage getBySoldSearch(Long organizationId, String cardNumber, long pageIndex, long pageSize) {
        StringBuffer sb = new StringBuffer("select e.expresspay_card_id,e.total_price,e.username,e.telephone,e.invoice_id ");
        sb.append(" from biz_expresspay_card_record e left join biz_expresspay_card ec on ec.id=e.expresspay_card_id  ");
        sb.append(" where e.available=1 and ec.available=1 and ec.organization_id=?1 and ec.use_status=?2  ");
        if (cardNumber != null && !"".equals(cardNumber)) {
            sb.append(" and ec.card_number=?3");
        }
        Query query = em.createNativeQuery(sb.toString());

        query.setParameter(1, organizationId);
        query.setParameter(2, OrderItemStatusEnum.USED.toString());
        if (cardNumber != null && !"".equals(cardNumber)) {
            query.setParameter(3, cardNumber);
        }
        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List<Object[]> list = query.getResultList();
        long totalCount = getBySoldSearchCount(organizationId, cardNumber).longValue();
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.setDomains(list);
        return domainPage;
    }

    private BigInteger getBySoldSearchCount(Long organizationId, String cardNumber) {
        StringBuffer sb = new StringBuffer("select count(e.id) from biz_expresspay_card_record e left join biz_expresspay_card ec on ec.id=e.expresspay_card_id ");
        sb.append(" where e.available=1 and ec.available=1 and ec.organization_id=?1 and ec.use_status=?2  ");
        if (cardNumber != null && !"".equals(cardNumber)) {
            sb.append(" and ec.card_number=?3");
        }
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, organizationId);
        query.setParameter(2, OrderItemStatusEnum.USED.toString());
        if (cardNumber != null && !"".equals(cardNumber)) {
            query.setParameter(3, cardNumber);
        }
        return (BigInteger) query.getResultList().get(0);
    }


    private BigInteger selectCount(Long organizationId) {
        StringBuffer sb = new StringBuffer();
        sb.append("select count(*)  from (SELECT count(b.id) ");
        sb.append("   from biz_expresspay_card b  ");
        sb.append("  where b.organization_id=?1 and b.use_status=?2 and b.shipping_status=?3 ");
        sb.append(" GROUP BY b.card_Identifier) as temp   ");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, organizationId);
        query.setParameter(2, 1);
        query.setParameter(3, 1);
        return (BigInteger) query.getResultList().get(0);
    }

}