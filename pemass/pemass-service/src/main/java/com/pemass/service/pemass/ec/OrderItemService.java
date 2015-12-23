/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.ec;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.biz.ProductSnapshot;
import com.pemass.persist.domain.jpa.ec.Order;
import com.pemass.persist.domain.jpa.ec.OrderItem;
import com.pemass.persist.domain.jpa.ec.OrderTicket;
import com.pemass.persist.domain.jpa.poi.CollectMoneyStrategySnapshot;
import com.pemass.persist.domain.vo.OrderVO;
import com.pemass.persist.enumeration.OrderItemStatusEnum;

import java.util.List;
import java.util.Map;

/**
 * @Description: OrderItemService
 * @Author: zhou hang
 * @CreateTime: 2014-11-19 11:33
 */
public interface OrderItemService {

    /**
     * 保存订单项
     *
     * @param orderItem
     */
    void saveOrderItem(OrderItem orderItem);

    /**
     * 跟新订单项
     *
     * @param source
     * @return
     */
    OrderItem updateOrderItem(OrderItem source);

    /**
     * 根据ID获取订单项
     *
     * @param orderItemId
     * @return
     */
    OrderItem getById(Long orderItemId);

    /**
     * 检票
     *
     * @param ticketCode     票码
     * @param terminalUserId 检票员id
     */
    Boolean checkinTicket(String ticketCode, Long terminalUserId, String username);

    /**
     * 根据检票员,查询检票记录
     *
     * @param ticketerUserId 检票id
     * @param pageSize       每页显示多少条
     * @param pageIndex      第几页
     * @return
     */
    DomainPage selectTicketInfo(Long ticketerUserId, Long pageSize, Long pageIndex);


    /**
     * 查询订单列表
     *
     * @param flag
     * @param id
     * @param fuzzyFieldNameValueMap
     * @param compoundFieldNameValueMap
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getOrderList(int flag, List<Long> id, String isDistribution, Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, Map<String, Object> compoundFieldNameValueMap, long pageIndex, long pageSize);

    /**
     * 获取满足条件的订单
     *
     * @param conditions 商品订单的查询条件
     * @return 返回的订单结果
     */
    DomainPage getOrdersByConditions(Map<String, Object> conditions, DomainPage domainPage);

    /**
     * 获取满足条件的自定义订单
     *
     * @param conditions 自定义订单的查询条件
     * @param domainPage 分页信息
     * @return 返回结果
     */
    DomainPage getCustomizationOrdersByCondition(Map<String, Object> conditions, DomainPage domainPage);

    /**
     * 获取满足条件的自定义订单
     *
     * @param conditions 自定义订单的查询条件
     * @return 返回结果
     */
    List<OrderVO> getCustomizationOrdersByCondition(Map<String, Object> conditions);

    /**
     * 只返回OrderItem的一些特定字段,
     * <p/>
     * 使用场景有限
     *
     * @param orderId
     * @return
     */
    List<OrderItem> selectOrderItemWithFieldsByOrderId(Long orderId);

    /**
     * 根据订单号查询订单明细
     *
     * @param fieldNameValueMap
     * @return
     */
    List<OrderItem> getOrderItemList(Map<String, Object> fieldNameValueMap);

    List<OrderTicket> selectOrderItemByOrderId(Long orderId, List<OrderItemStatusEnum> itemStatus);

    Long selectWillOverdueTicket(Long uid);

    void updateOrderItem(Long orderItemId);

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
     * 查询出C端用户所用 电子票
     *
     * @param uid
     * @return
     */
    List<OrderItem> getByIdList(Long uid);

    List<OrderItem> selectOrderItem(Long orderId);

    /**
     * 根据商户id分页查询检票记录
     *
     * @param organizationId 商户id
     * @param dateMap        时间筛选条件
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectCheckTicketRecordList(Long organizationId, Map<String, Object> dateMap, long pageIndex, long pageSize);

    /**
     * 根据票码获取票的信息
     *
     * @param ticketCode
     * @return
     */
    OrderItem getOrderItemByTicketCode(String ticketCode);

    Integer usedTicket(Long terminalUserId);

    /**
     * 根据商户id获取当前商户的直接销售利润
     *
     * @param organizationId 商户id
     * @return
     */
    Double getDirectProfit(Long organizationId);

    /**
     * 根据商户id获取当前商户的分销利润
     *
     * @param organizationId 商户id
     * @return
     */
    Double getDistributionProfit(Long organizationId);


    /**
     * 保存订单项
     * <p/>
     * 自定义订单项
     *
     * @param order            订单
     * @param amount           数量
     * @param strategySnapshot 策略快照
     */
    void saveOrderItem4CustomizationOrder(Order order, Integer amount, CollectMoneyStrategySnapshot strategySnapshot);

    /**
     * 保存订单项
     * <p/>
     * 商品订单项
     *
     * @param order           订单
     * @param amount          数量
     * @param productSnapshot 商品快照
     */
    void saveOrderItem4Order(Order order, Integer amount, ProductSnapshot productSnapshot);


    /**
     * 根据订单ID获取订单项
     *
     * @param orderId
     * @return
     */
    List<OrderItem> selectByOrderId(Long orderId);
}
