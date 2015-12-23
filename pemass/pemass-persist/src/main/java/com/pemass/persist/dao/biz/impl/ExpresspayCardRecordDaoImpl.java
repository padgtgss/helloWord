/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.biz.impl;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.biz.ExpresspayCardRecordDao;
import com.pemass.persist.domain.jpa.biz.ExpresspayCard;
import com.pemass.persist.domain.jpa.biz.ExpresspayCardRecord;
import com.pemass.persist.enumeration.LogisticsStatusEnum;
import com.pemass.persist.enumeration.OrderItemStatusEnum;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.*;

/**
 * @Description: ExpresspayCardDaoImpl
 * @Author: zhou hang
 * @CreateTime: 2015-04-18 16:59
 */
@Repository
public class ExpresspayCardRecordDaoImpl extends JPABaseDaoImpl implements ExpresspayCardRecordDao {

    @Override
    public ExpresspayCard findEntity(String cardNumber, Long organizationId, OrderItemStatusEnum statusEnum) {

        StringBuffer sb = new StringBuffer();
        sb.append("from ExpresspayCard e where e.available=1 and e.organizationId=?1 ");
        sb.append(" and e.cardNumber=?2 and e.useStatus=?3 and e.shippingStatus=?4 ");

        Query query = em.createQuery(sb.toString());
        query.setParameter(1, organizationId);
        query.setParameter(2, cardNumber);
        query.setParameter(3, statusEnum);
        query.setParameter(4, LogisticsStatusEnum.PUTAWAY);
        List<ExpresspayCard> list = query.getResultList();
        if (list == null || list.size() <= 0) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public DomainPage findByCondition(Map<String, Object> map, Map<String, Object> conMap,
                                      long pageIndex, long pageSize) {
        StringBuffer sb = new StringBuffer("select r.id,e.card_number,o.organization_name,t.terminal_username,r.username,r.telephone,r.total_price, ");
        sb.append(" r.invoice_id,i.receive_address,r.legal_idcard,r.create_time,e.card_category,e.cost_price ");
        sb.append(" from biz_expresspay_card_record r ");
        sb.append(" left join biz_expresspay_card e on e.id=r.expresspay_card_id ");
        sb.append(" left join sys_organization o on o.id=e.organization_id ");
        sb.append(" left join sys_terminal_user t on t.id=r.cashier_user_id ");
        sb.append(" left join ec_invoice i on i.id=r.invoice_id ");
        sb.append(" where r.available = 1 and e.available=1 and o.available=1 and t.available=1 and i.available=1 ");
        Set<String> fieldNames = map.keySet();
        Iterator<String> iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            sb.append(" and " + fieldName + " ?" + i);
        }

        Set<String> conNames = conMap.keySet();
        for (String key : conNames) {
            sb.append(" and " + key + " " + conMap.get(key));
        }
        sb.append(" order by r.update_time desc");
        Query query = em.createNativeQuery(sb.toString());
        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            query.setParameter(i, map.get(fieldName));
        }
        int totalCount = query.getResultList().size();

        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List<Object[]> resultList = query.getResultList();

        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);
        return domainPage;
    }
}