/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudpos.api.controller.ec;

import com.google.common.collect.ImmutableMap;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.domain.jpa.biz.ProductSnapshot;
import com.pemass.persist.domain.jpa.ec.OrderTicket;
import com.pemass.persist.domain.vo.PushMessageVO;
import com.pemass.persist.enumeration.PushMessageTypeEnum;
import com.pemass.pojo.biz.ProductSnapshotPojo;
import com.pemass.pojo.ec.OrderTicketPojo;
import com.pemass.service.jms.Producer;
import com.pemass.service.pemass.biz.ProductSnapshotService;
import com.pemass.service.pemass.ec.OrderTicketService;
import com.pemass.service.pemass.sys.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: OrderItemController
 * @Author: zhou hang
 * @CreateTime: 2014-11-19 11:31
 */
@Controller
@RequestMapping("/ticket")
public class TicketController {


    @Resource
    private UserService userService;

    @Resource
    private Producer pushProducer;

    @Resource
    private OrderTicketService orderTicketService;

    @Resource
    private ProductSnapshotService productSnapshotService;



    /**
     * 检票
     *
     * @param ticketCode     票码
     * @param terminalUserId 检票员id
     * @return
     */
    @PreAuthorize("hasRole('ROLE_TICKETER')")
    @RequestMapping(value = "/{ticketCode}/checkin", method = RequestMethod.POST)
    @ResponseBody
    public Object ticket(@PathVariable("ticketCode") String ticketCode, Long terminalUserId, String username) {

        Boolean ticketStatus = orderTicketService.checkinTicket(ticketCode, terminalUserId, username);
        if (ticketStatus) {
            this.ticketMessage(ticketCode);
        }
        return ImmutableMap.of("result", ticketStatus);
    }

    /**
     * 扫票码信息
     *
     * @param ticketCode 票码
     * @return
     */
    @PreAuthorize("hasRole('ROLE_TICKETER')")
    @RequestMapping(value = "/{ticketCode}/scan", method = RequestMethod.GET)
    @ResponseBody
    public Object confirmationTicket(@PathVariable("ticketCode") String ticketCode, Long terminalUserId) {
        OrderTicket orderTicket = orderTicketService.confirmationTicket(ticketCode, terminalUserId);

        /**1,封装数据*/
        ProductSnapshot productSnapshot = productSnapshotService.getSnapshotById(orderTicket.getProductSnapshotId());
        ProductSnapshotPojo productSnapshotPojo = new ProductSnapshotPojo();
        MergeUtil.merge(productSnapshot, productSnapshotPojo);

        OrderTicketPojo orderTicketPojo = new OrderTicketPojo();
        MergeUtil.merge(orderTicket, orderTicketPojo);
        orderTicketPojo.setProductSnapshotPojo(productSnapshotPojo);

        return orderTicketPojo;
    }

    /**
     * 根据检票员,查询检票记录
     *
     * @param terminalUserId 检票id
     * @param pageSize       每页显示多少条
     * @param pageIndex      第几页
     * @return domainPage对象
     */
    @PreAuthorize("hasRole('ROLE_TICKETER')")
    @RequestMapping(value = "search", method = RequestMethod.GET)
    @ResponseBody
    public Object ticketInfo(Long terminalUserId, Long pageSize, Long pageIndex) {
        DomainPage domainPage = new DomainPage();
        domainPage = orderTicketService.selectTicketInfo(terminalUserId, pageSize, pageIndex);

        /**1,封装数据*/
        List<OrderTicket> orderTicketList = domainPage.getDomains();
        List<OrderTicketPojo> orderTicketPojoList = new ArrayList<OrderTicketPojo>();

        for (OrderTicket orderTicket : orderTicketList) {
            OrderTicketPojo orderTicketPojo = new OrderTicketPojo();
            MergeUtil.merge(orderTicket, orderTicketPojo);
            ProductSnapshot productSnapshot = productSnapshotService.getSnapshotById(orderTicket.getProductSnapshotId());
            ProductSnapshotPojo productSnapshotPojo = new ProductSnapshotPojo();
            MergeUtil.merge(productSnapshot, productSnapshotPojo);
            orderTicketPojo.setProductSnapshotPojo(productSnapshotPojo);

            orderTicketPojoList.add(orderTicketPojo);
        }
        domainPage.setDomains(orderTicketPojoList);

        return domainPage;
    }


    /**
     * 检票成功后给用户发送推送消息
     */
    private void ticketMessage(String ticketCode) {
        /**推送消息**/
        OrderTicket ticket = orderTicketService.getOrderTicketByTicketCode(ticketCode);
        ProductSnapshot productSnapshot  = productSnapshotService.getSnapshotById(ticket.getProductSnapshotId());

        PushMessageVO pushMessageVO = new PushMessageVO();
        pushMessageVO.setAudience(ticket.getUserId().toString());
        pushMessageVO.setPushMessageType(PushMessageTypeEnum.TICKET_HAS_CHECKED);
        List<Object> param = new ArrayList<Object>();
        param.add(productSnapshot.getProductName());
        param.add(ticket.getTicketCode());
        pushMessageVO.setParam(param);
        pushProducer.send(pushMessageVO);

    }


//    @PreAuthorize("hasRole('ROLE_TICKETER')")
//    @RequestMapping(value = "uncheckin", method = RequestMethod.GET)
//    @ResponseBody
//    public Object countticket(Long terminalUserId) {
//
//        Integer result = orderItemService.usedTicket(terminalUserId);
//        return ImmutableMap.of("result", result);
//    }
}