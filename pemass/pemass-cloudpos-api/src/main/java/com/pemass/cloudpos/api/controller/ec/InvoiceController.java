/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudpos.api.controller.ec;


import com.google.common.collect.ImmutableMap;
import com.pemass.persist.domain.jpa.biz.Product;
import com.pemass.persist.domain.jpa.ec.Invoice;
import com.pemass.persist.domain.jpa.ec.Order;
import com.pemass.persist.domain.jpa.ec.OrderItem;
import com.pemass.persist.enumeration.InvoiceStatusEnum;
import com.pemass.service.pemass.biz.ProductService;
import com.pemass.service.pemass.ec.InvoiceService;
import com.pemass.service.pemass.ec.OrderItemService;
import com.pemass.service.pemass.ec.OrderService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: OrderController
 * @Author: pokl.huang
 * @CreateTime: 2014-11-18 09:40
 */
@Controller
@RequestMapping("/invoice")
public class InvoiceController {

    private Log logger = LogFactory.getLog(InvoiceController.class);


    @Resource
    private OrderService orderService;


    @Resource
    private ProductService productService;


    @Resource
    private InvoiceService invoiceService;

    @Resource
    private OrderItemService orderItemService;

    /**
     * 开发票
     *
     * @param orderId
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CASHIER')")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Object addInvoice(Long orderId,String invoiceTitle,String receiveName,String receivePhone,String receiveAddress,String postDescription) {
        Order order = orderService.getById(orderId);
        List<OrderItem> orderItemList = orderItemService.selectByOrderId(order.getId());
        OrderItem orderItem = orderItemList.get(0);
        Product product = productService.getProductInfo(orderItem.getProductSnapshotId());

        Invoice invoice = new Invoice();
        invoice.setOrderId(order.getId());
        invoice.setInvoiceStatus(InvoiceStatusEnum.NONE_SEND);
        invoice.setInvoiceTitle(invoiceTitle);
        invoice.setProductId(product.getId());
        invoice.setPostDescription(postDescription);
        invoice.setReceiveAddress(receiveAddress);
        invoice.setReceiveName(receiveName);
        invoice.setReceivePhone(receivePhone);
        invoice.setTotalPrice(order.getTotalPrice());

        invoice = invoiceService.addInvoice(invoice);
        return ImmutableMap.of("result", invoice.getId());
    }




}

