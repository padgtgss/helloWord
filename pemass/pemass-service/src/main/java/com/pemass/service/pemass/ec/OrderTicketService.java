/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.ec;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.ec.OrderTicket;

import java.util.List;

/**
 * @Description: OrderItemService
 * @Author: zhou hang
 * @CreateTime: 2014-11-19 11:33
 */
public interface OrderTicketService {

    /**
     * 保存电子票信息
     *
     * @param orderTicket
     */
    void saveOrderTicket(OrderTicket orderTicket);

    /**
     * 根据订单ID 获取 票码列表
     *
     * @param orderId
     * @return
     */
    List<OrderTicket> getOrderTicketsByOrderId(Long orderId);


    /**
     * 扫票码信息
     *
     * @param ticketCode 票码
     * @return
     */
    OrderTicket confirmationTicket(String ticketCode, Long terminalUserId);


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
     * 生成电子票码
     *
     * @return
     */
    String generateTicketCode();


    /**
     * 获取用户的电子票
     *
     * @param uid
     * @param orderItemStatusEnums
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectTickets(Long uid, List<String> orderItemStatusEnums, long pageIndex, long pageSize);

    /**
     * 分享电子票
     *
     * @param ticketId
     * @return
     */
    Boolean shareTicket(Long ticketId);

    /**
     * 查询所有电子票
     * @return
     */
    List<OrderTicket> selectAllOrderTicket();

    /**
     * 修改电子票信息
     * @param orderTicket
     */
    void updateOrderTicket(OrderTicket orderTicket);

    /**
     * 根据ID获取电子票
     * @param orderTicketId
     * @return
     */
    OrderTicket getOrderTicketById(Long orderTicketId);


    /**
     * 检票
     *
     * @param ticketCode     票码
     * @param terminalUserId 检票员id
     */
    Boolean checkinTicket(String ticketCode, Long terminalUserId, String username);


    /**
     * 根据票码获取电子票
     *
     * @param ticketCode
     * @return
     */
    OrderTicket getOrderTicketByTicketCode(String ticketCode);
}
