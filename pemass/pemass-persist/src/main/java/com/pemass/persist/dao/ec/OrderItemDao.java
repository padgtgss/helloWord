/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.ec;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.ec.OrderItem;
import com.pemass.persist.domain.jpa.ec.OrderTicket;
import com.pemass.persist.enumeration.OrderItemStatusEnum;

import java.util.List;
import java.util.Map;

/**
 * @Description: OrderItemDao
 * @Author: estn.zuo
 * @CreateTime: 2014-12-16 16:00
 */
public interface OrderItemDao extends BaseDao {

    /**
     * 只返回OrderItem的一些特定字段
     *
     * @param orderId
     * @return
     */
    List<OrderItem> selectOrderItemWithFieldsByOrderId(Long orderId);

    /**
     * 根据订单ID查询未使用的订单项
     * <p/>
     * 只返回OrderItem的一些特定字段
     *
     * @param orderId    订单ID
     * @param itemStatus 订单状态集合
     * @return
     */
    List<OrderTicket> selectOrderItemByOrderId(Long orderId, List<OrderItemStatusEnum> itemStatus);

    /**
     * 查询即将过期的电子票数量
     *
     * @param uid 用户ID
     * @return
     */
    Long selectWillOverdueTicket(Long uid);

    /**
     * 根据商户id，统计当前商户本月的销售量
     *
     * @param organizationId
     * @return
     */
    List selectThisMonthSalesVolume(Long organizationId);

    /**
     * 根据商户id，统计当前用户的销售量
     *
     * @param organizationId 商户id
     * @return
     */
    Long selectSalesVolume(Long organizationId, List<Long> cashierIds);

    /**
     * 查询用户没有过期的订单
     *
     * @param uid
     * @return
     */
    List<OrderItem> getEntitiesByIdList(Long uid);

    List<OrderItem> getOrderItemList(Long orderId);

    /**
     * 根据商户id查询检票记录
     *
     * @param organizationId 商户id
     * @return
     */
    DomainPage selectCheckingTicketRecord(Long organizationId, Map<String, Object> dateMap, long pageIndex, long pageSize);

    /**
     * 根据收银员id集合，查询所有订单信息
     *
     * @param cashierIds 收银员id集合
     * @return
     */
    List<OrderItem> selectOrderItemByIds(List<Long> cashierIds);

    /**
     * 获取满足条件的商品订单
     *
     * @param conditions 商品订单查询条件
     * @param domainPage 分页信息
     * @return 返回的订单结果
     */
    DomainPage getOrderItemsByConditions(Map<String, Object> conditions, DomainPage domainPage);

}
