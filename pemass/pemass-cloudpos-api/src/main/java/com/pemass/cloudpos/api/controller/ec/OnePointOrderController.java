/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudpos.api.controller.ec;

import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.domain.jpa.ec.Order;
import com.pemass.persist.domain.jpa.sys.TerminalUser;
import com.pemass.pojo.ec.OrderPojo;
import com.pemass.pojo.poi.OrderPointPayDetailPojo;
import com.pemass.service.pemass.ec.OrderService;
import com.pemass.service.pemass.sys.TerminalUserService;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;


/**
 * @Description: 订单Controller
 * @Author: estn.zuo
 * @CreateTime: 2014-12-10 10:11
 */
@Controller
@RequestMapping("/onepointorder")
public class OnePointOrderController {

    @Resource
    private OrderService orderService;

    @Resource
    private ObjectMapper jacksonObjectMapper;

    @Resource
    private TerminalUserService terminalUserService;


    /**
     * 下单购买商品
     *
     * @param username            用户名
     * @param terminalUserId      收银员ID
     * @param productId           商品ID
     * @param amount              总数量
     * @param totalPrice          实付金额
     * @param usePointE           使用E通币数量
     * @param usePointP           使用E积分数量
     * @param usePointO           使用一元购积分数量
     * @param orderPointPayDetail 积分支付明细
     * @return
     * @throws java.io.IOException
     */
    @PreAuthorize("hasRole('ROLE_CASHIER')")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Object insert(String username, Long terminalUserId, Long productId, Integer amount, Double totalPrice,
                         Integer usePointE, Integer usePointP, Integer usePointO, String orderPointPayDetail) throws IOException {
        TerminalUser terminalUser = terminalUserService.getTerminalUser(terminalUserId);

        List<OrderPointPayDetailPojo> orderPointPayDetails = jacksonObjectMapper.readValue(orderPointPayDetail, new TypeReference<List<OrderPointPayDetailPojo>>() {
        });

        Order order = orderService.insertOnePointOrder(username, terminalUserId ,terminalUser.getSiteId(), productId, amount, totalPrice, usePointE, usePointP, usePointO, orderPointPayDetails);
        OrderPojo orderPojo = new OrderPojo();
        MergeUtil.merge(order, orderPojo);

        return orderPojo;
    }




}
