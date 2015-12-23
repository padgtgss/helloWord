/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.ec.impl;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.ec.OrderTicketDao;
import com.pemass.persist.domain.jpa.ec.Order;
import com.pemass.persist.domain.jpa.ec.OrderTicket;
import com.pemass.persist.enumeration.OrderItemStatusEnum;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

/**
 * @Description: OrderTicketDaoImpl
 * @Author: pokl.huang
 * @CreateTime: 2015-06-10 14:18
 */
@Repository
public class OrderTicketDaoImpl extends JPABaseDaoImpl implements OrderTicketDao {

    @Override
    public DomainPage selectTickets(Long uid, List<String> orderItemStatusEnums, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;
        StringBuffer sql = new StringBuffer();
        sql.append("select  * from ec_order_ticket oi ");
        sql.append(" where oi.available = 1 ");
        sql.append(" and oi.user_id = ?1 ");
        sql.append(" and oi.use_status in (:orderItemStatusEnums) ");
        if (!orderItemStatusEnums.contains(OrderItemStatusEnum.USED)){
            sql.append(" and oi.expiry_time >= NOW() ");
        }
        sql.append(" order by oi.update_time desc");
        Query query = em.createNativeQuery(sql.toString(), OrderTicket.class);
        query.setParameter(1, uid);
        query.setParameter("orderItemStatusEnums", orderItemStatusEnums);


        Long totalCount = (long) query.getResultList().size(); //获取分页前的总条数

        query = em.createNativeQuery(sql.toString(), OrderTicket.class);
        query.setParameter(1, uid);
        query.setParameter("orderItemStatusEnums", orderItemStatusEnums);

        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List result = query.getResultList(); //获取分页后的数据

        DomainPage<Order> domainPage = new DomainPage<Order>(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(result);
        return domainPage;
    }
}

