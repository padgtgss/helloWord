/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.ec.impl;

import com.google.common.collect.Lists;
import com.pemass.common.core.constant.SystemConst;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.core.util.ArithmeticUtil;
import com.pemass.common.core.util.DateUtil;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.dao.ec.OrderDao;
import com.pemass.persist.domain.jpa.biz.Product;
import com.pemass.persist.domain.jpa.ec.Order;
import com.pemass.persist.domain.jpa.ec.OrderItem;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.persist.domain.vo.OrderVO;
import com.pemass.persist.enumeration.OrderItemStatusEnum;
import com.pemass.persist.enumeration.OrderStatusEnum;
import com.pemass.persist.enumeration.OrderTypeEnum;
import com.pemass.persist.enumeration.PayStatusEnum;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: OrderDaoImpl
 * @Author: pokl.huang
 * @CreateTime: 2014-11-18 10:44
 */
@Repository(value = "orderDao")
public class OrderDaoImpl extends JPABaseDaoImpl implements OrderDao {

    @Override
    public <T extends BaseDomain> void updateProductSaleNumber(Class<T> clazz, List<String> params, Integer amount) {
        StringBuffer sql = new StringBuffer();  //  update biz_product p set p.sale_number = p.sale_number + 1 where p.id in ();
        sql.append("update ");
        sql.append(clazz.getName());
        sql.append(" p set p.saleNumber = (p.saleNumber + " + amount + ") where p.id in (");
        for (int i = 0; i < params.size(); i++) {
            sql.append(" '");
            sql.append(params.get(i));
            sql.append(" ' ");
            if (i < params.size() - 1) {
                sql.append(" ,");
            }
        }
        sql.append(" )");
        Query query = em.createQuery(sql.toString());
        query.executeUpdate();
    }

    @Override
    public <T extends BaseDomain> void updateProductSaleNumberReturn(Class<T> clazz, List<String> params, Integer amount) {
        StringBuffer sql = new StringBuffer();  //  update biz_product p set p.sale_number = p.sale_number + 1 where p.id in ();
        sql.append("update ");
        sql.append(clazz.getName());
        sql.append(" p set p.saleNumber = (p.saleNumber - " + amount + ") where p.id in (");
        for (int i = 0; i < params.size(); i++) {
            sql.append(" '");
            sql.append(params.get(i));
            sql.append(" ' ");
            if (i < params.size() - 1) {
                sql.append(" ,");
            }
        }
        sql.append(" )");
        Query query = em.createQuery(sql.toString());
        query.executeUpdate();
    }

    @Override
    public DomainPage<OrderItem> getOrderList(Class clazz, int flag, List<Long> cashierIds, Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, Map<String, Object> compoundFieldNameValueMap, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;
        //查询满足条件的结果

        String sql = "select c from OrderItem c ,ProductSnapshot p," + clazz.getName() + " b where c.orderId = b.id and c.productSnapshotId = p.id and b.cashierUserId in (:id) and ";
        sql = sql + " c.available = 1 and b.available = 1 ";

        Date endDate = new Date();
        Calendar cl = Calendar.getInstance();
        cl.setTime(endDate);
        cl.add(Calendar.MONTH, -3);
        Date startDate = cl.getTime();
        SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
        String start = dd.format(startDate);
        String end = dd.format(endDate);
        if (compoundFieldNameValueMap.size() != 0) {
            if (compoundFieldNameValueMap.get("startTime") != null) {
                sql = sql + " and date_format(b.orderTime,'%y-%m-%d') >= date_format(?1,'%y-%m-%d')";
            }
            if (compoundFieldNameValueMap.get("endTime") != null) {
                sql = sql + " and date_format(b.orderTime,'%y-%m-%d') <= date_format(?2,'%y-%m-%d')";
            }
        } else {
            if (flag == 0) {
                sql = sql + " and date_format(b.orderTime,'%y-%m-%d') BETWEEN date_format(?1,'%y-%m-%d') and date_format(?2,'%y-%m-%d') ";
            }
        }

        Set<String> fieldNames = fieldNameValueMap.keySet();
        Iterator<String> iterator = fieldNames.iterator();
        int fiel = fieldNames.size();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            if ("parentProductId".equals(fieldName)) {
                if (flag == 0 || compoundFieldNameValueMap.size() != 0) {
                    sql = sql + " and p." + fieldName + "<>?" + (i + 2);
                } else {
                    sql = sql + " and p." + fieldName + "<>?" + i;
                }
            } else {
                if (flag == 0 || compoundFieldNameValueMap.size() != 0) {
                    sql = sql + " and b." + fieldName + " = ?" + (i + 2);
                } else {
                    sql = sql + " and b." + fieldName + " = ?" + i;
                }
            }
        }

        Set<String> fuzzyFieldNames = fuzzyFieldNameValueMap.keySet();
        Iterator<String> fuzzyIterator = fuzzyFieldNames.iterator();
        int fuzzy = fuzzyFieldNames.size();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            if ("productName".equals(fuzzyFieldName)) {
                if (flag == 0 || compoundFieldNameValueMap.size() != 0) {
                    sql = sql + " and p." + fuzzyFieldName + " like ?" + (fiel + i + 2);
                } else {
                    sql = sql + " and p." + fuzzyFieldName + " like ?" + (fiel + i);
                }
            } else {
                if (flag == 0 || compoundFieldNameValueMap.size() != 0) {
                    sql = sql + " and c." + fuzzyFieldName + " like ?" + (fiel + i + 2);
                } else {
                    sql = sql + " and c." + fuzzyFieldName + " like ?" + (fiel + i);
                }
            }
        }


        sql = sql + " group by b.id order by c.updateTime desc";
        Query query = em.createQuery(sql);
        query.setParameter("id", cashierIds);
        if (compoundFieldNameValueMap.size() != 0) {
            if (compoundFieldNameValueMap.get("startTime") != null) {
                query.setParameter(1, dd.format((Date) compoundFieldNameValueMap.get("startTime")));
            }
            if (compoundFieldNameValueMap.get("endTime") != null) {
                query.setParameter(2, dd.format((Date) compoundFieldNameValueMap.get("endTime")));
            }
        } else {
            if (flag == 0) {
                query.setParameter(1, start);
                query.setParameter(2, end);
            }
        }

        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            if (flag == 0 || compoundFieldNameValueMap.size() != 0) {
                query.setParameter(i + 2, fieldNameValueMap.get(fieldName));
            } else {
                query.setParameter(i, fieldNameValueMap.get(fieldName));
            }

        }

        fuzzyIterator = fuzzyFieldNames.iterator();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            if (flag == 0 || compoundFieldNameValueMap.size() != 0) {
                query.setParameter((fiel + i + 2), "%" + fuzzyFieldNameValueMap.get(fuzzyFieldName) + "%");
            } else {
                query.setParameter((fiel + i), "%" + fuzzyFieldNameValueMap.get(fuzzyFieldName) + "%");
            }

        }
        Long totalCount = (long) query.getResultList().size();


        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List<Order> resultList = query.getResultList();


        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);
        return domainPage;
    }

    @Override
    public DomainPage scenicSpotOrders(Class clazz, Long orgainzationId, Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, Map<String, Object> compoundFieldNameValueMap, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;
        //查询满足条件的结果
        String sql = "select c from " + clazz.getName() + " c where ";
        sql = sql + " c.product.id in (select b.id from Product b where b.originProductId in (select a.id from Product a where a.organization.id = " + orgainzationId + ")) and ";
        sql = sql + " c.available = 1 and c.order.available = 1 ";

        Date endDate = new Date();
        Calendar cl = Calendar.getInstance();
        cl.setTime(endDate);
        cl.add(Calendar.MONTH, -3);
        Date startDate = cl.getTime();
        SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
        String start = dd.format(startDate);
        String end = dd.format(endDate);
        if (compoundFieldNameValueMap.size() != 0) {
            if (compoundFieldNameValueMap.get("startTime") != null) {
                sql = sql + " and date_format(c.order.orderTime,'%y-%m-%d') >= date_format(?1,'%y-%m-%d')";
            }
            if (compoundFieldNameValueMap.get("endTime") != null) {
                sql = sql + " and date_format(c.order.orderTime,'%y-%m-%d') <= date_format(?2,'%y-%m-%d')";
            }
        } else {
            sql = sql + " and date_format(c.order.orderTime,'%y-%m-%d') BETWEEN date_format(?1,'%y-%m-%d') and date_format(?2,'%y-%m-%d') ";
        }

        Set<String> fieldNames = fieldNameValueMap.keySet();
        Iterator<String> iterator = fieldNames.iterator();
        int fiel = fieldNames.size();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            sql = sql + " and c." + fieldName + " = ?" + (i + 2);
        }

        Set<String> fuzzyFieldNames = fuzzyFieldNameValueMap.keySet();
        Iterator<String> fuzzyIterator = fuzzyFieldNames.iterator();
        int fuzzy = fuzzyFieldNames.size();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            if (i == 1) {
                sql = sql + " and c." + fuzzyFieldName + " like ?" + (fiel + i + 2);
            } else {
                sql = sql + " and c." + fuzzyFieldName + " like ?" + (fiel + i + 2);
            }
        }


        sql = sql + " group by c.order.id order by c.updateTime desc";
        Query query = em.createQuery(sql);
        if (compoundFieldNameValueMap.size() != 0) {
            if (compoundFieldNameValueMap.get("startTime") != null) {
                query.setParameter(1, dd.format((Date) compoundFieldNameValueMap.get("startTime")));
            }
            if (compoundFieldNameValueMap.get("endTime") != null) {
                query.setParameter(2, dd.format((Date) compoundFieldNameValueMap.get("endTime")));
            }
        } else {
            query.setParameter(1, start);
            query.setParameter(2, end);
        }


        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            query.setParameter(i + 2, fieldNameValueMap.get(fieldName));
        }

        fuzzyIterator = fuzzyFieldNames.iterator();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            query.setParameter((fiel + i + 2), "%" + fuzzyFieldNameValueMap.get(fuzzyFieldName) + "%");
        }
        Long totalCount = (long) query.getResultList().size();


        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List<Order> resultList = query.getResultList();


        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);
        return domainPage;
    }

    @Override
    public DomainPage getAllOrdersByPage(Map<String, Object> conditions, DomainPage domainPage) {
        Long pageIndex = domainPage.getPageIndex();
        Long pageSize = domainPage.getPageSize();
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;

        OrderStatusEnum orderStatus = (OrderStatusEnum) conditions.get("orderStatus");
        PayStatusEnum payStatus = (PayStatusEnum) conditions.get("payStatus");
        Date startDate = (Date) conditions.get("startDate");
        Date endDate = (Date) conditions.get("endDate");

        StringBuilder sql = new StringBuilder("SELECT o.order_identifier,(SELECT u.username FROM sys_user u WHERE u.id = o.user_id)");
        sql.append(" ,(SELECT (SELECT o.organization_name FROM sys_organization o WHERE o.id = s.organization_id) FROM biz_site s WHERE s.id = o.site_id) organizationName");
        sql.append(" ,(SELECT s.site_name FROM biz_site s WHERE s.id = o.site_id) siteName");
        sql.append(" ,o.order_time,(SELECT t.terminal_username FROM sys_terminal_user t WHERE t.id = o.cashier_user_id) cashierName");
        sql.append(" ,o.amount,o.total_price,o.total_point_e,o.total_point_p,o.order_status,o.pay_status");
        sql.append(" FROM ec_order AS o");
        sql.append(" WHERE o.available = 1");
        if (orderStatus != null) sql.append(" AND o.order_status = '" + orderStatus.toString() + "'");
        if (payStatus != null) sql.append(" AND o.pay_status = '" + payStatus.toString() + "'");
        if (startDate != null && endDate != null)
            sql.append(" AND o.order_time BETWEEN '" + DateUtil.gap(startDate, "yyyy-MM-dd") + " 00:00:00' AND '" + DateUtil.gap(endDate, "yyyy-MM-dd") + " 23:59:59'");
        sql.append(" GROUP BY o.order_identifier");
        sql.append(" ORDER BY o.order_time");
        sql.append(" LIMIT " + (pageIndex - 1) * pageSize + "," + pageIndex * pageSize);

        Query query = em.createNativeQuery(sql.toString());
        List<Object[]> resultList = query.getResultList();

        // 获取总纪录
        Long totalCount = getOrderItemTotalCount(pageIndex, pageSize, orderStatus, payStatus, startDate, endDate);

        DomainPage returnDomainPage = new DomainPage(pageSize, pageIndex, totalCount);
        returnDomainPage.setDomains(resultList);
        return returnDomainPage;
    }

    private Long getOrderItemTotalCount(Long pageIndex, Long pageSize, OrderStatusEnum orderStatus, PayStatusEnum payStatus, Date startDate, Date endDate) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(o.id) FROM ec_order AS o WHERE o.available = 1");
        if (orderStatus != null) sql.append(" AND o.order_status = '" + orderStatus.toString() + "'");
        if (payStatus != null) sql.append(" AND o.pay_status = '" + payStatus.toString() + "'");
        if (startDate != null && endDate != null)
            sql.append(" AND o.order_time BETWEEN '" + DateUtil.gap(startDate, "yyyy-MM-dd") + " 00:00:00' AND '" + DateUtil.gap(endDate, "yyyy-MM-dd") + " 23:59:59'");
        sql.append(" GROUP BY o.order_identifier");
        sql.append(" ORDER BY o.order_time");
        sql.append(" LIMIT " + (pageIndex - 1) * pageSize + "," + pageIndex * pageSize);

        Query query = em.createNativeQuery(sql.toString());
        List resultList = query.getResultList();

        Long totalCount = 0L;
        if (resultList.size() > 0) totalCount = Long.parseLong(resultList.get(0).toString());
        return totalCount;
    }

    @Override
    public DomainPage getUsersBuyProductByOrganizationId(Long orgainzationId, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;

        String sql = "SELECT o FROM Order o,TerminalUser tu,Organization oz,User u " +
                " WHERE o.cashierUserId = tu.id AND o.userId = u.id AND tu.organizationId = oz .id " +
                " AND o.available = 1 AND tu.available = 1 AND oz.available = 1 and u.available = 1 " +
                " AND oz.id = ?1 GROUP BY o.userId ORDER BY u.registerTime DESC";

        Query query = em.createQuery(sql);
        query.setParameter(1, orgainzationId);
        Long totalCount = (long) query.getResultList().size(); //获取分页前的总条数

        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List result = query.getResultList(); //获取分页后的数据

        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(result);
        return domainPage;
    }

    @Override
    public DomainPage<Order> selectOrder(Long uid, List<OrderItemStatusEnum> itemStatus, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;
        String sql = "select  distinct o from OrderTicket oi, Order o " +
                " where oi.orderId = o.id" +
                " and o.userId = ?1 " +
                " and oi.useStatus in ?2 " +
                " and o.payStatus = ?3 " +
                " and o.available = 1";

        Query query = em.createQuery(sql);
        query.setParameter(1, uid)
                .setParameter(2, itemStatus)
                .setParameter(3, PayStatusEnum.HAS_PAY);


        Long totalCount = (long) query.getResultList().size(); //获取分页前的总条数

        query = em.createQuery(sql);
        query.setParameter(1, uid)
                .setParameter(2, itemStatus)
                .setParameter(3, PayStatusEnum.HAS_PAY);

        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List result = query.getResultList(); //获取分页后的数据

        DomainPage<Order> domainPage = new DomainPage<Order>(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(result);
        return domainPage;
    }

    @Override
    public Order sumOrder(Long terminalUserId, String dateType) {
        String sql = "SELECT " +
                "IFNULL(SUM(t.total_price), 0) total_price," +
                "IFNULL(SUM(t.total_point_e), 0) total_point_e," +
                "IFNULL(SUM(t.total_point_p), 0) total_point_p " +
                "FROM " +
                "ec_order t " +
                "WHERE " +
                "t.pay_status = 'HAS_PAY' " +
                "AND t.cashier_user_id = '" + terminalUserId + "'";
        if (dateType.equals("day")) {
            sql = sql + "AND t.pay_time >= CURRENT_DATE () " +
                    "AND t.pay_time <= ADDDATE(CURDATE(), 1)";
        } else if (dateType.equals("month")) {
            sql = sql + "AND t.pay_time >= DATE_ADD(curdate(),interval -day(curdate())+1 day) " +
                    "AND t.pay_time <= DATE_ADD(curdate()-day(curdate())+1,interval 1 month)";
        } else if (dateType.equals("year")) {
            sql = sql + "AND t.pay_time >= DATE_SUB(CURDATE(),INTERVAL dayofyear(now())-1 DAY) " +
                    "AND t.pay_time <= concat(YEAR(now()),'-12-31')";
        }

        Query query = em.createNativeQuery(sql);
        List<Object[]> objects = query.getResultList();
        Order order = new Order();
        Object[] ob = objects.get(0);
        order.setTotalPrice(Double.valueOf(ob[0].toString()));
        order.setTotalPointE(Integer.valueOf(ob[1].toString()));
        order.setTotalPointP(Integer.valueOf(ob[2].toString()));

        return order;
    }


    /**
     * 多条件查询
     *
     * @param clazz
     * @param fieldNameValueMap
     * @param fuzzyFieldNameValueMap
     * @param compoundFieldNameValueMap
     * @param <T>
     * @return
     */
    @Override
    public <T extends BaseDomain> List<T> getOrderByCompoundConditions(Class clazz, Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, Map<String, Object> compoundFieldNameValueMap) {

        //查询满足条件的结果
        String sql = "select c from " + clazz.getName() + " c where ";
        sql = sql + " c.available = 1";
        Set<String> fieldNames = fieldNameValueMap.keySet();
        Iterator<String> iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            sql = sql + " and c." + fieldName + " = ?" + i;
        }

        Set<String> fuzzyFieldNames = fuzzyFieldNameValueMap.keySet();
        Iterator<String> fuzzyIterator = fuzzyFieldNames.iterator();
        int fuzzyBegin = fieldNames.size() == 0 ? 0 : fieldNames.size();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            sql = sql + " and c." + fuzzyFieldName + " like ?" + (fuzzyBegin + i);
        }

        sql = sql + " order by c.updateTime desc";
        Query query = em.createQuery(sql);

        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            query.setParameter(i, fieldNameValueMap.get(fieldName));
        }

        fuzzyIterator = fuzzyFieldNames.iterator();
        for (int i = 1; i <= fuzzyFieldNames.size(); i++) {
            String fuzzyFieldName = fuzzyIterator.next();
            query.setParameter((fuzzyBegin + i), "%" + fuzzyFieldNameValueMap.get(fuzzyFieldName) + "%");
        }


        List<T> resultList = query.getResultList();


        return resultList;
    }

    @Override
    public DomainPage getOrderByUsername(List<User> userList, Long terminalUserId, Long pageIndex, Long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;
        //查询满足条件的结果
        String sql = "select * from ec_order t where 1=1 and t.cashier_id = " + terminalUserId;
        if (userList.size() > 0 && userList != null) {
            sql += " and t.uid in (";
            for (int i = 0; i < userList.size(); i++) {
                sql += userList.get(i).getId();
                if (i < userList.size() - 1) {
                    sql += ",";
                }
            }
            sql += ")";
        }

        sql += " order by order_time desc";


        Query query = em.createNativeQuery(sql, Order.class);
        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults(pageSize.intValue());
        List<T> resultList = query.getResultList();

        Long totalCount = Long.valueOf(query.getResultList().size() + "");

        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);
        return domainPage;

    }

    @Override
    public List<Order> getOrderByPeriodOfTime(Class clazz, Map<String, Object> fieldNameValueMap) {

        //查询满足条件的结果
        String sql = "select c from " + clazz.getName() + " c where ";
        sql = sql + " c.available = 1 ";

        Date date = new Date();
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        cl.add(Calendar.DAY_OF_MONTH, -1);
        Date endDate = cl.getTime();
        SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
        String end = dd.format(endDate);
        System.out.println(end);
        sql = sql + " and date_format(c.payTime,'%y-%m-%d') = date_format(?1,'%y-%m-%d') ";

        Set<String> fieldNames = fieldNameValueMap.keySet();
        Iterator<String> iterator = fieldNames.iterator();
        int fiel = fieldNames.size();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            sql = sql + " and c." + fieldName + " = ?" + (i + 1);
        }

        sql = sql + "  order by c.updateTime desc";
        Query query = em.createQuery(sql);

        query.setParameter(1, end);

        iterator = fieldNames.iterator();
        for (int i = 1; i <= fieldNames.size(); i++) {
            String fieldName = iterator.next();
            query.setParameter(i + 1, fieldNameValueMap.get(fieldName));
        }

        List<Order> resultList = query.getResultList();

        return resultList;
    }

    public Organization getOrganizationByOrderId(Long orderId, OrderTypeEnum orderType) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT o from Organization as o WHERE o.id in ");
        sb.append(" (SELECT p.organizationId from OrderItem e,");
        if (orderType == OrderTypeEnum.CUSTOMIZATION_ORDER) {
            sb.append("CollectMoneyStrategySnapshot p where e.collectMoneyStrategySnapshotId=p.id AND e.orderId=?1) ");
        } else {
            sb.append("ProductSnapshot p where e.productSnapshotId=p.id AND e.orderId=?1) ");
        }
        Query query = em.createQuery(sb.toString());
        query.setParameter(1, orderId);

        List<Organization> result = query.getResultList();
        if (result != null) {
            return result.get(0);
        } else {
            return null;
        }
    }

    @Override
    @SuppressWarnings("all")
    public DomainPage getCustomizationOrdersByConditions(Map<String, Object> conditions, DomainPage domainPage) {
        StringBuilder sql = getSqlByConditions(conditions);
        // 分页条件
        long pageIndex = domainPage.getPageIndex();
        long pageSize = domainPage.getPageSize();
        sql.append(" LIMIT " + (pageIndex - 1) * pageSize + "," + pageSize);

        // 获取结果
        Query query = em.createNativeQuery(sql.toString());
        List<Object[]> result = query.getResultList();
        List<OrderVO> newResult = Lists.newArrayList();
        getCustomizationNewResult(result, newResult);

        // 获取总纪录数
        long totalCount = getCustomizationOrderTotalCount(conditions);
        DomainPage returnDomainPage = new DomainPage(pageSize, pageIndex, totalCount);
        returnDomainPage.setDomains(newResult);

        return returnDomainPage;
    }

    @SuppressWarnings("all")
    private StringBuilder getSqlByConditions(Map<String, Object> conditions) {
        OrderTypeEnum orderType = (OrderTypeEnum) conditions.get("orderType");  // 订单类型
        String orderIdentifier = (String) conditions.get("orderIdentifier");    // 订单编号
        String organizationName = (String) conditions.get("organizationName"); // 商户名
        String cashierName = (String) conditions.get("cashierName"); // 收银员
        OrderStatusEnum orderStatus = (OrderStatusEnum) conditions.get("orderStatus");  // 订单状态
        Date startTime = (Date) conditions.get("startTime");    // 开始时间
        Date endTime = (Date) conditions.get("endTime");    // 结束时间
        Long organizationId = (Long) conditions.get("organizationId"); // 商户ID

        StringBuilder sql = new StringBuilder("SELECT o.id,o.order_identifier,o.order_status,o.pay_status,");
        sql.append("o.order_time,tu.terminal_username,u.username,o.total_price,o.amount,");
        sql.append("o.total_point_e,o.total_point_p,o.total_point_o,o.giving_point_e,o.giving_point_p,o.giving_point_o,");
        sql.append("org.organization_name");
        sql.append(" FROM ec_order o, sys_user u, sys_terminal_user tu, sys_organization org");
        sql.append(" WHERE o.available = 1");
        sql.append(" AND o.user_id = u.id");
        sql.append(" AND o.cashier_user_id =  tu.id");
        sql.append(" AND tu.organization_id = org.id");

        // 自定义订单展示已付款
        sql.append(" AND o.pay_status = '" + PayStatusEnum.HAS_PAY.toString() + "'");
        // 商户ID
        if (organizationId != null) sql.append(" AND tu.organization_id = " + organizationId);
        // 订单类型
        if (orderType != null) sql.append(" AND o.order_type = " + orderType.getOrdinalReplace());
        // 订单编号
        if (StringUtils.isNotBlank(orderIdentifier))
            sql.append(" AND o.order_identifier like '" + orderIdentifier + "%'");
        // 商户名称
        if (StringUtils.isNotBlank(organizationName))
            sql.append(" AND org.organization_name like '%" + organizationName + "%'");
        // 收银员信息
        if (StringUtils.isNotBlank(cashierName))
            sql.append(" AND tu.terminal_username like '%" + cashierName + "%'");
        // 订单状态
        if (orderStatus != null) sql.append(" AND o.order_status = '" + orderStatus.toString() + "'");
        // 订单时间段
        if (startTime != null && endTime != null) {
            String newStartTime = new DateTime(startTime).millisOfDay().withMinimumValue().toString(SystemConst.DATE_FORMAT);
            String newEndTime = new DateTime(endTime).millisOfDay().withMaximumValue().toString(SystemConst.DATE_FORMAT);
            sql.append(" AND o.order_time between '" + newStartTime + "' AND '" + newEndTime + "'");
        }
        // 排序条件
        sql.append(" ORDER BY o.update_time DESC");
        return sql;
    }

    @Override
    @SuppressWarnings("all")
    public List<OrderVO> getCustomizationOrdersByConditions(Map<String, Object> conditions) {
        StringBuilder sql = getSqlByConditions(conditions);

        Query query = em.createNativeQuery(sql.toString());
        List<Object[]> result = query.getResultList();
        List<OrderVO> newResult = Lists.newArrayList();
        getCustomizationNewResult(result, newResult);

        return newResult;
    }

    @Override
    public List<Order> selectCustomOrderList() {
        String sql = "select * from ec_order where order_identifier like '81%' and order_status = 'CONFIRMED' and pay_status = 'NONE_PAY'";
        Query query = em.createNativeQuery(sql, Order.class);
        List<Order> orderList = query.getResultList();
        return orderList;
    }

    @Override
    public void updateOrderClearingStatus(Long orderId) {
        StringBuffer sql = new StringBuffer();
        sql.append("update Order set clearingStatus=1 where id=" + orderId);
        System.out.println("====sql===" + sql.toString());
        Query query = em.createQuery(sql.toString());
        query.executeUpdate();
    }

    @Override
    public DomainPage selectOrganizationOrder(Map<String, Object> map, long organizationId, long pageIndex, long pageSize) {
        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize < 1 ? 1 : pageSize;
        SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
        StringBuffer sb = new StringBuffer();
        sb.append("select o.id oid,s.organization_id sid from ec_order o,biz_site s where o.site_id = s.id  and s.organization_id = " + organizationId);
        sb.append(" and o.pay_status = 'HAS_PAY' and o.order_status = 'CONFIRMED'");
        sb.append(" and o.available = 1 and s.available = 1");
        if (map.size() != 0) {
            if (map.get("startTime") != null) {
                sb.append(" and date_format(o.pay_time,'%y-%m-%d') >= date_format(?1,'%y-%m-%d')");
            }
            if (map.get("endTime") != null) {
                sb.append(" and date_format(o.pay_time,'%y-%m-%d') <= date_format(?2,'%y-%m-%d')");
            }
        }

        sb.append(" order by o.pay_time desc");
        Query query = em.createNativeQuery(sb.toString());
        if (map.size() != 0) {
            if (map.get("startTime") != null) {
                query.setParameter(1, dd.format((Date) map.get("startTime")));
            }
            if (map.get("endTime") != null) {
                query.setParameter(2, dd.format((Date) map.get("endTime")));
            }
        }
        Long totalCount = (long) query.getResultList().size();
        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);
        List<Product> resultList = query.getResultList();

        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.getDomains().addAll(resultList);
        return domainPage;
    }

    @Override
    public Long getHasPayOrderAmountOfDay() {
        DateTime minDate = DateTime.now().minusDays(1).millisOfDay().withMinimumValue();
        DateTime maxDate = DateTime.now().minusDays(1).millisOfDay().withMaximumValue();

        StringBuffer sql = new StringBuffer(" SELECT COUNT(o.id) FROM ec_order o ")
                .append(" WHERE o.available=1 ")
                .append(" AND o.pay_status = 'HAS_PAY' ")
                .append(" AND o.pay_time >= ?1 and o.pay_time <= ?2 ");

        Query query = em.createNativeQuery(sql.toString());
        query.setParameter(1, minDate.toString("yyyy-MM-dd HH:mm:ss"));
        query.setParameter(2, maxDate.toString("yyyy-MM-dd HH:mm:ss"));
        List result = query.getResultList();

        Long totalCount = 0L;
        if (result.get(0) != null) {
            totalCount = Long.parseLong(result.get(0).toString());
        }
        return totalCount;
    }

    @Override
    public Float getHasPayOrderPricesOfDay() {
        DateTime minDate = DateTime.now().minusDays(1).millisOfDay().withMinimumValue();
        DateTime maxDate = DateTime.now().minusDays(1).millisOfDay().withMaximumValue();

        StringBuffer sql = new StringBuffer(" SELECT sum(o.total_price) FROM ec_order o ")
                .append(" WHERE o.available=1 ")
                .append(" AND o.pay_status = 'HAS_PAY' ")
                .append(" AND o.pay_time >= ?1 AND o.pay_time <= ?2 ");

        Query query = em.createNativeQuery(sql.toString());
        query.setParameter(1, minDate.toString("yyyy-MM-dd HH:mm:ss"));
        query.setParameter(2, maxDate.toString("yyyy-MM-dd HH:mm:ss"));
        List result = query.getResultList();

        Float totalPrice = 0F;
        if (result.get(0) != null) {
            totalPrice = ArithmeticUtil.ceil(Float.valueOf(result.get(0).toString()), 2);
        }
        return totalPrice;
    }

    @Override
    public Long getHasPayOrderOfDayByField(String field) {
        //创建最小值和最大值
        DateTime minDate = DateTime.now().minusDays(1).millisOfDay().withMinimumValue();
        DateTime maxDate = DateTime.now().minusDays(1).millisOfDay().withMaximumValue();

        StringBuffer sql = new StringBuffer(" SELECT sum(o." + field + ") FROM ec_order o ")
                .append(" WHERE o.available=1 ")
                .append(" AND o.pay_status = 'HAS_PAY' ")
                .append(" AND o.pay_time >= ?1 AND o.pay_time <= ?2 ");

        Query query = em.createNativeQuery(sql.toString());
        query.setParameter(1, minDate.toString("yyyy-MM-dd HH:mm:ss"));
        query.setParameter(2, maxDate.toString("yyyy-MM-dd HH:mm:ss"));
        List result = query.getResultList();

        Long totalCount = 0L;
        if (result.get(0) != null) {
            totalCount = Long.parseLong(result.get(0).toString());
        }
        return totalCount;
    }


    /**
     * 封装自定义订单返回结果
     *
     * @param result    旧的结果
     * @param newResult 封装后的结果
     */
    private void getCustomizationNewResult(List<Object[]> result, List<OrderVO> newResult) {
        for (Object[] line : result) {
            OrderVO orderVO = new OrderVO();
            orderVO.setOrderId(Long.parseLong(line[0].toString()));
            orderVO.setOrderIdentifier(line[1].toString());
            orderVO.setOrderStatus(OrderStatusEnum.valueOf(line[2].toString()));
            orderVO.setPayStatus(PayStatusEnum.valueOf(line[3].toString()));
            orderVO.setOrderTime((Date) line[4]);
            orderVO.setCasherUsername(line[5].toString());
            orderVO.setUsername(line[6].toString());
            orderVO.setTotalPrice(line[7].toString());
            orderVO.setAmount(Integer.parseInt(line[8].toString()));
            orderVO.setTotalPointE(Integer.parseInt(line[9].toString()));
            orderVO.setTotalPointP(Integer.parseInt(line[10].toString()));
            orderVO.setTotalPointO(Integer.parseInt(line[11].toString()));
            orderVO.setGivingPointE(Integer.parseInt(line[12].toString()));
            orderVO.setGivingPointP(Integer.parseInt(line[13].toString()));
            orderVO.setGivingPointO(Integer.parseInt(line[14].toString()));
            orderVO.setOrganizationName(line[15].toString());

            newResult.add(orderVO);
        }
    }

    /**
     * 获取自定义订单的总纪录数
     *
     * @param conditions 条件
     * @return 返回的纪录数量
     */
    @SuppressWarnings("all")
    private long getCustomizationOrderTotalCount(Map<String, Object> conditions) {
        OrderTypeEnum orderType = (OrderTypeEnum) conditions.get("orderType");  // 订单类型
        String orderIdentifier = (String) conditions.get("orderIdentifier");    // 订单编号
        String cashierName = (String) conditions.get("cashierName"); // 收银员
        String organizationName = (String) conditions.get("organizationName"); // 商户名
        OrderStatusEnum orderStatus = (OrderStatusEnum) conditions.get("orderStatus");  // 订单状态
        Date startTime = (Date) conditions.get("startTime");    // 开始时间
        Date endTime = (Date) conditions.get("endTime");    // 结束时间
        Long organizationId = (Long) conditions.get("organizationId"); // 商户ID
        StringBuilder sql = new StringBuilder("SELECT COUNT(o.id) FROM ec_order o,sys_user u,sys_terminal_user tu, sys_organization org");
        sql.append(" WHERE o.available = 1");
        sql.append(" AND o.user_id = u.id");
        sql.append(" AND o.cashier_user_id =  tu.id");
        sql.append(" AND tu.organization_id = org.id");

        // 自定义订单展示已付款
        sql.append(" AND o.pay_status = '" + PayStatusEnum.HAS_PAY.toString() + "'");
        // 商户ID
        if (organizationId != null) sql.append(" AND tu.organization_id = " + organizationId);
        // 订单类型
        if (orderType != null) sql.append(" AND o.order_type = " + orderType.getOrdinalReplace());
        // 订单编号
        if (StringUtils.isNotBlank(orderIdentifier))
            sql.append(" AND o.order_identifier like '" + orderIdentifier + "%'");
        // 商户名称
        if (StringUtils.isNotBlank(organizationName))
            sql.append(" AND org.organization_name like '%" + organizationName + "%'");
        // 收银员信息
        if (StringUtils.isNotBlank(cashierName))
            sql.append(" AND tu.terminal_username like '%" + cashierName + "%'");
        // 订单状态
        if (orderStatus != null) sql.append(" AND o.order_status = '" + orderStatus.toString() + "'");
        // 订单时间段
        if (startTime != null && endTime != null) {
            String newStartTime = new DateTime(startTime).millisOfDay().withMinimumValue().toString(SystemConst.DATE_FORMAT);
            String newEndTime = new DateTime(endTime).millisOfDay().withMaximumValue().toString(SystemConst.DATE_FORMAT);
            sql.append(" AND o.order_time between '" + newStartTime + "' AND '" + newEndTime + "'");
        }

        // 获取结果
        Query query = em.createNativeQuery(sql.toString());
        List result = query.getResultList();
        long totalCount = 0;
        if (result.size() == 1) totalCount = Long.parseLong(result.get(0).toString());

        return totalCount;
    }

}

