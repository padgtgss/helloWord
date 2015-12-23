/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.ec;

import com.google.common.collect.ImmutableMap;
import com.pemass.common.core.annotation.Auth;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.domain.jpa.biz.Product;
import com.pemass.persist.domain.jpa.biz.ProductSnapshot;
import com.pemass.persist.domain.jpa.ec.OrderTicket;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.enumeration.OrderItemStatusEnum;
import com.pemass.pojo.biz.ProductSnapshotPojo;
import com.pemass.pojo.ec.OrderTicketPojo;
import com.pemass.pojo.sys.OrganizationPojo;
import com.pemass.service.pemass.biz.ProductService;
import com.pemass.service.pemass.biz.ProductSnapshotService;
import com.pemass.service.pemass.ec.OrderTicketService;
import com.pemass.service.pemass.sys.OrganizationService;
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
 * @Description: TicketController
 * @Author: estn.zuo
 * @CreateTime: 2014-12-22 14:49
 */
@Controller
@RequestMapping("/ticket")
public class TicketController {

    @Resource
    private OrderTicketService orderTicketService;

    @Resource
    private ProductSnapshotService productSnapshotService;

    @Resource
    private ProductService productService;

    @Resource
    private OrganizationService organizationService;

    /**
     * 获取电子票
     *
     * @param uid       用户ID
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Auth(entity = OrderTicket.class, parameter = "uid")
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "search", method = RequestMethod.GET)
    @ResponseBody
    public Object ticket(Long uid, OrderItemStatusEnum[] useStatus, long pageIndex, long pageSize) {

        List<String> orderItemStatusEnums= new ArrayList<String>();
        for (int i = 0; i < useStatus.length ; i++){
            orderItemStatusEnums.add(useStatus[i].toString());
        }
        DomainPage domainPage = orderTicketService.selectTickets(uid, orderItemStatusEnums, pageIndex, pageSize);
        domainPage.setDomains(this.fetchUnusedOrder(domainPage));
        return domainPage;
    }





    /**
     * 分享该电子票
     *
     * @param ticketId 电子票ID
     * @return
     */
    @Auth(entity = OrderTicket.class, parameter = "ticketId",fieldName = "id")
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/share/{ticketId}", method = RequestMethod.POST)
    @ResponseBody
    public Object share(@PathVariable("ticketId")Long ticketId) {
        return ImmutableMap.of("result", orderTicketService.shareTicket(ticketId));
    }


    /**
     * 封装
     *
     * @param domainPage
     * @return
     */
    private List<OrderTicketPojo> fetchUnusedOrder(DomainPage<OrderTicket> domainPage) {
        List<OrderTicket> orderTicketList = domainPage.getDomains();
        List<OrderTicketPojo> orderTicketPojoList = new ArrayList<OrderTicketPojo>();
        for (OrderTicket orderTicket : orderTicketList) {
            OrderTicketPojo orderTicketPojo= new OrderTicketPojo();
            MergeUtil.merge(orderTicket, orderTicketPojo);

            ProductSnapshot productSnapshot = productSnapshotService.getSnapshotById(orderTicket.getProductSnapshotId());
            ProductSnapshotPojo productSnapshotPojo = new ProductSnapshotPojo();
            MergeUtil.merge(productSnapshot,productSnapshotPojo);
            /**处理分享内容**/
            String  content  = productSnapshot.getShareContent();
            content = content.substring(0,content.indexOf("【")+1) + orderTicket.getTicketCode() + content.substring(content.indexOf("】"));
            productSnapshotPojo.setShareContent(content);

            orderTicketPojo.setProductSnapshotPojo(productSnapshotPojo);

            Product product = productService.getProductInfo(productSnapshot.getProductId());
            Organization organization = organizationService.getOrganizationById(product.getOrganizationId());
            OrganizationPojo organizationPojo = new OrganizationPojo();
            MergeUtil.merge(organization,organizationPojo);

            orderTicketPojo.setOrganizationPojo(organizationPojo);

            orderTicketPojoList.add(orderTicketPojo);
        }
        return orderTicketPojoList;
    }
}
