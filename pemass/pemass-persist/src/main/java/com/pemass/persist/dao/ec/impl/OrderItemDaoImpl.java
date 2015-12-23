/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.ec.impl;

import com.google.common.collect.Lists;
import com.pemass.common.core.constant.SystemConst;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.ec.OrderItemDao;
import com.pemass.persist.domain.jpa.biz.Product;
import com.pemass.persist.domain.jpa.ec.OrderItem;
import com.pemass.persist.domain.jpa.ec.OrderTicket;
import com.pemass.persist.domain.vo.OrderVO;
import com.pemass.persist.enumeration.OrderItemStatusEnum;
import com.pemass.persist.enumeration.OrderStatusEnum;
import com.pemass.persist.enumeration.OrderTypeEnum;
import com.pemass.persist.enumeration.PayStatusEnum;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.*;

/**
 * @Description: OrderItemDaoImpl
 * @Author: estn.zuo
 * @CreateTime: 2014-12-16 16:01
 */
@Repository
public class OrderItemDaoImpl extends JPABaseDaoImpl implements OrderItemDao {
    @Override
    public List<OrderItem> selectOrderItemWithFieldsByOrderId(Long orderId) {
        String sql = "SELECT p.product_name,p.preview_image,p.id FROM ec_order_item t,biz_snapshot p " +
                "where t.product_id = p.id " +
                "and t.order_id = ?1 " +
                "and p.available = 1 " +
                "and t.available = 1 ";
        Query query = em.createNativeQuery(sql);

        query.setParameter(1, orderId);
        List result = query.getResultList();

        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        for (int i = 0; i < result.size(); i++) {
            Object[] objects = (Object[]) result.get(i);
            OrderItem orderItem = new OrderItem();
            Product product = new Product();
            product.setProductName((String) objects[0]);
            product.setPreviewImage((String) objects[1]);
//            orderItem.setProductId(product);
            orderItem.setProductSnapshotId(Long.valueOf(objects[2].toString()));
            orderItems.add(orderItem);
        }

        return orderItems;
    }

    @Override
    public List<OrderTicket> selectOrderItemByOrderId(Long orderId, List<OrderItemStatusEnum> itemStatus) {
        String sql = "SELECT  ot.id,ot.ticket_code,p.product_name,ot.expiry_time,ot.use_status,ot.is_shared " +
                "FROM ec_order o,ec_order_ticket ot,biz_product p " +
                "WHERE o.id = ot.order_id " +
                "and ot.product_id = p.id " +
                "and o.id = ?1 " +
                "and ot.use_status in ?2 " +
                "and o.available = 1";

        Query query = em.createNativeQuery(sql);
        query.setParameter(1, orderId);
        query.setParameter(2, itemStatus);

        List result = query.getResultList();
        List<OrderTicket> orderTicketList = new ArrayList<OrderTicket>();
        for (int i = 0; i < result.size(); i++) {
            Object[] objects = (Object[]) result.get(i);
            OrderTicket orderTicket = new OrderTicket();
            orderTicket.setId(Long.parseLong(objects[0].toString()));
            orderTicket.setTicketCode(objects[1].toString());
            Product product = new Product();
            product.setProductName(objects[2].toString());
            orderTicket.setExpiryTime((java.util.Date) objects[3]);
            orderTicket.setUseStatus(OrderItemStatusEnum.valueOf(objects[4].toString()));
//            orderItem.setProduct(product);
            if (objects[5] != null) {
                try {
                    orderTicket.setIsShared(Integer.parseInt(objects[5].toString()));
                } catch (Exception e) {
                }
            }

            orderTicketList.add(orderTicket);
        }

        return orderTicketList;
    }

    @Override
    public Long selectWillOverdueTicket(Long uid) {
        String sql = "select count(oi) from  ec_order_ticket oi , ec_order o " +
                "where oi.order_id = o.id " +
                "and o.user_id = ?1 " +
                "and oi.use_status =  'UNUSED' " +
                "and oi.expiry_time between  ?2 and ?3";

        Query query = em.createQuery(sql);
        query.setParameter(1, uid);
        query.setParameter(2, new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        query.setParameter(3, calendar.getTime());

        return (Long) query.getSingleResult();
    }

    @Override
    public List selectThisMonthSalesVolume(Long organizationId) {
        //获取本月第一天
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        //获取当天
        Date nowDate = new Date();

        String sql = "select o.payTime as date,sum(oi.amount) as amount from OrderItem oi,Order o,Product p  " +
                " where oi.orderId = o.id " +
                " and oi.productId = p.id " +
                " and oi.available = 1 " +
                " and o.available= 1 " +
                " and p.available = 1 " +
                " and o.orderStatus = ?1 " +
                " and o.payStatus = ?2 " +
                " and p.organizationId = ?3 " +
                " and o.payTime between ?4 and ?5 " +
                " group by o.payTime";

        Query query = em.createQuery(sql);
        query.setParameter(1, OrderStatusEnum.CONFIRMED);
        query.setParameter(2, PayStatusEnum.HAS_PAY);
        query.setParameter(3, organizationId);
        query.setParameter(4, cal.getTime());
        query.setParameter(5, nowDate);
        List result = query.getResultList();
        return result;
    }

    @Override
    public Long selectSalesVolume(Long organizationId, List<Long> cashierIds) {
        String sql = "select sum(c.amount)  from OrderItem c,Order o  " +
                " where c.orderId = o.id " +
                " and o.cashierUserId in (:id) " +
                " and c.available = 1 " +
                " and o.available = 1 " +
                " and o.available= 1 ";
        Query query = em.createQuery(sql);
        query.setParameter("id", cashierIds);
        List resultList = query.getResultList();
        Long total = 0L;
        if (resultList.size() > 0) {
            if (resultList.get(0) != null) {
                total = (Long) resultList.get(0);
            }
        }
        return total;
    }

    @Override
    public List<OrderItem> getEntitiesByIdList(Long uid) {
        StringBuffer sb = new StringBuffer();
        sb.append("from OrderItem o where o.order.user.id=?1 and useStatus=?2 and expiryTime>?3 ");
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, uid);
        query.setParameter(2, OrderItemStatusEnum.UNUSED);
        query.setParameter(3, new Date());
        return query.getResultList();
    }

    @Override
    public List<OrderItem> getOrderItemList(Long orderId) {
        String sql = "select * from ec_order_item e where e.order_id = " + orderId;
        Query query = em.createNativeQuery(sql, OrderItem.class);
        List<OrderItem> orderItemList = query.getResultList();
        return orderItemList;
    }

    @Override
    public DomainPage selectCheckingTicketRecord(Long organizationId, Map<String, Object> dateMap, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;

        String sql = "select c from OrderItem c where c.available = 1 and c.useStatus = ?1 and c.ticketerUser.organization.id = ?2 ";
        if (dateMap.size() > 0) {
            if (dateMap.get("startDate") != null && dateMap.get("endDate") != null) {
                sql = sql + " and date_format(c.useTime,'%y-%m-%d') between date_format(?3,'%y-%m-%d')  " +
                        " and date_format(?4,'%y-%m-%d') ";
            }
        }
        sql = sql + " order by c.updateTime desc ";
        Query query = em.createQuery(sql);
        query.setParameter(1, OrderItemStatusEnum.USED);
        query.setParameter(2, organizationId);
        if (dateMap.size() > 0) {
            query.setParameter(3, dateMap.get("startDate"));
            query.setParameter(4, dateMap.get("endDate"));
        }

        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List resultList = query.getResultList();
        //查询总记录数
        sql = "select count(c) from OrderItem c where c.available = 1 and c.useStatus = ?1 and c.ticketerUser.organization.id = ?2 ";
        if (dateMap.size() > 0) {
            if (dateMap.get("startDate") != null && dateMap.get("endDate") != null) {
                sql = sql + " and date_format(c.useTime,'%y-%m-%d') between date_format(?3,'%y-%m-%d')  " +
                        " and date_format(?4,'%y-%m-%d') ";
            }
        }
        sql = sql + " order by c.updateTime desc ";
        query = em.createQuery(sql);
        query.setParameter(1, OrderItemStatusEnum.USED);
        query.setParameter(2, organizationId);
        if (dateMap.size() > 0) {
            query.setParameter(3, dateMap.get("startDate"));
            query.setParameter(4, dateMap.get("endDate"));
        }

        List result = query.getResultList();
        Long totalCount = (Long) result.get(0);
        //封装DomainPage
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);
        return domainPage;
    }

    @Override
    public List<OrderItem> selectOrderItemByIds(List<Long> cashierIds) {
        String sql = "select c from OrderItem c " +
                " where c.available = 1 and c.order.available = 1 and c.product.available = 1 " +
                " and c.order.cashierUser.id in(:ids) and c.order.payStatus = ?1 " +
                " group by c.order.id";
        Query query = em.createQuery(sql);
        query.setParameter("ids", cashierIds);
        query.setParameter(1, PayStatusEnum.HAS_PAY);
        List resultList = query.getResultList();
        return resultList;
    }

    @Override
    @SuppressWarnings("all")
    public DomainPage getOrderItemsByConditions(Map<String, Object> conditions, DomainPage domainPage) {
        OrderTypeEnum orderType = (OrderTypeEnum) conditions.get("orderType");  // 订单类型
        String orderIdentifier = (String) conditions.get("orderIdentifier");    // 订单编号
        String productName = (String) conditions.get("productName");    // 商品名称
        String organizationName = (String) conditions.get("organizationName"); // 商户名称
        String isDistribution = (String) conditions.get("isDistribution"); // 是否分销
        OrderStatusEnum orderStatus = (OrderStatusEnum) conditions.get("orderStatus");  // 订单状态
        PayStatusEnum payStatus = (PayStatusEnum) conditions.get("payStatus"); // 支付状态
        Integer clearingStatus = (Integer) conditions.get("clearingStatus"); // 清分状态
        Date startTime = (Date) conditions.get("startTime");    // 开始时间
        Date endTime = (Date) conditions.get("endTime");    // 结束时间
        Long organizationId = (Long) conditions.get("organizationId"); // 商户ID

        StringBuilder sql = new StringBuilder("SELECT o.id,o.order_identifier,o.order_status,o.pay_status,");
        sql.append("o.order_time, u.username,o.total_price,p.product_name,o.amount,");
        sql.append("o.total_point_e,o.total_point_p,o.total_point_o,o.giving_point_e,o.giving_point_p,o.giving_point_o,");
        sql.append("org.organization_name,o.clearing_status");
        sql.append(" FROM ec_order_item oi, ec_order o, biz_product_snapshot p, sys_user u, sys_organization org");
        sql.append(" WHERE oi.available = 1");
        sql.append(" AND oi.order_id = o.id");
        sql.append(" AND oi.product_snapshot_id = p.id");
        sql.append(" AND o.user_id = u.id");
        sql.append(" AND p.organization_id = org.id");  // 连接商户信息

        // 订单类型
        if (orderType != null) sql.append(" AND o.order_type = " + orderType.getOrdinalReplace());
        // 订单编号
        if (StringUtils.isNotBlank(orderIdentifier))
            sql.append(" AND o.order_identifier like '" + orderIdentifier + "%'");
        // 商品名称
        if (StringUtils.isNotBlank(productName))
            sql.append(" AND p.product_name like '%" + productName + "%'");
        // 商户名称
        if (StringUtils.isNotBlank(organizationName))
            sql.append(" AND org.organization_name like '%" + organizationName + "%'");

        // 订单状态
        if (orderStatus != null) sql.append(" AND o.order_status = '" + orderStatus.toString() + "'");

        // 订单支付状态
        if (payStatus != null) sql.append(" AND o.pay_status = '" + payStatus.toString() + "'");

        // 清分状态
        if (clearingStatus != null) sql.append(" AND o.clearing_status = " + clearingStatus);

        // 订单时间段
        if (startTime != null && endTime != null) {
            String newStartTime = new DateTime(startTime).millisOfDay().withMinimumValue().toString(SystemConst.DATE_FORMAT);
            String newEndTime = new DateTime(endTime).millisOfDay().withMaximumValue().toString(SystemConst.DATE_FORMAT);
            sql.append(" AND o.order_time between '" + newStartTime + "' AND '" + newEndTime + "'");
        }
        // 直销分销 "Y"代表直销 "N"代表分销
        if ("Y".equals(isDistribution)) {
            // 当前账户的收银员
            sql.append(" AND o.site_id IN (");
            sql.append(" SELECT bs.id FROM biz_site bs WHERE bs.available = 1");
            if (organizationId != null) sql.append(" AND bs.organization_id = " + organizationId);
            sql.append(" )");
        } else if ("N".equals(isDistribution)) {
            // 非当前账户营业点
            sql.append(" AND o.site_id NOT IN (");
            sql.append(" SELECT bs.id FROM biz_site bs WHERE bs.available = 1");
            if (organizationId != null) sql.append(" AND bs.organization_id = " + organizationId);
            sql.append(" )");

            // 商品的根ID属于当前商户直销商品ID
            sql.append(" AND p.parent_product_id IN (SELECT cp.id FROM biz_product cp");
            sql.append(" WHERE cp.available = 1 AND cp.parent_product_id = 0"); // 它是根商品
            sql.append(" AND cp.organization_id = " + organizationId);
            sql.append(" )");
        }
        // 排序条件
        sql.append(" ORDER BY o.update_time DESC");
        // 分页条件
        long pageIndex = domainPage.getPageIndex();
        long pageSize = domainPage.getPageSize();
        sql.append(" LIMIT " + (pageIndex - 1) * pageSize + "," + pageSize);

        // 获取结果
        Query query = em.createNativeQuery(sql.toString());
        List<Object[]> result = query.getResultList();
        List<OrderVO> newResult = Lists.newArrayList();
        getNewResult(result, newResult);

        // 获取总纪录数
        long totalCount = getOrderItemTotalCount(conditions);
        DomainPage returnDomainPage = new DomainPage(pageSize, pageIndex, totalCount);
        returnDomainPage.setDomains(newResult);

        return returnDomainPage;
    }

    /**
     * 封装返回结果
     *
     * @param result    旧的结果
     * @param newResult 封装后的结果
     */
    private void getNewResult(List<Object[]> result, List<OrderVO> newResult) {
        for (Object[] line : result) {
            OrderVO orderVO = new OrderVO();
            orderVO.setOrderId(line[0] != null ? Long.parseLong(line[0].toString()) : 0l);
            orderVO.setOrderIdentifier(line[1] != null ? line[1].toString() : "");
            orderVO.setOrderStatus(line[2] != null ? OrderStatusEnum.valueOf(line[2].toString()) : null);
            orderVO.setPayStatus(line[3] != null ? PayStatusEnum.valueOf(line[3].toString()) : null);
            orderVO.setOrderTime(line[4] != null ? (Date) line[4] : null);
            orderVO.setUsername(line[5] != null ? line[5].toString() : "");
            orderVO.setTotalPrice(line[6] != null ? line[6].toString() : "");
            orderVO.setProductName(line[7] != null ? line[7].toString() : "");
            orderVO.setAmount(line[8] != null ? Integer.parseInt(line[8].toString()) : null);
            orderVO.setTotalPointE(line[9] != null ? Integer.parseInt(line[9].toString()) : null);
            orderVO.setTotalPointP(line[10] != null ? Integer.parseInt(line[10].toString()) : null);
            orderVO.setTotalPointO(line[11] != null ? Integer.parseInt(line[11].toString()) : null);
            orderVO.setGivingPointE(line[12] != null ? Integer.parseInt(line[12].toString()) : null);
            orderVO.setGivingPointP(line[13] != null ? Integer.parseInt(line[13].toString()) : null);
            orderVO.setGivingPointO(line[14] != null ? Integer.parseInt(line[14].toString()) : null);
            orderVO.setOrganizationName(line[15] != null ? line[15].toString() : "");
            orderVO.setClearingStatus(line[16] != null ? Integer.parseInt(line[16].toString()) : null);

            newResult.add(orderVO);
        }
    }

    /**
     * 获取商品订单的总纪录数
     *
     * @param conditions 条件
     * @return 返回的纪录数量
     */
    private long getOrderItemTotalCount(Map<String, Object> conditions) {
        OrderTypeEnum orderType = (OrderTypeEnum) conditions.get("orderType");  // 订单类型
        String orderIdentifier = (String) conditions.get("orderIdentifier");    // 订单编号
        String productName = (String) conditions.get("productName");    // 商品名称
        String organizationName = (String) conditions.get("organizationName"); // 商户名称
        String isDistribution = (String) conditions.get("isDistribution"); // 是否分销
        OrderStatusEnum orderStatus = (OrderStatusEnum) conditions.get("orderStatus");  // 订单状态
        PayStatusEnum payStatus = (PayStatusEnum) conditions.get("payStatus"); // 支付状态
        Integer clearingStatus = (Integer) conditions.get("clearingStatus"); // 清分状态
        Date startTime = (Date) conditions.get("startTime");    // 开始时间
        Date endTime = (Date) conditions.get("endTime");    // 结束时间
        Long organizationId = (Long) conditions.get("organizationId"); // 商户ID

        StringBuilder sql = new StringBuilder("SELECT COUNT(oi.id) FROM ec_order_item oi, ec_order o, biz_product_snapshot p, ");
        sql.append("sys_user u, sys_organization org");
        sql.append(" WHERE oi.available = 1");
        sql.append(" AND oi.order_id = o.id");
        sql.append(" AND oi.product_snapshot_id = p.id");
        sql.append(" AND o.user_id = u.id");
        sql.append(" AND p.organization_id = org.id");  // 连接营业点

        // 订单类型
        if (orderType != null) sql.append(" AND o.order_type = " + orderType.getOrdinalReplace());
        // 订单编号
        if (StringUtils.isNotBlank(orderIdentifier))
            sql.append(" AND o.order_identifier like '" + orderIdentifier + "%'");
        // 商品名称
        if (StringUtils.isNotBlank(productName))
            sql.append(" AND p.product_name like '%" + productName + "%'");
        // 商户名称
        if (StringUtils.isNotBlank(organizationName))
            sql.append(" AND org.organization_name like '%" + organizationName + "%'");
        // 订单状态
        if (orderStatus != null) sql.append(" AND o.order_status = '" + orderStatus.toString() + "'");
        // 订单支付状态
        if (payStatus != null) sql.append(" AND o.pay_status = '" + payStatus.toString() + "'");
        // 清分状态
        if (clearingStatus != null) sql.append(" AND o.clearing_status = " + clearingStatus);
        // 订单时间段
        if (startTime != null && endTime != null) {
            String newStartTime = new DateTime(startTime).millisOfDay().withMinimumValue().toString(SystemConst.DATE_FORMAT);
            String newEndTime = new DateTime(endTime).millisOfDay().withMaximumValue().toString(SystemConst.DATE_FORMAT);
            sql.append(" AND o.order_time between '" + newStartTime + "' AND '" + newEndTime + "'");
        }
        // 直销分销 "Y"代表直销 "N"代表分销
        if ("Y".equals(isDistribution)) {
            // 当前账户的收银员
            sql.append(" AND o.site_id IN (");
            sql.append(" SELECT bs.id FROM biz_site bs WHERE bs.available = 1");
            if (organizationId != null) sql.append(" AND bs.organization_id = " + organizationId);
            sql.append(" )");
        } else if ("N".equals(isDistribution)) {
            // 非当前账户营业点
            sql.append(" AND o.site_id NOT IN (");
            sql.append(" SELECT bs.id FROM biz_site bs WHERE bs.available = 1");
            if (organizationId != null) sql.append(" AND bs.organization_id = " + organizationId);
            sql.append(" )");

            // 商品的根ID属于当前商户
            sql.append(" AND p.parent_product_id IN (SELECT cp.id FROM biz_product cp");
            sql.append(" WHERE cp.available = 1 AND cp.parent_product_id = 0"); // 它是根商品
            sql.append(" AND cp.organization_id = " + organizationId);
            sql.append(" )");
        }

        // 获取结果
        Query query = em.createNativeQuery(sql.toString());
        List result = query.getResultList();
        long totalCount = 0;
        if (result.size() == 1) totalCount = Long.parseLong(result.get(0).toString());

        return totalCount;
    }

}
