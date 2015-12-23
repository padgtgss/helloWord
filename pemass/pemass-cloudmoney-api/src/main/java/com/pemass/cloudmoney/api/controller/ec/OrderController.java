/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.ec;

import com.google.common.collect.ImmutableMap;
import com.pemass.common.core.annotation.Auth;
import com.pemass.common.core.pojo.Body;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.domain.jpa.biz.ProductSnapshot;
import com.pemass.persist.domain.jpa.biz.Site;
import com.pemass.persist.domain.jpa.ec.Order;
import com.pemass.persist.domain.jpa.ec.OrderItem;
import com.pemass.persist.domain.jpa.poi.CollectMoneyStrategySnapshot;
import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.persist.domain.redis.Config;
import com.pemass.persist.enumeration.ConfigEnum;
import com.pemass.persist.enumeration.OrderStatusEnum;
import com.pemass.persist.enumeration.OrderTypeEnum;
import com.pemass.persist.enumeration.PayStatusEnum;
import com.pemass.pojo.biz.ProductSnapshotPojo;
import com.pemass.pojo.biz.SitePojo;
import com.pemass.pojo.ec.OrderItemPojo;
import com.pemass.pojo.ec.OrderPojo;
import com.pemass.pojo.poi.CollectMoneyStrategySnapshotPojo;
import com.pemass.pojo.poi.OrderPointPayDetailPojo;
import com.pemass.pojo.sys.BodyPojo;
import com.pemass.service.pemass.biz.ProductSnapshotService;
import com.pemass.service.pemass.biz.SiteService;
import com.pemass.service.pemass.ec.OrderItemService;
import com.pemass.service.pemass.ec.OrderService;
import com.pemass.service.pemass.poi.CollectMoneyStrategySnapshotService;
import com.pemass.service.pemass.sys.ConfigService;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @Description: 订单Controller
 * @Author: estn.zuo
 * @CreateTime: 2014-12-10 10:11
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @Resource
    private OrderItemService orderItemService;

    @Resource
    private ObjectMapper jacksonObjectMapper;

    @Resource
    private ProductSnapshotService productSnapshotService;

    @Resource
    private SiteService siteService;

    @Resource
    private ConfigService configService;

    @Resource
    private CollectMoneyStrategySnapshotService collectMoneyStrategySnapshotService;

    /**
     * 下单购买商品
     *
     * @param username            用户名
     * @param siteId              营业点ID
     * @param productId           商品ID
     * @param amount              总数量
     * @param totalPrice          实付金额
     * @param usePointE           使用E通币数量
     * @param usePointP           使用E积分数量
     * @param usePointO           使用一元购积分数量
     * @param orderPointPayDetail 积分支付明细
     * @return
     * @throws IOException
     */
    @Auth(entity = User.class, parameter = "username", fieldName = "username")
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Object insert(String username, Long siteId, Long productId, Integer amount, Double totalPrice,
                         Integer usePointE, Integer usePointP, Integer usePointO, String orderPointPayDetail) throws IOException {

        List<OrderPointPayDetailPojo> orderPointPayDetails = jacksonObjectMapper.readValue(orderPointPayDetail, new TypeReference<List<OrderPointPayDetailPojo>>() {
        });

        Order order = orderService.insertProductOrder(username, null, siteId, productId, amount, totalPrice, usePointE, usePointP, usePointO, orderPointPayDetails);
        OrderPojo orderPojo = new OrderPojo();
        MergeUtil.merge(order, orderPojo);

        return orderPojo;
    }


    /**
     * 取消订单
     *
     * @param orderId
     * @return
     */
    @Auth(entity = Order.class, parameter = "orderId", fieldName = "id")
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "cancel", method = RequestMethod.POST)
    @ResponseBody
    public Object cancel(Long orderId) {
        Order order = orderService.cancel(orderId);

        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setId(order.getId());
        orderPojo.setOrderIdentifier(order.getOrderIdentifier());

        return orderPojo;
    }


    /**
     * 查询用户订单列表
     *
     * @param uid
     * @param orderTypeEnum
     * @param payStatusEnum
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Auth(entity = Order.class, parameter = "uid")
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "search", method = RequestMethod.GET)
    @ResponseBody
    public Object list(Long uid,OrderStatusEnum orderStatusEnum , OrderTypeEnum orderTypeEnum, PayStatusEnum payStatusEnum, Long pageIndex, Long pageSize) {
        DomainPage orderDomainPage = orderService.selectOrderByUid(uid,null, orderStatusEnum, orderTypeEnum, payStatusEnum, pageIndex, pageSize);
        orderDomainPage.setDomains(this.fetchOrderPojoFromDomainPage(orderDomainPage));
        return orderDomainPage;
    }

    /**
     * 获取用户未支付订单数【只有普通订单】
     *
     * @param uid
     * @return
     */
    @Auth(entity = Order.class, parameter = "uid")
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "obligations", method = RequestMethod.GET)
    @ResponseBody
    public Object nonepayOrder(Long uid) {
        Integer total = orderService.countObligations(uid);
        return ImmutableMap.of("result",total);
    }

    /**
     * 封装OrderPojo
     *
     * @param orderDomainPage
     * @return
     */
    private List<OrderPojo> fetchOrderPojoFromDomainPage(DomainPage<Order> orderDomainPage) {
        List<Order> orderList = orderDomainPage.getDomains();
        List<OrderPojo> orderPojoList = new ArrayList<OrderPojo>();

        for (int i = 0; i < orderList.size(); i++) {
            Order order = orderList.get(i);
            OrderPojo orderPojo = fetchOrderInfoPojoFromOrder(order);
            orderPojoList.add(orderPojo);
        }
        return orderPojoList;
    }

    /**
     * @param order
     * @return
     */
    private OrderPojo fetchOrderInfoPojoFromOrder(Order order) {

        List<OrderItem> orderItemList = orderItemService.selectByOrderId(order.getId());

        OrderPojo orderPojo = new OrderPojo();
        /*-- 基本信息 --*/
        MergeUtil.merge(order, orderPojo);

        if (orderItemList.size() <= 0) {
            return orderPojo;
        }
        /*-- orderItemPojoList --*/
        List<OrderItemPojo> orderItemPojoList = new ArrayList<OrderItemPojo>();
        for (OrderItem orderItem : orderItemList) {
            OrderItemPojo orderItemPojo = new OrderItemPojo();
            MergeUtil.merge(orderItem, orderItemPojo);

            ProductSnapshotPojo productSnapshotPojo = null;
            if (orderItem.getProductSnapshotId() != null) {
                productSnapshotPojo = new ProductSnapshotPojo();
                ProductSnapshot productSnapshot = productSnapshotService.getSnapshotById(orderItem.getProductSnapshotId());
                MergeUtil.merge(productSnapshot, productSnapshotPojo);
                /**一元购手续费**/
                Config config = configService.getConfigByKey(ConfigEnum.LOAN_RATE);
                Float rate = Float.valueOf(config.getValue());
                Double o = Math.ceil((productSnapshot.getOutPrice() - 1) * rate);
                productSnapshotPojo.setPoundage(o.doubleValue());
            }

            CollectMoneyStrategySnapshotPojo strategySnapshotPojo = null;
            //Merge 自定义收款策略快照
            if (orderItem.getCollectMoneyStrategySnapshotId() != null) {
                CollectMoneyStrategySnapshot _strategySnapshot = collectMoneyStrategySnapshotService.getById(orderItem.getCollectMoneyStrategySnapshotId());
                strategySnapshotPojo = new CollectMoneyStrategySnapshotPojo();
                MergeUtil.merge(_strategySnapshot, strategySnapshotPojo);
            }

            orderItemPojo.setProductSnapshotPojo(productSnapshotPojo);
            orderItemPojo.setCollectMoneyStrategySnapshotPojo(strategySnapshotPojo);

            orderItemPojoList.add(orderItemPojo);
        }
        orderPojo.setOrderItemPojoList(orderItemPojoList);

        if (order.getSiteId() != null){
            Site site = siteService.getSiteById(order.getSiteId());
            SitePojo sitePojo = new SitePojo();
            MergeUtil.merge(site, sitePojo);
            List<BodyPojo> bodyPojoList = new ArrayList<BodyPojo>();
            for (Body body : site.getSummary()) {
                BodyPojo bodyPojo = new BodyPojo();
                MergeUtil.merge(body, bodyPojo);
                bodyPojoList.add(bodyPojo);
            }
            sitePojo.setSummary(bodyPojoList);
            orderPojo.setSitePojo(sitePojo);
        }


        return orderPojo;
    }



}
