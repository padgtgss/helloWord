/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudpos.api.controller.ec;


import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.core.pojo.Body;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.domain.jpa.biz.ProductSnapshot;
import com.pemass.persist.domain.jpa.biz.Site;
import com.pemass.persist.domain.jpa.ec.Order;
import com.pemass.persist.domain.jpa.ec.OrderItem;
import com.pemass.persist.domain.jpa.ec.Payment;
import com.pemass.persist.domain.jpa.poi.CollectMoneyStrategySnapshot;
import com.pemass.persist.domain.jpa.sys.TerminalUser;
import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.persist.domain.redis.Config;
import com.pemass.persist.enumeration.AccountStatusEnum;
import com.pemass.persist.enumeration.ConfigEnum;
import com.pemass.persist.enumeration.OrderStatusEnum;
import com.pemass.persist.enumeration.OrderTypeEnum;
import com.pemass.persist.enumeration.PayStatusEnum;
import com.pemass.persist.enumeration.PaymentTypeEnum;
import com.pemass.persist.enumeration.SmsTypeEnum;
import com.pemass.pojo.biz.ProductSnapshotPojo;
import com.pemass.pojo.biz.SitePojo;
import com.pemass.pojo.ec.OrderItemPojo;
import com.pemass.pojo.ec.OrderPojo;
import com.pemass.pojo.ec.PaymentPojo;
import com.pemass.pojo.ec.SumOrderPojo;
import com.pemass.pojo.poi.CollectMoneyStrategySnapshotPojo;
import com.pemass.pojo.poi.OrderPointPayDetailPojo;
import com.pemass.pojo.sys.BodyPojo;
import com.pemass.service.exception.EcError;
import com.pemass.service.exception.SysError;
import com.pemass.service.pemass.bas.SmsMessageService;
import com.pemass.service.pemass.biz.ProductSnapshotService;
import com.pemass.service.pemass.biz.SiteService;
import com.pemass.service.pemass.ec.OrderItemService;
import com.pemass.service.pemass.ec.OrderService;
import com.pemass.service.pemass.ec.PaymentService;
import com.pemass.service.pemass.poi.CollectMoneyStrategySnapshotService;
import com.pemass.service.pemass.sys.ConfigService;
import com.pemass.service.pemass.sys.TerminalUserService;
import com.pemass.service.pemass.sys.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: OrderController
 * @Author: pokl.huang
 * @CreateTime: 2014-11-18 09:40
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    private Log logger = LogFactory.getLog(OrderController.class);

    @Resource
    private UserService userService;

    @Resource
    private SmsMessageService smsMessageService;

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

    @Resource
    private TerminalUserService terminalUserService;

    @Resource
    private PaymentService paymentService;

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
    @RequestMapping(method = RequestMethod.POST, params = "orderType=PRODUCT_ORDER")
    @ResponseBody
    public Object insert(String username, Long terminalUserId, Long productId, Integer amount, Double totalPrice,
                         Integer usePointE, Integer usePointP, Integer usePointO, String orderPointPayDetail) throws IOException {
        User user = userService.getByUsername(username);
        if (null == user) {
            throw new BaseBusinessException(SysError.USER_NOT_EXIST);
        }
        List<OrderPointPayDetailPojo> orderPointPayDetails = jacksonObjectMapper.readValue(orderPointPayDetail, new TypeReference<List<OrderPointPayDetailPojo>>() {
        });
        TerminalUser terminalUser = terminalUserService.getTerminalUser(terminalUserId);
        if (terminalUser == null) {
            throw new BaseBusinessException(SysError.TERMINAlUSER_NOT_EXIST);
        }
        if (terminalUser.getAccountStatus().equals(AccountStatusEnum.FREEZE)) {
            throw new BaseBusinessException(SysError.USER_IS_FREEZE);
        }

        Order order = orderService.insertProductOrder(username, terminalUserId, terminalUser.getSiteId(), productId, amount, totalPrice, usePointE, usePointP, usePointO, orderPointPayDetails);
        OrderPojo orderPojo = new OrderPojo();
        MergeUtil.merge(order, orderPojo);

        return orderPojo;
    }

    /**
     * 自定义订单.下单
     *
     * @param username       用户名
     * @param originalPrice  原始价格(未折扣前)
     * @param terminalUserId 收银员ID
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CASHIER')")
    @RequestMapping(method = RequestMethod.POST, params = "orderType=CUSTOMIZATION_ORDER")
    @ResponseBody
    public Object insertCustomizationOrder(String username, Double originalPrice, Long terminalUserId) {
        User user = userService.getByUsername(username);
        if (null == user) {
            throw new BaseBusinessException(SysError.USER_NOT_EXIST);
        }
        TerminalUser terminalUser = terminalUserService.getTerminalUser(terminalUserId);
        if (terminalUser == null) {
            throw new BaseBusinessException(SysError.TERMINAlUSER_NOT_EXIST);
        }
        if (terminalUser.getAccountStatus().equals(AccountStatusEnum.FREEZE)) {
            throw new BaseBusinessException(SysError.USER_IS_FREEZE);
        }
        Order customizationOrder = orderService.insertCustomizationOrder(username, originalPrice, null, terminalUserId, null, null);
        return fetchOrderInfoPojoFromOrder(customizationOrder);
    }


    /**
     * 订单支付【包含自定义订单和商品订单】
     * </p>
     *
     *
     * @param orderId                        订单ID
     * @param paymentType                    支付方式
     * @param terminalUserId                 收银员ID
     * @param externalPaymentIdentifier　　　外部支付流水号
     * @param payId                          支付帐号
     * @param deviceId                       设备ID
     * @param posSerial　　　　　　　　　　　机具号
     * @param isSucceed                      是否支付成功
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CASHIER')")
    @RequestMapping(value = "/{orderId}/pay", method = RequestMethod.POST)
    @ResponseBody
    public Object pay(@PathVariable("orderId") Long orderId, PaymentTypeEnum paymentType,
                      Long terminalUserId, String externalPaymentIdentifier,String payId, Integer deviceId, String posSerial, Integer isSucceed) {

        TerminalUser terminalUser = terminalUserService.getTerminalUser(terminalUserId);
        if (terminalUser == null) {
            throw new BaseBusinessException(SysError.TERMINAlUSER_NOT_EXIST);
        }
        if (terminalUser.getAccountStatus().equals(AccountStatusEnum.FREEZE)) {
            throw new BaseBusinessException(SysError.USER_IS_FREEZE);
        }

        Order order = orderService.getById(orderId);
        Payment payment = null;
        OrderTypeEnum orderType = order.getOrderType();

        if (OrderTypeEnum.PRODUCT_ORDER == orderType || OrderTypeEnum.ONE_PURCHASE_ORDER == orderType) {
            if (isSucceed.equals(1)){
                payment = orderService.payProductOrder(orderId, paymentType, terminalUserId, externalPaymentIdentifier, payId, posSerial, deviceId,isSucceed);
            }else {
                payment = paymentService.insertOrderPayment(order, paymentType,  externalPaymentIdentifier, payId, posSerial, deviceId, 0, isSucceed);
            }

        }
        if (OrderTypeEnum.CUSTOMIZATION_ORDER == orderType) {
            if (isSucceed.equals(1)){
                payment = orderService.payCustomizationOrder(orderId, paymentType, terminalUserId, externalPaymentIdentifier, payId, posSerial, deviceId, isSucceed);
            } else {
                payment = paymentService.insertOrderPayment(order, paymentType, externalPaymentIdentifier, payId, posSerial, deviceId, 1, isSucceed);
            }
        }

        PaymentPojo paymentPojo = new PaymentPojo();
        MergeUtil.merge(payment, paymentPojo);
        return paymentPojo;
    }


    /**
     * 获取销售订单列表
     *
     * @param terminalUserId
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CASHIER')")
    @RequestMapping(value = "search", method = RequestMethod.GET)
    @ResponseBody
    public Object list(String username, Long terminalUserId, OrderStatusEnum orderStatusEnum, OrderTypeEnum orderTypeEnum, PayStatusEnum payStatusEnum, Long pageIndex, Long pageSize) {
        DomainPage orderDomainPage;
        TerminalUser terminalUser = terminalUserService.getTerminalUser(terminalUserId);
        if (terminalUser == null) {
            throw new BaseBusinessException(SysError.TERMINAlUSER_NOT_EXIST) ;
        }
        if (terminalUser.getAccountStatus().equals(AccountStatusEnum.FREEZE)) {
            throw new BaseBusinessException(SysError.USER_IS_FREEZE);
        }
        User user = userService.getByUsername(username);
        if (StringUtils.isNotBlank(username) && user ==null){
            throw new BaseBusinessException(SysError.USER_NOT_EXIST);
        }
        if (user == null ){
            orderDomainPage = orderService.selectOrderByUid(null, terminalUser.getSiteId(), orderStatusEnum, orderTypeEnum, payStatusEnum, pageIndex, pageSize);
        }
        else {
            orderDomainPage = orderService.selectOrderByUid(user.getId(), terminalUser.getSiteId(), orderStatusEnum, orderTypeEnum, payStatusEnum, pageIndex, pageSize);
        }


        orderDomainPage.setDomains(this.fetchOrderPojoFromDomainPage(orderDomainPage));
        return orderDomainPage;
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
        User user = userService.getById(order.getUserId());
        orderPojo.setUsername(user.getUsername());

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

        if (order.getSiteId() != null) {
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

    /**
     * 根据订单编号查询订单详情
     * <p/>
     * 其中包括查询商品订单和自定义订单
     *
     * @param orderIdentifier 订单编号
     * @return 订单POJO
     */
    @RequestMapping(value = "/identifier/{orderIdentifier}", method = RequestMethod.GET)
    @ResponseBody
    public Object selectByOrderIdentifier(@PathVariable("orderIdentifier") String orderIdentifier, Long terminalUserId) {
        Order order = orderService.selectByOrderIdentifier(orderIdentifier);
        if (order == null){
            throw new BaseBusinessException(EcError.ORDER_NOT_FOUND);
        }
        Site site = siteService.getSiteById(order.getSiteId());
        TerminalUser terminalUser = terminalUserService.getTerminalUser(terminalUserId);
        if (terminalUser == null) {
            throw new BaseBusinessException(SysError.TERMINAlUSER_NOT_EXIST);
        }
        if (terminalUser.getAccountStatus().equals(AccountStatusEnum.FREEZE)) {
            throw new BaseBusinessException(SysError.USER_IS_FREEZE);
        }
        if (site.getId() != terminalUser.getSiteId()) {
            throw new BaseBusinessException(EcError.CAN_NOT_SEARCH);
        }

        OrderPojo orderPojo = this.fetchOrderInfoPojoFromOrder(order);

        return orderPojo;
    }


    /**
     * 退货操作
     *
     * @param orderId
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CASHIER')")
    @RequestMapping(value = "/{orderId}/return", method = RequestMethod.POST)
    @ResponseBody
    public Object returnOrder(@PathVariable("orderId") Long orderId, String validateCode) {
        Order order = orderService.getById(orderId);
        User user = userService.getById(order.getUserId());
        smsMessageService.validateCode(user.getUsername(), SmsTypeEnum.ORDER_RETURN, validateCode);

        orderId = orderService.updateReturn(orderId);
        order = orderService.getById(orderId);
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setId(orderId);
        orderPojo.setOrderIdentifier(order.getOrderIdentifier());

        return orderPojo;
    }


    /**
     * 收款统计
     *
     * @param terminalUserId
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CASHIER')")
    @RequestMapping(value = "/terminaluser/{terminalUserId}/statistics", method = RequestMethod.GET)
    @ResponseBody
    public Object sumOrder(@PathVariable("terminalUserId") Long terminalUserId) {
        TerminalUser terminalUser = terminalUserService.getTerminalUser(terminalUserId);
        if (terminalUser == null) {
            throw new BaseBusinessException(SysError.TERMINAlUSER_NOT_EXIST);
        }
        if (terminalUser.getAccountStatus().equals(AccountStatusEnum.FREEZE)) {
            throw new BaseBusinessException(SysError.USER_IS_FREEZE);
        }

        Order order = orderService.sumOrder(terminalUserId, "day");

        List<SumOrderPojo> sumOrderPojoList = new ArrayList<SumOrderPojo>();
        SumOrderPojo sumOrderPojo = new SumOrderPojo();
        sumOrderPojo.setTotalPrice(order.getTotalPrice());
        sumOrderPojo.setUsePointE(order.getTotalPointE());
        sumOrderPojo.setUsePointP(order.getTotalPointP());

        sumOrderPojoList.add(sumOrderPojo);//当日统计

        order = new Order();
        sumOrderPojo = new SumOrderPojo();
        order = orderService.sumOrder(terminalUserId, "month");

        sumOrderPojo.setTotalPrice(order.getTotalPrice());
        sumOrderPojo.setUsePointE(order.getTotalPointE());
        sumOrderPojo.setUsePointP(order.getTotalPointP());

        sumOrderPojoList.add(sumOrderPojo);//当月统计

        order = new Order();
        sumOrderPojo = new SumOrderPojo();
        order = orderService.sumOrder(terminalUserId, "year");

        sumOrderPojo.setTotalPrice(order.getTotalPrice());
        sumOrderPojo.setUsePointE(order.getTotalPointE());
        sumOrderPojo.setUsePointP(order.getTotalPointP());

        sumOrderPojoList.add(sumOrderPojo);//当年统计

        return sumOrderPojoList;
    }

}

