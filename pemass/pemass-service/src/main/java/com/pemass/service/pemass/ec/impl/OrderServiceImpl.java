/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.ec.impl;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.core.util.ArithmeticUtil;
import com.pemass.common.core.util.BarCodeUtil;
import com.pemass.common.core.util.QRCodeUtil;
import com.pemass.common.core.util.UUIDUtil;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.domain.Expression;
import com.pemass.common.server.domain.Operation;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.dao.ec.OrderDao;
import com.pemass.persist.domain.jpa.biz.Product;
import com.pemass.persist.domain.jpa.biz.ProductCategory;
import com.pemass.persist.domain.jpa.biz.ProductSnapshot;
import com.pemass.persist.domain.jpa.biz.Site;
import com.pemass.persist.domain.jpa.ec.Clearing;
import com.pemass.persist.domain.jpa.ec.Order;
import com.pemass.persist.domain.jpa.ec.OrderItem;
import com.pemass.persist.domain.jpa.ec.OrderTicket;
import com.pemass.persist.domain.jpa.ec.Payment;
import com.pemass.persist.domain.jpa.poi.CollectMoneyScheme;
import com.pemass.persist.domain.jpa.poi.CollectMoneyStrategy;
import com.pemass.persist.domain.jpa.poi.CollectMoneyStrategySnapshot;
import com.pemass.persist.domain.jpa.poi.OnePointConsumeDetail;
import com.pemass.persist.domain.jpa.poi.OnePointDetail;
import com.pemass.persist.domain.jpa.poi.OrganizationConsumeDetail;
import com.pemass.persist.domain.jpa.poi.OrganizationPointDetail;
import com.pemass.persist.domain.jpa.poi.PointPool;
import com.pemass.persist.domain.jpa.poi.PointPurchase;
import com.pemass.persist.domain.jpa.poi.UserConsumeDetail;
import com.pemass.persist.domain.jpa.poi.UserPointDetail;
import com.pemass.persist.domain.jpa.sys.BankCard;
import com.pemass.persist.domain.jpa.sys.Company;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.domain.jpa.sys.TerminalUser;
import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.persist.domain.redis.Config;
import com.pemass.persist.domain.vo.ClearingVo;
import com.pemass.persist.domain.vo.PointAreaVO;
import com.pemass.persist.domain.vo.PushMessageVO;
import com.pemass.persist.enumeration.ClearSourceEnum;
import com.pemass.persist.enumeration.ConfigEnum;
import com.pemass.persist.enumeration.ConsumeTypeEnum;
import com.pemass.persist.enumeration.OrderItemStatusEnum;
import com.pemass.persist.enumeration.OrderStatusEnum;
import com.pemass.persist.enumeration.OrderTypeEnum;
import com.pemass.persist.enumeration.PayStatusEnum;
import com.pemass.persist.enumeration.PaymentTypeEnum;
import com.pemass.persist.enumeration.PointChannelEnum;
import com.pemass.persist.enumeration.PointTypeEnum;
import com.pemass.persist.enumeration.PushMessageTypeEnum;
import com.pemass.persist.enumeration.SequenceEnum;
import com.pemass.persist.enumeration.SettlementRoleEnum;
import com.pemass.persist.enumeration.SmsTypeEnum;
import com.pemass.persist.enumeration.TransactionAccountTypeEnum;
import com.pemass.pojo.biz.ProductSnapshotPojo;
import com.pemass.pojo.ec.OrderItemPojo;
import com.pemass.pojo.ec.OrderPojo;
import com.pemass.pojo.ec.OrderTicketPojo;
import com.pemass.pojo.poi.CollectMoneyStrategySnapshotPojo;
import com.pemass.pojo.poi.OrderPointPayDetailPojo;
import com.pemass.pojo.poi.UserPointAggregationPojo;
import com.pemass.service.constant.PemassConst;
import com.pemass.service.exception.BizError;
import com.pemass.service.exception.EcError;
import com.pemass.service.exception.PoiError;
import com.pemass.service.exception.SysError;
import com.pemass.service.jms.Producer;
import com.pemass.service.pemass.bas.SequenceService;
import com.pemass.service.pemass.bas.SmsMessageService;
import com.pemass.service.pemass.biz.ProductCategoryService;
import com.pemass.service.pemass.biz.ProductService;
import com.pemass.service.pemass.biz.ProductSnapshotService;
import com.pemass.service.pemass.biz.SiteService;
import com.pemass.service.pemass.ec.OrderItemService;
import com.pemass.service.pemass.ec.OrderService;
import com.pemass.service.pemass.ec.OrderTicketService;
import com.pemass.service.pemass.ec.PaymentService;
import com.pemass.service.pemass.poi.CollectMoneySchemeService;
import com.pemass.service.pemass.poi.CollectMoneyStrategyService;
import com.pemass.service.pemass.poi.CollectMoneyStrategySnapshotService;
import com.pemass.service.pemass.poi.OnePointConsumeDetailService;
import com.pemass.service.pemass.poi.OnePointDetailService;
import com.pemass.service.pemass.poi.OrganizationConsumeDetailService;
import com.pemass.service.pemass.poi.OrganizationPointDetailService;
import com.pemass.service.pemass.poi.PointPoolService;
import com.pemass.service.pemass.poi.PointPurchaseService;
import com.pemass.service.pemass.poi.PresentService;
import com.pemass.service.pemass.poi.UserConsumeDetailService;
import com.pemass.service.pemass.poi.UserPointDetailService;
import com.pemass.service.pemass.sys.AuthService;
import com.pemass.service.pemass.sys.BankCardService;
import com.pemass.service.pemass.sys.CompanyService;
import com.pemass.service.pemass.sys.ConfigService;
import com.pemass.service.pemass.sys.OrganizationService;
import com.pemass.service.pemass.sys.TerminalUserService;
import com.pemass.service.pemass.sys.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @Description: OrderServiceImpl
 * @Author: pokl.huang
 * @CreateTime: 2014-11-18 10:41
 */
@Service
public class OrderServiceImpl implements OrderService {

    private Log logger = LogFactory.getLog(OrderServiceImpl.class);

    @Resource
    private OrderDao orderDao;

    @Resource
    private BaseDao jpaBaseDao;

    @Resource
    private UserService userService;

    @Resource
    private ProductService productService;

    @Resource
    private TerminalUserService terminalUserService;

    @Resource
    private SmsMessageService smsMessageService;

    @Resource
    private PresentService presentService;

    @Resource
    private SiteService siteService;

    @Resource
    private ProductSnapshotService productSnapshotService;

    @Resource
    private SequenceService sequenceService;

    @Resource
    private OrderItemService orderItemService;

    @Resource
    private OrderTicketService orderTicketService;

    @Resource
    private CollectMoneyStrategyService collectMoneyStrategyService;

    @Resource
    private CollectMoneySchemeService collectMoneySchemeService;

    @Resource
    private OrganizationPointDetailService organizationPointDetailService;

    @Resource
    private OrganizationService organizationService;

    @Resource
    private UserPointDetailService userPointDetailService;

    @Resource
    private PaymentService paymentService;

    @Resource
    private PointPurchaseService pointPurchaseService;

    @Resource
    private OrganizationConsumeDetailService organizationConsumeDetailService;

    @Resource
    private OnePointDetailService onePointDetailService;

    @Resource
    private OnePointConsumeDetailService onePointConsumeDetailService;

    @Resource
    private ConfigService configService;

    @Resource
    private AuthService authService;

    @Resource
    private UserConsumeDetailService userConsumeDetailService;

    @Resource
    private PointPoolService pointPoolService;

    @Resource
    private ProductCategoryService productCategoryService;

    @Resource
    private CollectMoneyStrategySnapshotService collectMoneyStrategySnapshotService;

    @Resource
    private Producer pushProducer;

    @Resource
    private Producer clearingProducer;

    @Resource
    private BankCardService bankCardService;

    @Resource
    private CompanyService companyService;


    @Override
    public void saveOrder(Order order) {
        Preconditions.checkNotNull(order);
        orderDao.persist(order);
    }

    @Override
    public Order updateOrder(Order order) {
        Preconditions.checkNotNull(order);
        return orderDao.merge(order);
    }

    @Override
    public void updateOrderClearingStatus(Long orderId) {
        orderDao.updateOrderClearingStatus(orderId);
    }

    @Override
    public DomainPage<Order> getOrder(Map<String, Object> fieldNameValueMap, long pageIndex, long pageSize) {
        OrderStatusEnum orderStatus = OrderStatusEnum.CONFIRMED;//已确认
        PayStatusEnum payStatus = PayStatusEnum.HAS_PAY;//已支付
        String orderIdentifier = (String) fieldNameValueMap.get("orderIdentifier");//单号
        Date startTime = (Date) fieldNameValueMap.get("startTime");//payTime 支付时间
        Date endTime = (Date) fieldNameValueMap.get("endTime");//payTime 支付时间

        /** 获取满足条件的结果 **/
        List<Expression> expressions = Lists.newArrayList();
        Expression orderStatusExpression = new Expression("orderStatus", Operation.Equal, orderStatus);
        Expression payStatusExpression = new Expression("payStatus", Operation.Equal, payStatus);
        expressions.add(orderStatusExpression);
        expressions.add(payStatusExpression);

        if (orderIdentifier != null) {
            Expression orderIdentifierExpression = new Expression("orderIdentifier", Operation.AllLike, "%" + orderIdentifier + "%");
            expressions.add(orderIdentifierExpression);
        }


        if (startTime != null && endTime != null) {
            startTime = new DateTime(startTime).millisOfDay().withMinimumValue().toDate();
            endTime = new DateTime(endTime).millisOfDay().withMaximumValue().toDate();
            Expression startExpression = new Expression("payTime", Operation.GreaterThanEqual, startTime);
            Expression endExpression = new Expression("payTime", Operation.LessThanEqual, endTime);
            expressions.add(startExpression);
            expressions.add(endExpression);
        }

        DomainPage domainPage = jpaBaseDao.getEntitiesPagesByExpressionList(Order.class, expressions, pageIndex, pageSize);//getEntitiesPagesByFieldList(Order.class,fieldNameValueMap,pageIndex,pageSize);
        if (domainPage.getDomains().size() > 0 && domainPage != null) {
            for (int i = 0; i < domainPage.getDomains().size(); i++) {
                Object[] objects = new Object[4];
                objects[0] = domainPage.getDomains().get(i);

                /**获取所属商户*/
                Site site = siteService.getSiteById(((Order) objects[0]).getSiteId());
                Organization organization = organizationService.getOrganizationById(site.getOrganizationId());
                objects[3] = organization;

                //交易金额
                Double transactionAmount = ArithmeticUtil.add(((Order) objects[0]).getTotalPrice(), ((Order) objects[0]).getTotalPointE());
                objects[1] = ArithmeticUtil.round(transactionAmount, 2);
                //商品现价
                Double nowPrice = ArithmeticUtil.sub(((Order) objects[0]).getOriginalPrice(), ((Order) objects[0]).getTotalPointP());
                //E通币实际入账金额
                Double actualAmountRecordedE = ArithmeticUtil.sub(((Order) objects[0]).getTotalPointE(), ((Order) objects[0]).getGivingPointE());
                //营销服务手续费
                Double marketingServicesFee = ArithmeticUtil.mul(nowPrice, organization.getPointRatio());
                //实际入账金额 = E通币实际入账金额 + 营销服务手续费
                Double actualAmountRecorded = ArithmeticUtil.add(actualAmountRecordedE, marketingServicesFee);
                objects[2] = ArithmeticUtil.round(actualAmountRecorded, 3);

                domainPage.getDomains().set(i, objects);
            }
        }
        return domainPage;
    }

    @Override
    public Map<String, Object> selectOrganizationOrder(Map<String, Object> map, long organizationId, long pageIndex, long pageSize) {
        DomainPage domainPage = orderDao.selectOrganizationOrder(map, organizationId, pageIndex, pageSize);
        List<Object[]> list = Lists.newArrayList();
        double totalPrice = 0.0;
        if (domainPage.getDomains() != null && domainPage.getDomains().size() > 0) {
            for (int i = 0; i < domainPage.getDomains().size(); i++) {
                Object[] objects = (Object[]) domainPage.getDomains().get(i);
                Object[] newObjects = new Object[3];
                Order order = jpaBaseDao.getEntityById(Order.class, new BigInteger(objects[0].toString()).longValue());
                Organization organization = jpaBaseDao.getEntityById(Organization.class, new BigInteger(objects[1].toString()).longValue());
                newObjects[0] = order;
                newObjects[1] = computeMoney(order, organization);
                newObjects[2] = organization;
                totalPrice = ArithmeticUtil.add(totalPrice, Double.valueOf(computeMoney(order, organization)));
                list.add(newObjects);
            }
        }
        domainPage.setDomains(list);
        Map<String, Object> couMap = Maps.newHashMap();
        couMap.put("domainPage", domainPage);
        couMap.put("totalPrice", totalPrice);
        return couMap;
    }

    @Override
    public Map<String, Object> getOrderDetail(long orderId, long organizationId) {
        Order order = jpaBaseDao.getEntityById(Order.class, orderId);
        Organization organization = jpaBaseDao.getEntityById(Organization.class, organizationId);
        double presentPrice = ArithmeticUtil.add(order.getTotalPrice(), order.getTotalPointE());//商品现价
        int actualE = order.getTotalPointE() - order.getGivingPointE();//E通币实际入账金额
        double poundage = ArithmeticUtil.mul(presentPrice, organization.getPointRatio());//营销服务手续费
        double income = ArithmeticUtil.sub(actualE, poundage);//实际入账金额
        Map<String, Object> map = Maps.newHashMap();
        map.put("order", order);
        map.put("presentPrice", presentPrice);
        map.put("actualE", actualE);
        map.put("poundage", poundage);
        map.put("income", income);
        return map;
    }

    @Override
    public void givingPoint(Organization organization, Long userId, PointTypeEnum pointType, Integer pointNum) {
        /**1.商户自动认购积分**/
        if (pointType.equals(PointTypeEnum.P)) {
            PointPurchase pointPurchase = new PointPurchase();
            pointPurchase.setOrganizationId(organization.getId());
            pointPurchase.setPointType(PointTypeEnum.P);
            pointPurchase.setAmount(pointNum);
            pointPurchase.setOrganizationId(organization.getId());
            pointPurchase.setPurchaseTime(new Date());
            pointPurchase.setIsAutomatic(1);//自动认购

            pointPurchaseService.insert(pointPurchase);

            /**增加清分明细**/
            this.saveClearing(pointPurchase, organization);

            /**2.商户积分明细增加积分 **/
            OrganizationPointDetail organizationPointDetail = this.addOrganzationPoint(pointPurchase);

            /**3.用户积分明细增加积分**/
            UserPointDetail userPointDetail = new UserPointDetail();
            PointPool pointPool = pointPoolService.getById(pointPurchase.getPointPoolId());
            userPointDetail.setOrganizationId(organization.getId());
            userPointDetail.setAmount(pointPurchase.getAmount());
            userPointDetail.setUseableAmount(pointPurchase.getAmount());
            userPointDetail.setPointType(pointPurchase.getPointType());
            userPointDetail.setArea(pointPurchase.getArea());
            DateTime dateTime = new DateTime();
            userPointDetail.setExpiryTime(dateTime.plusMonths(pointPool.getExpiryPeriod()).millisOfDay().withMaximumValue().toDate());
            userPointDetail.setPointChannel(PointChannelEnum.SYSTEM_LARGESS);//系统赠送
            userPointDetail.setPointPurchaseId(pointPurchase.getId());
            userPointDetail.setUserId(userId);

            userPointDetailService.insert(userPointDetail);

            /**4.商户积分消耗明细**/
            OrganizationConsumeDetail organizationConsumeDetail = new OrganizationConsumeDetail();
            organizationConsumeDetail.setPayableAmount(organizationPointDetail.getAmount());
            organizationConsumeDetail.setAmount(organizationPointDetail.getAmount());
            organizationConsumeDetail.setConsumeType(ConsumeTypeEnum.ACTIVITY_GIFT);
            organizationConsumeDetail.setPointType(organizationPointDetail.getPointType());
            organizationConsumeDetail.setPointPurchaseId(organizationPointDetail.getPointPurchaseId());
            organizationConsumeDetail.setConsumeTargetId(userPointDetail.getId());

            organizationConsumeDetailService.insert(organizationConsumeDetail);

            /**推送消息*/
            PushMessageVO pushMessageVO = new PushMessageVO();
            pushMessageVO.setAudience(userId.toString());
            pushMessageVO.setPushMessageType(PushMessageTypeEnum.GRANT_POINT_FROM_REGISTER);
            pushProducer.send(pushMessageVO);
        }


    }


    private String computeMoney(Order order, Organization organization) {
        double presentPrice = ArithmeticUtil.add(order.getTotalPrice(), order.getTotalPointE());
        int actualE = order.getTotalPointE() - order.getGivingPointE();
        double poundage = ArithmeticUtil.mul(presentPrice, organization.getPointRatio());
        double income = ArithmeticUtil.sub(actualE, poundage);
        DecimalFormat df = new DecimalFormat("###.####");
        return df.format(income);
    }

    @Override
    public Long getHasPayOrderAmountOfDay() {
        return orderDao.getHasPayOrderAmountOfDay();
    }

    @Override
    public Float getHasPayOrderPricesOfDay() {
        return orderDao.getHasPayOrderPricesOfDay();
    }

    @Override
    public Long getHasPayOrderOfDayByField(Integer flag) {
        Preconditions.checkNotNull(flag);
        String field;
        if (1 == flag) field = "total_point_p";
        else if (2 == flag) field = "total_point_e";
        else return 0L;
        return orderDao.getHasPayOrderOfDayByField(field);
    }


    @Transactional
    @Override
    public Order insertProductOrder(String username, Long terminalUserId, Long siteId, Long productId, Integer amount, Double totalPrice, Integer usePointE,
                                    Integer usePointP, Integer usePointO, List<OrderPointPayDetailPojo> orderPointPayDetailPojoList) {
        Product purchaseProduct = productService.getProductInfo(productId);


        /**1.检索用户。*/
        User purchaseUser = userService.getByUsername(username);

        /**2.验证信息*/
        validate(purchaseUser, totalPrice, siteId, purchaseProduct, amount, usePointE, usePointP, usePointO);

        /**3.组装并保存订单*/
        Order order = this.saveProductOrder(totalPrice, amount, purchaseUser.getId(), terminalUserId, siteId, usePointE, usePointP, usePointO, purchaseProduct);

        /**4.生成商品快照和订单项*/
        ProductSnapshot productSnapshot = productSnapshotService.saveSnapshot(purchaseProduct);
        orderItemService.saveOrderItem4Order(order, amount, productSnapshot);

        /**5.冻结用户积分并生成积分消耗记录*/
        for (OrderPointPayDetailPojo orderPointPayDetailPojo : orderPointPayDetailPojoList) {
            orderPointPayDetailPojo.setOrderId(order.getId());
        }
        userPointDetailService.usePoint(orderPointPayDetailPojoList);

        /**推送消息**/
        sendPushMessage(purchaseUser.getId(), order);

        return order;
    }

    /**
     * 订单验证
     *
     * @param purchaseUser
     * @param purchaseProduct
     * @param amount
     * @param usePointE
     * @param usePointP
     * @param usePointO
     */
    private void validate(User purchaseUser, Double totalPrice, Long siteId, Product purchaseProduct, Integer amount, Integer usePointE, Integer usePointP, Integer usePointO) {
        /**1.验证用户**/
        if (purchaseUser == null) {
            throw new BaseBusinessException(SysError.USERNAME_NOT_EXIST);
        }

        /**2.验证商品**/
        if (!purchaseProduct.getProductStatus().equals(2)) {
            throw new BaseBusinessException(BizError.PRODUCT_IS_NOT_EXIST);
        }

        if (!purchaseProduct.getStockNumber().equals(-1) && purchaseProduct.getStockNumber() < amount) {
            throw new BaseBusinessException(BizError.PRODUCT_STOCK_NUMBER_IS_NOT_ENOUGH);
        }

        Site site = siteService.getSiteById(siteId);
        if (!purchaseProduct.getOrganizationId().equals(site.getOrganizationId())) {
            throw new BaseBusinessException(BizError.PRODUCT_NOT_MATCHING_SITE);
        }

        /**3.验证价格是否正确**/
        PointAreaVO pointAreaVO = new PointAreaVO(site.getProvinceId(), site.getCityId(), site.getDistrictId(), siteId, site.getOrganizationId());
        UserPointAggregationPojo userPointAggregation = userPointDetailService.getUserPointAggregation(purchaseUser.getId(), pointAreaVO);
        userPointAggregation.setUseableAmountO(onePointDetailService.getByRemainPointCount(purchaseUser.getId()).intValue());
        userPointAggregation.setGivingUseableAmountO(onePointDetailService.selectPointGivingAmount(purchaseUser.getId(), true).intValue());

        if (purchaseProduct.getDeductionPoint() * amount > userPointAggregation.getUseableAmountP()) {
            //价格变更
            /**变更后价格 = 商品市场价 * 数量 - 用户拥有的E积分数 - 使用的E通币数**/
            Double price = purchaseProduct.getMarketPrice() * amount - userPointAggregation.getUseableAmountP() - usePointE;
            if (usePointP > userPointAggregation.getUseableAmountP()) {
                throw new BaseBusinessException(PoiError.POINT_P_NOT_ENOUGH);
            }

            if (usePointE > userPointAggregation.getUseableAmountE()) {
                throw new BaseBusinessException(PoiError.POINT_E_NOT_ENOUGH);
            }

            if (!price.equals(totalPrice)) {
                throw new BaseBusinessException(EcError.PAYMENT_TOTAL_PRICE_ERROR);
            }
        }
    }

    @Transactional
    @Override
    public Order insertOnePointOrder(String username, Long terminalUserId, Long siteId, Long productId, Integer amount, Double totalPrice, Integer usePointE, Integer usePointP, Integer usePointO, List<OrderPointPayDetailPojo> orderPointPayDetailPojoList) {
        Product purchaseProduct = productService.getProductInfo(productId);
        /**1.验证信息*/
        //        validate(username, site, purchaseProduct, amount, usePointE, usePointP, usePointO);

        /**2.检索用户。*/
        User purchaseUser = userService.getByUsername(username);

        /**3.组装并保存订单*/
        Order order = this.saveOnePointOrder(totalPrice, amount, purchaseUser.getId(), terminalUserId, siteId, usePointE, usePointP, usePointO, purchaseProduct);

        /**4.生成商品快照和订单项*/
        ProductSnapshot productSnapshot = productSnapshotService.saveSnapshot(purchaseProduct);
        orderItemService.saveOrderItem4Order(order, amount, productSnapshot);

        /**5.冻结用户积分并生成积分消耗记录*/
        for (OrderPointPayDetailPojo orderPointPayDetailPojo : orderPointPayDetailPojoList) {
            orderPointPayDetailPojo.setOrderId(order.getId());
        }
        userPointDetailService.usePoint(orderPointPayDetailPojoList);

        /**推送消息**/
        StringBuffer content = new StringBuffer();
        if (order.getTotalPointO() > 0) {
            content.append("壹积分" + order.getTotalPointO() + "个，");
        }
        if (order.getTotalPointE() > 0) {
            content.append("E通币" + order.getTotalPointE() + "个，");
        }
        if (order.getTotalPointP() > 0) {
            content.append("E积分" + order.getTotalPointP() + "个，");
        }

        if (StringUtils.isNotBlank(content.toString())) {
            PushMessageVO pushMessageVO = new PushMessageVO();
            pushMessageVO.setAudience(purchaseUser.getId().toString());
            pushMessageVO.setPushMessageType(PushMessageTypeEnum.FREEZE_POINT);
            List<Object> param = new ArrayList<Object>();
            param.add(content);
            pushMessageVO.setParam(param);
            pushProducer.send(pushMessageVO);
        }

        return order;
    }


    @Override
    public Order insertCustomizationOrder(String username, Double originalPrice, Long organizationId, Long terminalUserId, String externalOrderIdentifier, String remark) {
        Preconditions.checkNotNull(username);
        Preconditions.checkNotNull(originalPrice);
        Preconditions.checkState(terminalUserId != null || organizationId != null, "收银员ID和机构ID不能同时为空");

        Double totalPrice = originalPrice;//实付金额
        //如果用户不存在就注册一个
        boolean isRegister = false;
        User user = userService.getByUsername(username);
        if (user == null) {
            user = new User();
            user.setUsername(username);
            user.setPassword(username);
            authService.register(user);
            isRegister = true;
        }

        TerminalUser terminalUser = null;
        if (terminalUserId != null) {
            terminalUser = terminalUserService.getTerminalById(terminalUserId);
            organizationId = terminalUser.getOrganizationId();
        }

        /** 检索当前机构可用的策略 */
        CollectMoneyStrategy strategy = this.doRetrieveCurrentStrategyByTime(organizationId);

        /** 保存自定义收款策略快照 */
        CollectMoneyStrategySnapshot strategySnapshot = this.doSaveCollectMoneyStrategySnapshot(organizationId, strategy);

        /** 保存自定义订单 */
        Order customizationOrder = this.saveCustomizationOrder(originalPrice, terminalUserId, externalOrderIdentifier, remark, user, terminalUser);

        /** 保存自定义订单项 */
        orderItemService.saveOrderItem4CustomizationOrder(customizationOrder, 1, strategySnapshot);

        UserPointAggregationPojo userPointAggregation = userPointDetailService.getUserPointAggregation(user.getId(), new PointAreaVO(organizationId));
        Integer useablePointE = userPointAggregation.getUseableAmountE();

        /** 如果策略为空，抵扣E通币后直接返回订单信息  */
        if (strategy == null) {
            deductionPointE(organizationId, totalPrice, user, customizationOrder, useablePointE);
            jpaBaseDao.merge(customizationOrder);
            return customizationOrder;
        }

        Integer returnPointE = 0;
        /** 计算折扣 */
        if (strategy.getIsDiscount() == 1) {
            totalPrice = ArithmeticUtil.round(originalPrice * strategy.getDiscount(), 2);//取小数点后两位并转成整数 与客户端保持一致
            Integer totalPointP = ArithmeticUtil.ceil(originalPrice * (1 - strategy.getDiscount()));
            //当E积分不足时,进行价格变动
            Integer useableAmountP = userPointAggregation.getUseableAmountP();
            if (useableAmountP < totalPointP) {
                totalPrice = originalPrice - useableAmountP;
                String pointETaxRate = configService.getConfigByKey(ConfigEnum.POINT_E_TAX_RATE).getValue();
                returnPointE = ArithmeticUtil.round((totalPointP - useableAmountP) * (1 - Double.parseDouble(pointETaxRate)), 2).intValue();//取小数点后两位并转成整数 与客户端保持一致
                totalPointP = useableAmountP;
            }

            customizationOrder.setTotalPrice(totalPrice);
            PointAreaVO pointAreaVO = new PointAreaVO(organizationId);

            userPointDetailService.deductionPointP4CustomizationOrder(user, customizationOrder.getId(), totalPointP, pointAreaVO);
            //设置自定义订单消耗E积分数量
            customizationOrder.setTotalPointP(totalPointP);
        }

        /** 抵扣E通币 */
        if (strategy.getIsDeductionPointE() == 1) {
            deductionPointE(organizationId, totalPrice, user, customizationOrder, useablePointE);
        }

        /** 计算赠送E积分数量 */
        if (strategy.getIsGivingPointP() == 1) {
            customizationOrder.setGivingPointP(this.calculateGivingPoint(originalPrice, strategy.getPointPSchemeId()));
        }

        /** 计算赠送E通币数量 */
        customizationOrder.setGivingPointE(returnPointE);
        if (strategy.getIsGivingPointE() == 1) {
            customizationOrder.setGivingPointE(customizationOrder.getGivingPointE() + this.calculateGivingPoint(originalPrice, strategy.getPointESchemeId()));
        }
        jpaBaseDao.merge(customizationOrder);
        if (isRegister) {
            smsMessageService.append(username, SmsTypeEnum.SYS_REG, new String[]{PemassConst.CLOUDMONEY_LATEST_URL});
        }

        /**推送消息**/
        sendPushMessage(user.getId(), customizationOrder);


        return customizationOrder;

    }

    private void sendPushMessage(Long userId, Order customizationOrder) {
        StringBuffer content = new StringBuffer();
        if (customizationOrder.getTotalPointO() > 0) {
            content.append("壹积分" + customizationOrder.getTotalPointO() + "个,");
        }
        if (customizationOrder.getTotalPointE() > 0) {
            content.append("E通币" + customizationOrder.getTotalPointE() + "个,");
        }
        if (customizationOrder.getTotalPointP() > 0) {
            content.append("E积分" + customizationOrder.getTotalPointP() + "个,");
        }

        if (StringUtils.isNotBlank(content.toString())) {
            PushMessageVO pushMessageVO = new PushMessageVO();
            pushMessageVO.setAudience(userId + "");
            pushMessageVO.setPushMessageType(PushMessageTypeEnum.FREEZE_POINT);
            List<Object> param = new ArrayList<Object>();
            param.add(content.substring(0, content.toString().length() - 1));
            pushMessageVO.setParam(param);
            pushProducer.send(pushMessageVO);
        }
    }

    /**
     * 抵扣E通币
     * <p/>
     * 默认抵扣全部的E通币
     *
     * @param organizationId
     * @param totalPrice
     * @param user
     * @param customizationOrder
     * @param useablePointE
     */
    private void deductionPointE(Long organizationId, Double totalPrice, User user, Order customizationOrder, Integer useablePointE) {
        //默认抵扣全部E通币
        int deductionPointE = ArithmeticUtil.floor(Math.min(totalPrice, useablePointE));
        if (useablePointE != 0) {
            PointAreaVO pointAreaVO = new PointAreaVO(organizationId);
            userPointDetailService.deductionPointE4CustomizationOrder(user, customizationOrder.getId(), deductionPointE, pointAreaVO);
        }
        //实际支付金额为消费金额减去抵扣E通币数量
//        customizationOrder.setTotalPrice(ArithmeticUtil.sub(totalPrice, deductionPointE).floatValue());
        customizationOrder.setTotalPrice(ArithmeticUtil.round(totalPrice - deductionPointE, 2));
        //设置自定义订单消耗E通币数量
        customizationOrder.setTotalPointE(deductionPointE);
    }

    /**
     * 保存策略快照
     *
     * @param organizationId
     * @param strategy
     */
    private CollectMoneyStrategySnapshot doSaveCollectMoneyStrategySnapshot(Long organizationId, CollectMoneyStrategy strategy) {
        if (strategy == null) {
            CollectMoneyStrategy emptyStrategy = new CollectMoneyStrategy();
            emptyStrategy.setStrategyName("无优惠");
            emptyStrategy.setOrganizationId(organizationId);
            emptyStrategy.setIsDiscount(0);
            emptyStrategy.setIsGivingPointE(0);
            emptyStrategy.setIsGivingPointP(0);
            emptyStrategy.setStartTime(new Date());
            emptyStrategy.setEndTime(new Date());


            return collectMoneyStrategySnapshotService.insertFromStrategy(emptyStrategy);
        } else {
            return collectMoneyStrategySnapshotService.insertFromStrategy(strategy);
        }
    }

    /**
     * 根据机构ID检索该机构当前可用策略
     *
     * @param organizationId 策略集合
     * @return null 表示没有可用集合
     */
    private CollectMoneyStrategy doRetrieveCurrentStrategyByTime(Long organizationId) {
        Preconditions.checkNotNull(organizationId);
        List<CollectMoneyStrategy> strategies = collectMoneyStrategyService.selectValidStrategyByOrganizationId(organizationId);
        if (strategies == null || strategies.size() == 0) {
            return null;
        }

        for (CollectMoneyStrategy strategy : strategies) {

            //比较日期
            long now = System.currentTimeMillis();
            long startTime = strategy.getStartTime().getTime();
            long endTime = new DateTime(strategy.getEndTime()).millisOfDay().withMaximumValue().getMillis();
            if (now < startTime || now > endTime) {
                continue;
            }

            //比较时间
            int minuteOfDay = DateTime.now().getMinuteOfDay();
            String executeStartTime = strategy.getExecuteStartTime();
            String executeEndTime = strategy.getExecuteEndTime();
            if (executeStartTime == null || executeEndTime == null) {
                throw new BaseBusinessException(PoiError.STRATEGY_FORMAT_ERROR);
            }
            List<String> startTimeList = Splitter.on(":").omitEmptyStrings().trimResults().splitToList(executeStartTime);
            List<String> endTimeList = Splitter.on(":").omitEmptyStrings().trimResults().splitToList(executeEndTime);
            if (startTimeList.size() != 2 || endTimeList.size() != 2) {
                throw new BaseBusinessException(PoiError.STRATEGY_FORMAT_ERROR);
            }

            int startMinuteOfDay = Integer.parseInt(startTimeList.get(0)) * 60 + Integer.parseInt(startTimeList.get(1));
            int endMinuteOfDay = Integer.parseInt(endTimeList.get(0)) * 60 + Integer.parseInt(endTimeList.get(1));
            if (minuteOfDay > startMinuteOfDay && minuteOfDay < endMinuteOfDay) {
                return strategy;
            }
        }

        return null;
    }

    @Transactional
    @Override
    public Payment payCustomizationOrder(Long orderId, PaymentTypeEnum paymentType,
                                         Long terminalUserId, String externalPaymentIdentifier, String payId, String posSerial, Integer deviceId, Integer isSucceed) {
        Preconditions.checkNotNull(orderId);
        Preconditions.checkNotNull(paymentType);

        Order order = jpaBaseDao.getEntityById(Order.class, orderId);
        if (order.getPayStatus() != PayStatusEnum.NONE_PAY) {
            throw new BaseBusinessException(EcError.ORDER_HAS_PAY);
        }
        if (order.getOrderStatus() != OrderStatusEnum.CONFIRMED) {
            throw new BaseBusinessException(EcError.ORDER_HAS_NOT_CONFIRMED);
        }

        order.setPaymentType(paymentType);  //设置支付类型

        /** 1.更新积分消耗明细 */
        userConsumeDetailService.updateAmount(ConsumeTypeEnum.CUSTOMIZATION, order.getId());

        /** 2.回收积分 */
        this.doRecyclePointE(order);

        /** 3.赠送积分 */
        List<OrderItem> orderItemList = orderItemService.selectByOrderId(orderId);
        Long organizationId = collectMoneyStrategySnapshotService.getById(orderItemList.get(0).getCollectMoneyStrategySnapshotId()).getOrganizationId();
        Organization organization = jpaBaseDao.getEntityById(Organization.class, organizationId);
        this.givingPoint(organization, order);

        /** 4.支付日志表 */
        Payment payment = paymentService.insertOrderPayment(order, paymentType, externalPaymentIdentifier, payId, posSerial, deviceId, 1, isSucceed);

        /** 5.修改订单支付状态 */
        order.setPayStatus(PayStatusEnum.HAS_PAY);
        order.setPayTime(new Date());
        if (terminalUserId != null) {
            TerminalUser terminalUser = jpaBaseDao.getEntityById(TerminalUser.class, terminalUserId);
            order.setCashierUserId(terminalUser.getId());
            Site site = siteService.getSiteById(terminalUser.getSiteId());
            order.setSiteId(site.getId());
        }

        jpaBaseDao.merge(order);

        /**发送支付成功短信**/
        User user = userService.getById(order.getUserId());
        Object[] objects = new Object[1];
        objects[0] = order.getGivingPointE();
        smsMessageService.append(user.getUsername(), SmsTypeEnum.CUSTOM_ORDER_PAY, objects);

        /**推送消息**/
        StringBuffer content = new StringBuffer();
        if (order.getTotalPointO() > 0) {
            content.append("壹积分" + order.getTotalPointO() + "个,");
        }
        if (order.getTotalPointE() > 0) {
            content.append("E通币" + order.getTotalPointE() + "个,");
        }
        if (order.getTotalPointP() > 0) {
            content.append("E积分" + order.getTotalPointP() + "个,");
        }
        if (order.getGivingPointE() + order.getGivingPointP() + order.getGivingPointP() > 0) {
            content.append("获赠");
            if (order.getGivingPointE() > 0) {
                content.append("E通币" + order.getGivingPointE() + "个，");
            }
            if (order.getGivingPointP() > 0) {
                content.append("E积分" + order.getTotalPointP() + "个，");
            }
            if (order.getGivingPointO() > 0) {
                content.append("壹积分" + order.getTotalPointO() + "个，");
            }
        }
        if (StringUtils.isNotBlank(content.toString())) {
            PushMessageVO pushMessageVO = new PushMessageVO();
            pushMessageVO.setAudience(order.getUserId().toString());
            pushMessageVO.setPushMessageType(PushMessageTypeEnum.DEDUCTION_POINT);
            List<Object> param = new ArrayList<Object>();
            param.add(order.getOrderIdentifier());
            param.add(content.substring(0, content.length() - 1));
            pushMessageVO.setParam(param);
            pushProducer.send(pushMessageVO);
        }

        /**清分**/
        ClearingVo vo = new ClearingVo();
        vo.setClearSource(ClearSourceEnum.CUSTOMIZATION_ORDER);
        vo.setSourceTargetId(order.getId());
        clearingProducer.send(vo);

        return payment;
    }

    private Order saveCustomizationOrder(Double originalPrice, Long terminalUserId, String externalOrderIdentifier, String remark, User user, TerminalUser terminalUser) {
        Order customizationOrder = new Order();
        customizationOrder.setOrderIdentifier(sequenceService.obtainSequence(SequenceEnum.CUSTOMIZATION_ORDER));
        customizationOrder.setOrderType(OrderTypeEnum.CUSTOMIZATION_ORDER);
        customizationOrder.setUserId(user.getId());
        customizationOrder.setOriginalPrice(originalPrice);
        customizationOrder.setTotalPrice(originalPrice);
        customizationOrder.setAmount(1);
        customizationOrder.setTotalPointE(0);
        customizationOrder.setTotalPointP(0);
        customizationOrder.setTotalPointO(0);
        customizationOrder.setGivingPointE(0);
        customizationOrder.setGivingPointP(0);
        customizationOrder.setGivingPointO(0);
        customizationOrder.setOrderStatus(OrderStatusEnum.CONFIRMED);
        customizationOrder.setPayStatus(PayStatusEnum.NONE_PAY);
        customizationOrder.setOrderTime(new Date());
        customizationOrder.setCashierUserId(terminalUserId);
        if (terminalUser != null) {
            customizationOrder.setSiteId(terminalUser.getSiteId());
        }
        customizationOrder.setRemark(remark);
        customizationOrder.setExternalOrderIdentifier(externalOrderIdentifier);
        customizationOrder.setBarCode(BarCodeUtil.encode(customizationOrder.getOrderIdentifier()));
        customizationOrder.setClearingStatus(0);

        jpaBaseDao.persist(customizationOrder);
        return customizationOrder;
    }

    /**
     * 根据策略计算赠送积分数量
     * <p/>
     * 策略ID可以知晓赠送E积分还是E通币
     *
     * @param consumeAmount
     * @param schemeId
     */
    private int calculateGivingPoint(Double consumeAmount, Long schemeId) {
        CollectMoneyScheme scheme = collectMoneySchemeService.getById(schemeId);
        switch (scheme.getSchemeType()) {
            case PERCENTAGE:
                int times = (int) (consumeAmount / scheme.getMiniAmount());
                return times * scheme.getMiniGiveAmount();
            case SHOPPING_AMOUNT:
                return ArithmeticUtil.floor(consumeAmount * scheme.getConversionFactor());
            case IMMOBILIZATION:
                return scheme.getImmobilizationPresented();
        }
        return 0;
    }


    @Transactional
    @Override
    public Payment payProductOrder(Long orderId, PaymentTypeEnum paymentType, Long terminalUserId, String externalPaymentIdentifier, String payId, String posSerial, Integer deviceId, Integer isSucceed) {
        Order order = jpaBaseDao.getEntityByField(Order.class, "id", orderId);
        if (order.getPayStatus() != PayStatusEnum.NONE_PAY) {
            throw new BaseBusinessException(EcError.ORDER_HAS_PAY);
        }
        if (order.getOrderStatus() != OrderStatusEnum.CONFIRMED) {
            throw new BaseBusinessException(EcError.ORDER_HAS_NOT_CONFIRMED);
        }

        List<OrderItem> orderItemList = orderItemService.selectByOrderId(orderId);
        OrderItem orderItem = orderItemList.get(0);
        ProductSnapshot productSnapshot = productSnapshotService.getSnapshotById(orderItem.getProductSnapshotId());
        Product purchaseProduct = productService.getProductInfo(productSnapshot.getProductId());
        Organization organization = organizationService.getOrganizationById(purchaseProduct.getOrganizationId());

        /**1.更新用户积分消耗明细的实付积分数*/
        this.deductUserPoint(order, purchaseProduct);

        /**2.修改商户积分可用数量 并将积分赠送给用户 此步骤在消费时完成*/
        this.givingPoint(organization, order);

        /**3.出票 生成票码和二维码*/
        ProductCategory productCategory = productCategoryService.getProductCategoryById(purchaseProduct.getProductCategoryId());
        if (productCategory.getIsCreateTicket().equals(1)) {
            this.saveOrderTicket(order, productSnapshot);
        }


        /**4.修改商品信息*/
        productService.updateProductStockNumber(purchaseProduct, order.getAmount());
        productService.updateProductSaleNumber(purchaseProduct, order.getAmount());

        /**5.支付日志表*/
        order.setPaymentType(paymentType);
        Payment payment = paymentService.insertOrderPayment(order, paymentType, externalPaymentIdentifier, payId, posSerial, deviceId, 0, isSucceed);

        /**6.修改订单支付状态*/
        order.setPayStatus(PayStatusEnum.HAS_PAY);
        order.setPayTime(new Date());
        if (terminalUserId != null) {
            order.setCashierUserId(terminalUserId);
        }
        jpaBaseDao.merge(order);

        /** 对生成的订单清分**/
        ClearingVo vo = new ClearingVo();
        vo.setClearSource(ClearSourceEnum.PRODUCT_ORDER);
        vo.setSourceTargetId(order.getId());
        clearingProducer.send(vo);

        /**发送支付成功短信**/
        List<OrderTicket> orderTicketList = orderTicketService.getOrderTicketsByOrderId(orderId);
        User user = userService.getById(order.getUserId());
        if (orderTicketList.size() > 0) {
            /**发送虚拟商品短信**/
            Object[] params = new Object[3];
            params[0] = productSnapshot.getProductName();
            params[1] = new DateTime(orderTicketList.get(0).getExpiryTime()).toString("yyyy-MM-dd");
            params[2] = order.getGivingPointE();
            smsMessageService.append(user.getUsername(), SmsTypeEnum.ORDER_SUCCESS, params);
        } else {
            /**发送实体商品短信**/
            Object[] params = new Object[2];
            params[0] = productSnapshot.getProductName();
            params[1] = order.getGivingPointE();
            smsMessageService.append(user.getUsername(), SmsTypeEnum.ORDER_SUCCESS_AF, params);
        }


        /**推送消息**/
        StringBuffer content = new StringBuffer();
        if (order.getTotalPointO() > 0) {
            content.append("壹积分" + order.getTotalPointO() + "个，");
        }
        if (order.getTotalPointE() > 0) {
            content.append("E通币" + order.getTotalPointE() + "个，");
        }
        if (order.getTotalPointP() > 0) {
            content.append("E积分" + order.getTotalPointP() + "个，");
        }
        if (order.getGivingPointE() + order.getGivingPointP() + order.getGivingPointP() > 0) {
            content.append("获赠");
            if (order.getGivingPointE() > 0) {
                content.append("E通币" + order.getGivingPointE() + "个，");
            }
            if (order.getGivingPointP() > 0) {
                content.append("E积分" + order.getTotalPointP() + "个，");
            }
            if (order.getGivingPointO() > 0) {
                content.append("壹积分" + order.getTotalPointO() + "个，");
            }
        }

        if (StringUtils.isNotBlank(content.toString())) {
            PushMessageVO pushMessageVO = new PushMessageVO();
            pushMessageVO.setAudience(order.getUserId().toString());
            pushMessageVO.setPushMessageType(PushMessageTypeEnum.DEDUCTION_POINT);
            List<Object> param = new ArrayList<Object>();
            param.add(order.getOrderIdentifier());
            param.add(content);
            pushMessageVO.setParam(param);
            pushProducer.send(pushMessageVO);
        }


        return payment;
    }

    private void saveOrderTicket(Order order, ProductSnapshot productSnapshot) {
        for (int i = 0; i < order.getAmount(); i++) {
            OrderTicket orderTicket = new OrderTicket();
            orderTicket.setOrderId(order.getId());
            orderTicket.setProductSnapshotId(productSnapshot.getId());
            orderTicket.setUserId(order.getUserId());
            orderTicket.setIsShared(0);
            orderTicket.setTicketCode(orderTicketService.generateTicketCode());
            orderTicket.setQrCode(QRCodeUtil.encodeQRCode(orderTicket.getTicketCode()));
            orderTicket.setUseStatus(OrderItemStatusEnum.UNUSED);
            Product product = productService.getProductInfo(productSnapshot.getProductId());
            if (product.getIsTimePeriod().equals(0)) {
                orderTicket.setExpiryTime(product.getExpiryPointTime());
            } else {
                DateTime dateTime = new DateTime();
                orderTicket.setExpiryTime(dateTime.plusDays(product.getExpiryPeriod()).millisOfDay().withMaximumValue().toDate());
            }

            orderTicketService.saveOrderTicket(orderTicket);
        }

    }

    private void deductUserPoint(Order order, Product purchaseProduct) {
        /** 使用E积分购买 */
        if (order.getTotalPointO() == null || order.getTotalPointO() == 0) {
            userConsumeDetailService.updateAmount(ConsumeTypeEnum.ORDER, order.getId());
            /*-- 积分回收 --*/
            this.doRecyclePointE(order);
        }
        /** 使用一元购积分购买 */
        else {
            List<OnePointConsumeDetail> onePointConsumeDetailList = onePointConsumeDetailService.selectByOrderId(order.getId());
            Double sum = 0D;
            /**将收益最终平分**/
            int i = 0;
            for (; i < onePointConsumeDetailList.size() - 1; i++) {
                OnePointConsumeDetail onePointConsumeDetail = onePointConsumeDetailList.get(i);
                onePointConsumeDetail.setAmount(onePointConsumeDetail.getPayableAmount());
                /**保留两位小数**/
                onePointConsumeDetail.setProfit(ArithmeticUtil.ceil(purchaseProduct.getCashbackRatio() * purchaseProduct.getOutPrice() * onePointConsumeDetail.getAmount() / order.getTotalPointO(), 4));
                sum += onePointConsumeDetail.getProfit();
                jpaBaseDao.merge(onePointConsumeDetail);
            }

            OnePointConsumeDetail onePointConsumeDetail = onePointConsumeDetailList.get(i);
            onePointConsumeDetail.setAmount(onePointConsumeDetail.getPayableAmount());
            onePointConsumeDetail.setProfit(purchaseProduct.getCashbackRatio() * purchaseProduct.getOutPrice() - sum);
            jpaBaseDao.merge(onePointConsumeDetail);
            /*-- 积分回收 --*/
            for (OnePointConsumeDetail onePointConsumeDetail1 : onePointConsumeDetailList) {
                pointPoolService.recyclePointO(onePointConsumeDetail1);
            }
        }
    }

    private void doRecyclePointE(Order order) {
        ConsumeTypeEnum consumeType = ConsumeTypeEnum.ORDER;
        if (order.getOrderType() == OrderTypeEnum.CUSTOMIZATION_ORDER) {
            consumeType = ConsumeTypeEnum.CUSTOMIZATION;
        }
        List<UserConsumeDetail> userConsumeDetailList = userConsumeDetailService.selectByConsumeTypeAndTargetId(consumeType, order.getId());
        for (UserConsumeDetail userConsumeDetail : userConsumeDetailList) {
            UserPointDetail userPointDetail = userPointDetailService.getById(userConsumeDetail.getUserPointDetailId());
            PointPurchase pointPurchase = pointPurchaseService.getById(userPointDetail.getPointPurchaseId());
            pointPoolService.recyclePointE(pointPurchase.getPointPoolId(), userConsumeDetail.getPayableAmount());
        }
    }


    private void givingPoint(Organization organization, Order order) {
        /**1.商户自动认购积分**/
        if (!order.getGivingPointE().equals(0)) {
            PointPurchase pointPurchase = new PointPurchase();
            pointPurchase.setOrganizationId(organization.getId());
            pointPurchase.setPointType(PointTypeEnum.E);
            pointPurchase.setAmount(order.getGivingPointE());
            pointPurchase.setOrganizationId(organization.getId());
            pointPurchase.setPurchaseTime(new Date());
            pointPurchase.setIsAutomatic(1);//自动认购

            pointPurchaseService.insert(pointPurchase);

            /**增加清分明细**/
            this.saveClearing(pointPurchase, organization);

            /**2.商户积分明细增加积分 **/
            OrganizationPointDetail organizationPointDetail = this.addOrganzationPoint(pointPurchase);

            /**3.用户积分明细增加积分**/
            this.insertUserPointDetail(organization, pointPurchase, order.getUserId(), order.getId());

            /**4.商户积分消耗明细**/
            this.addOrganzationConsumeDetail(organizationPointDetail, order);
        }
        if (!order.getGivingPointO().equals(0)) {
            PointPurchase pointPurchase = new PointPurchase();
            pointPurchase.setOrganizationId(organization.getId());
            pointPurchase.setPointType(PointTypeEnum.O);
            pointPurchase.setAmount(order.getGivingPointO());
            pointPurchase.setOrganizationId(organization.getId());
            pointPurchase.setPurchaseTime(new Date());
            pointPurchase.setIsAutomatic(1);//自动认购

            pointPurchaseService.insert(pointPurchase);

            /**2.商户积分明细增加积分 **/
            OrganizationPointDetail organizationPointDetail = this.addOrganzationPoint(pointPurchase);

            /**3.用户积分明细增加积分**/
            this.insertUserPointDetail(organization, pointPurchase, order.getUserId(), order.getId());

            /**4.商户积分消耗明细**/
            this.addOrganzationConsumeDetail(organizationPointDetail, order);
        }
        if (!order.getGivingPointP().equals(0)) {
            PointPurchase pointPurchase = new PointPurchase();
            pointPurchase.setOrganizationId(organization.getId());
            pointPurchase.setPointType(PointTypeEnum.P);
            pointPurchase.setAmount(order.getGivingPointP());
            pointPurchase.setOrganizationId(organization.getId());
            pointPurchase.setIsAutomatic(1);//自动认购

            pointPurchaseService.insert(pointPurchase);

            /**2.商户积分明细增加积分 **/
            OrganizationPointDetail organizationPointDetail = this.addOrganzationPoint(pointPurchase);

            /**3.用户积分明细增加积分**/
            this.insertUserPointDetail(organization, pointPurchase, order.getUserId(), order.getId());

            /**4.商户积分消耗明细**/
            this.addOrganzationConsumeDetail(organizationPointDetail, order);
        }

    }

    private void saveClearing(PointPurchase pointPurchase, Organization organization) {
        int flag = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        Clearing clearing = new Clearing();
        clearing.setClearSource(ClearSourceEnum.POINT_PURCHASE);
        clearing.setSourceTargetId(pointPurchase.getId());
        BankCard outgoBankCard = bankCardService.getBankCardByTargetUUID(organization.getUuid());
        if (outgoBankCard == null) {
            flag = 3;
            clearing.setOutgoBankCardId(0L);
        } else {
            clearing.setOutgoBankCardId(outgoBankCard.getId());
        }
        Company totalCompany = companyService.getGroupCompanyInfo();
        BankCard incomeBankCard = bankCardService.getBankCardByTargetUUID(totalCompany.getUuid());
        if (incomeBankCard == null) {
            flag = 3;
            clearing.setIncomeBankCardId(0L);
        } else {
            clearing.setIncomeBankCardId(incomeBankCard.getId());
        }
        clearing.setAmount(pointPurchase.getAmount().doubleValue());//金额为认购的E通币数量
        clearing.setIsSettle(flag);
        clearing.setSettlementRole(SettlementRoleEnum.POINT_E_PRINCIPAL);
        clearing.setSettlementRoleTargetId(organization.getId());
        clearing.setProvinceId(organization.getProvinceId());
        clearing.setCityId(organization.getCityId());
        clearing.setDistrictId(organization.getDistrictId());
        clearing.setTargetAmount(-pointPurchase.getAmount().doubleValue());
        clearing.setTargetBankCardId(outgoBankCard.getId());
        clearing.setSettlementDate(calendar.getTime());
        clearing.setTransactionAccountTypeEnum(TransactionAccountTypeEnum.ORGANIZATION);
        clearing.setSettlementDate(new Date());
        jpaBaseDao.persist(clearing);
    }

    private void addOrganzationConsumeDetail(OrganizationPointDetail organizationPointDetail, Order order) {
        OrganizationConsumeDetail organizationConsumeDetail = new OrganizationConsumeDetail();
        organizationConsumeDetail.setPayableAmount(organizationPointDetail.getAmount());
        organizationConsumeDetail.setAmount(organizationPointDetail.getAmount());
        organizationConsumeDetail.setConsumeType(ConsumeTypeEnum.ORDER);
        organizationConsumeDetail.setPointType(organizationPointDetail.getPointType());
        organizationConsumeDetail.setPointPurchaseId(organizationPointDetail.getPointPurchaseId());
        organizationConsumeDetail.setConsumeTargetId(order.getId());

        organizationConsumeDetailService.insert(organizationConsumeDetail);
    }

    private OrganizationPointDetail addOrganzationPoint(PointPurchase pointPurchase) {
        OrganizationPointDetail organizationPointDetail = new OrganizationPointDetail();
        organizationPointDetail.setAmount(pointPurchase.getAmount());
        organizationPointDetail.setExpiryTime(pointPurchase.getExpiryTime());
        organizationPointDetail.setArea(pointPurchase.getArea());
        organizationPointDetail.setOrganizationId(pointPurchase.getOrganizationId());
        organizationPointDetail.setPointPurchaseId(pointPurchase.getId());
        organizationPointDetail.setPointChannel(PointChannelEnum.ORGANIZATION_SUBSCRIBE);
        organizationPointDetail.setPointType(pointPurchase.getPointType());
        organizationPointDetail.setUseableAmount(0);//直接置0

        organizationPointDetailService.insert(organizationPointDetail);
        return organizationPointDetail;
    }

    private void insertUserPointDetail(Organization organization, PointPurchase pointPurchase, Long userId, Long orderId) {

        if (pointPurchase.getPointType().equals(PointTypeEnum.O)) {
            OnePointDetail onePointDetail = new OnePointDetail();
            onePointDetail.setAmount(pointPurchase.getAmount());
            onePointDetail.setUseableAmount(pointPurchase.getAmount());
            onePointDetail.setBelongUserId(userId);
            DateTime dateTime = new DateTime();
            onePointDetail.setExpiryTime(dateTime.plusMonths(12).millisOfDay().withMaximumValue().toDate());// 一元购自动有效期为12个月
            onePointDetail.setIsClear(false);
            onePointDetail.setUserId(userId);
            onePointDetail.setPointChannelEnum(PointChannelEnum.PURCHASE_GOODS);
            onePointDetail.setOrderId(orderId);

            onePointDetailService.insert(onePointDetail);
        } else {
            UserPointDetail userPointDetail = new UserPointDetail();
            PointPool pointPool = pointPoolService.getById(pointPurchase.getPointPoolId());
            userPointDetail.setOrganizationId(organization.getId());
            userPointDetail.setAmount(pointPurchase.getAmount());
            userPointDetail.setUseableAmount(pointPurchase.getAmount());
            userPointDetail.setPointType(pointPurchase.getPointType());
            userPointDetail.setArea(pointPurchase.getArea());
            DateTime dateTime = new DateTime();
            userPointDetail.setExpiryTime(dateTime.plusMonths(pointPool.getExpiryPeriod()).millisOfDay().withMaximumValue().toDate());
            userPointDetail.setPointChannel(PointChannelEnum.PURCHASE_GOODS);//购买商品所得
            userPointDetail.setPointPurchaseId(pointPurchase.getId());
            userPointDetail.setUserId(userId);

            userPointDetailService.insert(userPointDetail);
        }

    }


    /**
     * 获取销售订单记录
     *
     * @param terminalUserId
     */
    @Override
    public DomainPage getOrderList(String username, Long terminalUserId, Long pageIndex, Long pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();//精确条件

        Map<String, Object> fuzzyMap = new HashMap<String, Object>();//模糊条件
        if (StringUtils.isNotBlank(username)) {
            fuzzyMap.put("username", username);
        }

        Map<String, Object> compound = new HashMap<String, Object>();//其他条件

        List<User> userList = orderDao.getOrderByCompoundConditions(User.class, map, fuzzyMap, compound);

        DomainPage domainPage = new DomainPage();
        if ((userList == null) || (userList.size() == 0)) {
            domainPage.setPageIndex(pageIndex);
            domainPage.setPageSize(pageSize);
            return domainPage;
        }

        domainPage = orderDao.getOrderByUsername(userList, terminalUserId, pageIndex, pageSize);

        if (domainPage == null || domainPage.getDomains() == null || domainPage.getDomains().size() == 0) {
            throw new BaseBusinessException(EcError.ORDER_HAS_NOT_ITEM);
        }
        return domainPage;
    }

    /**
     * 退单
     *
     * @param orderId
     * @return
     */
    @Override
    public Long updateReturn(Long orderId) {
        Order order = this.getById(orderId);
        List<OrderItem> orderItemList = orderItemService.selectByOrderId(orderId);

        Integer amount = orderItemList.size();

        /**1.将订单状态改为失效*/
        order.setOrderStatus(OrderStatusEnum.RETURNED);
        orderDao.merge(order);

        /**2.将订单项状态改为已退货*/
        List<OrderTicket> orderTicketList = orderDao.getEntitiesByField(OrderTicket.class, "orderId", order.getId());

        for (int i = 0; i < orderTicketList.size(); i++) {
            OrderTicket orderTicket = orderTicketList.get(i);
            orderTicket.setUseStatus(OrderItemStatusEnum.RETURNED);
            orderDao.merge(orderTicket);
        }

        /**3.产品库存还原 销量还原*/

        ProductSnapshot snapshot = productSnapshotService.getSnapshotById(orderItemList.get(0).getProductSnapshotId());
        Product purchaseProduct = productService.getProductInfo(snapshot.getProductId());
        updateProductStockNumberReturn(purchaseProduct, amount);
        updateProductSaleNumberReturn(purchaseProduct, amount);

        /**4.退还用户使用积分*/

        return orderId;
    }

    @Override
    public DomainPage<Order> selectOrderByUid(Long uid, Long siteId, OrderStatusEnum orderStatusEnum, OrderTypeEnum orderTypeEnum, PayStatusEnum payStatusEnum, Long pageIndex, Long pageSize) {
        Map param = new HashMap();
        if (uid != null) {
            param.put("userId", uid);
        }
        if (siteId != null) {
            param.put("siteId", siteId);
        }
        if (orderStatusEnum != null) {
            param.put("orderStatus", orderStatusEnum);
        }
        if (payStatusEnum != null) {
            param.put("payStatus", payStatusEnum);
        }
        if (orderTypeEnum != null) {
            param.put("orderType", orderTypeEnum);
        }
        return jpaBaseDao.getEntitiesPagesByFieldList(Order.class, param, pageIndex, pageSize);
    }

    @Override
    public DomainPage getUsersBuyProductByOrganizationId(Long organizationId, long pageIndex, long pageSize) {
        DomainPage domainPage = orderDao.getUsersBuyProductByOrganizationId(organizationId, pageIndex, pageSize);
        DomainPage newDomainPage = this.PosttingDomainPage(domainPage);
        return newDomainPage;
    }

    /**
     * 重新封装
     *
     * @param domainPage
     * @return
     */
    private DomainPage PosttingDomainPage(DomainPage domainPage) {
        List result = new ArrayList();
        String area;
        String urlStr;
        for (Object object : domainPage.getDomains()) {
            Object[] obj = new Object[2];
            Order order = (Order) object;
            User user = jpaBaseDao.getEntityById(User.class, order.getUserId());
            obj[0] = user;
            urlStr = "http://api.map.baidu.com/geocoder/v2/?output=json&ak=P3T7zML1eu48NaVhb6ZweZlG&location="
                    + user.getLatitude() + "," + user.getLongitude();
            area = getUserArea(urlStr);
            obj[1] = area;
            result.add(obj);
        }
        DomainPage newDomainPage = domainPage;
        newDomainPage.getDomains().clear();
        newDomainPage.getDomains().addAll(result);
        return newDomainPage;
    }

    /**
     * 根据url地址获取json格式的字符串，并转换成Map
     *
     * @param urlStr
     * @return
     */
    private String getUserArea(String urlStr) {
        StringBuffer json = new StringBuffer();
        String location = urlStr.substring(urlStr.lastIndexOf("=") + 1);
        String area = null;
        if ("null,null".equals(location)) {
            area = "无区域";
        } else {
            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStreamReader input = new InputStreamReader(conn.getInputStream(), "utf-8");
                Scanner inputStream = new Scanner(input);
                while (inputStream.hasNext()) {
                    json.append(inputStream.nextLine());
                }
                Map<String, Object> results;
                Map<String, Object> maps;
                maps = new ObjectMapper().readValue(json.toString(), Map.class);
                Integer status = (Integer) maps.get("status");
                if (status == 0) {
                    results = (Map<String, Object>) maps.get("result");
                    area = (String) results.get("formatted_address");
                } else {
                    area = "无区域";
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return area;
    }

    @Override
    public Order getByOrderIdentifier(String orderIdentifier) {
        return orderDao.getEntityByField(Order.class, "orderIdentifier", orderIdentifier);
    }

    @Override
    public Order sumOrder(Long terminalUserId, String dateType) {
        return orderDao.sumOrder(terminalUserId, dateType);
    }


    /**
     * 把超过10天已确认未付款的订单状态改为已失效
     *
     * @param orderId
     * @return
     */
    @Override
    public Boolean updateOrderStatus(Long orderId) {
        Order order = orderDao.getEntityById(Order.class, orderId);
        order.setOrderStatus(OrderStatusEnum.INVALID);
        orderDao.merge(order);
        return order.getOrderStatus().equals(OrderStatusEnum.INVALID);
    }

    /**
     * 还原商品库存
     * <p/>
     * ***商品库存只存在于该商品的原始商品中
     *
     * @param purchaseProduct
     * @param amount
     */
    private void updateProductStockNumberReturn(Product purchaseProduct, Integer amount) {
        Product originProduct = productService.getProductInfo(purchaseProduct.getOriginProductId());
        //库存为-1时代表 无库存上限
        if (originProduct.getStockNumber().equals(-1)) {
            //不修改库存
        } else {
            originProduct.setStockNumber(originProduct.getStockNumber() + amount);
            orderDao.merge(originProduct);
        }

    }

    /**
     * 还原商品销售数量
     * <p/>
     * ***需要更新购买商品的父类的销售额字段
     *
     * @param purchaseProduct
     * @param amount
     */
    private void updateProductSaleNumberReturn(Product purchaseProduct, Integer amount) {
        String path = purchaseProduct.getPath();
        String[] paramString = path.split("/");
        int i = 1;//从1开始 paramString【0】为空字符
        List<String> params = new ArrayList<String>();
        while ((!paramString[i].equals(purchaseProduct.getId().toString())) && (i < paramString.length)) {
            params.add(paramString[i]);
            i++;
        }
        params.add(purchaseProduct.getId().toString());

        orderDao.updateProductSaleNumberReturn(Product.class, params, amount);
    }


    @Override
    public Order getById(Long orderId) {
        return orderDao.getEntityById(Order.class, orderId);
    }

    /**
     * 根据用户名获取用户
     * <p/>
     * 当用户名在数据库中不存在时，系统自动为其创建一个用户，并且密码为用户自己的手机号码即username
     *
     * @param username
     * @return
     */
    public User retrieveUser(String username) {
        User user;
        if (userService.hasBeing(username)) {
            user = userService.getByUsername(username);
        } else {
            user = new User();
            user.setUsername(username);
            user.setSalt(UUIDUtil.randomChar(8));
            user.setPassword(username);
            authService.register(user);
            smsMessageService.append(username, SmsTypeEnum.ORDER_VAL_NOREG, null);
        }
        return user;
    }


    @Override
    public List<Order> getOrderByPeriodOfTime(Map<String, Object> fieldNameValueMap) {
        return orderDao.getOrderByPeriodOfTime(Order.class, fieldNameValueMap);
    }

    @Override
    public DomainPage<Order> selectOrderByOnePoint(Long uid, long pageIndex, long pageSize) {
        List<Expression> list = new ArrayList<Expression>();
        list.add(new Expression("userId", Operation.Equal, uid));
        list.add(new Expression("totalPointO", Operation.GreaterThan, Integer.valueOf(0)));
        list.add(new Expression("payStatus", Operation.Equal, PayStatusEnum.HAS_PAY));
        return jpaBaseDao.getEntitiesPagesByExpressionList(Order.class, list, pageIndex, pageSize);
    }

    @Transactional
    @Override
    public Order cancel(Long orderId) {
        Order order = this.getById(orderId);

        /**1.退还用户积分**/
        userPointDetailService.usePointReturn(order);
        onePointDetailService.usePointReturn(order);

        /**2.修改订单状态 置为取消**/
        order.setOrderStatus(OrderStatusEnum.CANCEL);
        order.setUnsubscribeTime(new Date());
        jpaBaseDao.merge(order);

        return order;
    }

    @Override
    public List<Order> selectOrderByFieldList(Map<String, Object> map) {
        return jpaBaseDao.getEntitiesByFieldList(Order.class, map);
    }

    @Override
    public Integer countObligations(Long uid) {
        Map map = new HashMap();
        map.put("userId", uid);
        map.put("orderStatus", OrderStatusEnum.CONFIRMED);
        map.put("payStatus", PayStatusEnum.NONE_PAY);
        map.put("orderType", OrderTypeEnum.PRODUCT_ORDER);
        List<Order> orderList = jpaBaseDao.getEntitiesByFieldList(Order.class, map);
        if (orderList.size() > 0) {
            return orderList.size();
        } else
            return 0;
    }

    @Override
    public Order selectByOrderIdentifier(String orderIdentifier) {

        return jpaBaseDao.getEntityByField(Order.class, "orderIdentifier", orderIdentifier);
    }

    @Override
    public Organization getOrganizationByOrderId(Long orderId, OrderTypeEnum orderType) {
        return orderDao.getOrganizationByOrderId(orderId, orderType);
    }

    @Override
    public List<Order> getIsClearingOrderList() {
        Map map = new HashMap();
        map.put("orderStatus", OrderStatusEnum.CONFIRMED);
        map.put("payStatus", PayStatusEnum.HAS_PAY);
        map.put("clearingStatus", 0);//0表示未清分
        List<Order> orderList = jpaBaseDao.getEntitiesByFieldList(Order.class, map);
        return orderList;
    }

    private void mergeOrder(Order order, OrderPojo orderPojo) {
        MergeUtil.merge(order, orderPojo);
        this.mergeOrderItem(order.getId(), orderPojo);
        this.mergeOrderTicket(order.getId(), orderPojo);
    }

    private void mergeOrderItem(Long orderId, OrderPojo orderPojo) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("orderId", orderId);
        List<OrderItem> orderItemList = orderItemService.getOrderItemList(param);

        List<OrderItemPojo> orderItemPojos = Lists.newArrayList();
        for (OrderItem orderItem : orderItemList) {

            ProductSnapshotPojo snapshotPojo = null;
            CollectMoneyStrategySnapshotPojo strategySnapshotPojo = null;
            //Merge ProductSnapshot
            if (orderItem.getProductSnapshotId() != null) {
                ProductSnapshot snapshot = productSnapshotService.getSnapshotById(orderItem.getProductSnapshotId());
                snapshotPojo = new ProductSnapshotPojo();
                MergeUtil.merge(snapshot, snapshotPojo);
            }
            //Merge 自定义收款策略快照
            if (orderItem.getCollectMoneyStrategySnapshotId() != null) {
                CollectMoneyStrategySnapshot _strategySnapshot = collectMoneyStrategySnapshotService.getById(orderItem.getCollectMoneyStrategySnapshotId());
                strategySnapshotPojo = new CollectMoneyStrategySnapshotPojo();
                MergeUtil.merge(_strategySnapshot, strategySnapshotPojo);
            }
            //merge订单详情
            OrderItemPojo orderItemPojo = new OrderItemPojo();
            MergeUtil.merge(orderItem, orderItemPojo);
            orderItemPojo.setProductSnapshotPojo(snapshotPojo);
            orderItemPojo.setCollectMoneyStrategySnapshotPojo(strategySnapshotPojo);
            orderItemPojos.add(orderItemPojo);
        }
        orderPojo.setOrderItemPojoList(orderItemPojos);
    }

    private void mergeOrderTicket(Long orderId, OrderPojo orderPojo) {
        List<OrderTicket> orderTicketList = orderTicketService.getOrderTicketsByOrderId(orderId);

        List<OrderTicketPojo> orderTicketPojoList = null;
        for (OrderTicket orderTicket : orderTicketList) {
            orderTicketPojoList = Lists.newArrayList();
            //merge产品快照
            ProductSnapshot snapshot = productSnapshotService.getSnapshotById(orderTicket.getProductSnapshotId());
            ProductSnapshotPojo snapshotPojo = new ProductSnapshotPojo();
            MergeUtil.merge(snapshot, snapshotPojo);

            OrderTicketPojo orderTicketPojo = new OrderTicketPojo();
            MergeUtil.merge(orderTicket, orderTicketPojo);
            orderTicketPojo.setProductSnapshotPojo(snapshotPojo);
            orderTicketPojoList.add(orderTicketPojo);
        }
        orderPojo.setOrderTicketPojoList(orderTicketPojoList);
    }

    private Order saveProductOrder(Double totalPrice, Integer amount, Long purchaseUserId, Long terminalUserId, Long siteId, Integer usePointE, Integer usePointP, Integer usePointO, Product product) {
        Order order = new Order();
        order.setOrderIdentifier(sequenceService.obtainSequence(SequenceEnum.ORDER));
        order.setOrderType(OrderTypeEnum.PRODUCT_ORDER);
        order.setUserId(purchaseUserId);
        order.setOriginalPrice(product.getMarketPrice() * amount);
        order.setTotalPrice(totalPrice);
        order.setCashbackPrice(product.getCashbackRatio() * product.getOutPrice() * amount);
        order.setAmount(amount);
        order.setTotalPointE(usePointE);
        order.setTotalPointP(usePointP);
        order.setTotalPointO(usePointO);

        order.setGivingPointE(product.getGivingEnumber() * amount + this.rewardPointE(totalPrice, usePointE, product, amount));
        order.setGivingPointP(product.getGivingPnumber() * amount);
        order.setGivingPointO(0);

        order.setOrderStatus(OrderStatusEnum.CONFIRMED);
        order.setPayStatus(PayStatusEnum.NONE_PAY);
        order.setPaymentType(null);
        order.setOrderTime(new Date());
        if (terminalUserId != null) {
            order.setCashierUserId(terminalUserId);
        }
        order.setSiteId(siteId);
        order.setBarCode(BarCodeUtil.encode(order.getOrderIdentifier()));
        order.setClearingStatus(0);

        orderDao.persist(order);
        return order;
    }

    private Order saveOnePointOrder(Double totalPrice, Integer amount, Long purchaseUserId, Long terminalUserId, Long siteId, Integer usePointE, Integer usePointP, Integer usePointO, Product product) {
        Order order = new Order();
        order.setOrderIdentifier(sequenceService.obtainSequence(SequenceEnum.ONE_PURCHASE_ORDER));
        order.setOrderType(OrderTypeEnum.ONE_PURCHASE_ORDER);
        order.setUserId(purchaseUserId);
        order.setOriginalPrice(product.getMarketPrice() * amount);
        order.setTotalPrice(totalPrice);
        order.setCashbackPrice(0D);
        order.setAmount(amount);
        order.setTotalPointE(usePointE);
        order.setTotalPointP(usePointP);
        order.setTotalPointO(usePointO);

        order.setGivingPointE((product.getGivingEnumber() * amount + this.rewardPointE(totalPrice, usePointE, product, amount)));
        order.setGivingPointP(product.getGivingPnumber() * amount);
        Config config = configService.getConfigByKey(ConfigEnum.POINT_O_GIVING_RATIO);
        Float rate = Float.valueOf(config.getValue());
        Double o = Math.ceil(product.getOutPrice() * rate);
        order.setGivingPointO(o.intValue() * amount);

        order.setOrderStatus(OrderStatusEnum.CONFIRMED);
        order.setPayStatus(PayStatusEnum.NONE_PAY);
        order.setPaymentType(null);
        order.setOrderTime(new Date());
        if (terminalUserId != null) {
            order.setCashierUserId(terminalUserId);
        }
        order.setSiteId(siteId);
        order.setBarCode(BarCodeUtil.encode(order.getOrderIdentifier()));
        order.setClearingStatus(0);

        orderDao.persist(order);
        return order;
    }

    /**
     * 积分不足，奖励E通币数量
     *
     * @param totalPrice
     * @param usePointE
     * @param product
     * @return
     */
    private Integer rewardPointE(Double totalPrice, Integer usePointE, Product product, int amount) {
        /** 实付价格 + 用户抵用E通币 - 商品对外售价 * 数量 > 0**/
        if ((totalPrice + usePointE - product.getOutPrice() * amount) > 0) {
            Config config = configService.getConfigByKey(ConfigEnum.POINT_E_TAX_RATE);
            Float tax = 1 - Float.valueOf(config.getValue());
            /** (实付价格 + 用户抵用E通币 - 商品对外售价 * 数量) * 利率 **/
            Double x = (totalPrice + usePointE - product.getOutPrice() * amount) * tax;
            return ArithmeticUtil.floor(x);
        } else {
            return 0;
        }

    }

}

