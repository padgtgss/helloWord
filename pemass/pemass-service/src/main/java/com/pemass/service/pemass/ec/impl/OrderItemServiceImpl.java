/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.ec.impl;

import com.google.common.base.Preconditions;
import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.core.util.ArithmeticUtil;
import com.pemass.common.core.util.UUIDUtil;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.dao.ec.OrderDao;
import com.pemass.persist.dao.ec.OrderItemDao;
import com.pemass.persist.domain.jpa.biz.Product;
import com.pemass.persist.domain.jpa.biz.ProductSnapshot;
import com.pemass.persist.domain.jpa.ec.Order;
import com.pemass.persist.domain.jpa.ec.OrderItem;
import com.pemass.persist.domain.jpa.ec.OrderTicket;
import com.pemass.persist.domain.jpa.poi.CollectMoneyStrategySnapshot;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.domain.jpa.sys.TerminalUser;
import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.persist.domain.vo.OrderVO;
import com.pemass.persist.enumeration.AccountRoleEnum;
import com.pemass.persist.enumeration.OrderItemStatusEnum;
import com.pemass.persist.enumeration.SmsTypeEnum;
import com.pemass.service.constant.PemassConst;
import com.pemass.service.exception.BizError;
import com.pemass.service.exception.EcError;
import com.pemass.service.exception.SysError;
import com.pemass.service.pemass.bas.SmsMessageService;
import com.pemass.service.pemass.biz.ProductService;
import com.pemass.service.pemass.biz.ProductSnapshotService;
import com.pemass.service.pemass.ec.OrderItemService;
import com.pemass.service.pemass.sys.AuthService;
import com.pemass.service.pemass.sys.OrganizationService;
import com.pemass.service.pemass.sys.TerminalUserService;
import com.pemass.service.pemass.sys.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.*;

/**
 * @Description: OrderItemServiceImpl
 * @Author: zhou hang
 * @CreateTime: 2014-11-19 11:33
 */
@Service
public class OrderItemServiceImpl implements OrderItemService {


    @Resource
    private BaseDao jpaBaseDao;

    @Resource
    private OrderDao orderDao;

    @Resource
    private OrderItemDao orderItemDao;

    @Resource
    private UserService userService;

    @Resource
    private AuthService authService;

    @Resource
    private SmsMessageService smsMessageService;

    @Resource
    private TerminalUserService terminalUserService;

    @Resource
    private OrganizationService organizationService;


    @Resource
    private ProductService productService;


    @Resource
    private ProductSnapshotService productSnapshotService;


    @Override
    public void saveOrderItem(OrderItem orderItem) {
        Preconditions.checkNotNull(orderItem);
        orderItemDao.persist(orderItem);
    }

    @Override
    public OrderItem updateOrderItem(OrderItem source) {
        Preconditions.checkNotNull(source);
        OrderItem targetOrderItem = getById(source.getId());
        MergeUtil.merge(source, targetOrderItem);
        return orderItemDao.merge(targetOrderItem);
    }

    @Override
    public OrderItem getById(Long orderItemId) {
        Preconditions.checkNotNull(orderItemId);
        return orderItemDao.getEntityById(OrderItem.class, orderItemId);
    }
    // ==============================

    /**
     * 判断检票是否可用
     *
     * @param ticketCode     票码
     * @param terminalUserId 检票员id
     */
    @Transactional
    @Override
    public Boolean checkinTicket(String ticketCode, Long terminalUserId, String username) {

        if (terminalUserId == null || "".equals(ticketCode)) {
            throw new BaseBusinessException(SysError.TICKETER_NOT_EXIST);
        }

        OrderTicket orderTicket = jpaBaseDao.getEntityByField(OrderTicket.class, "ticketCode", ticketCode);
        if (orderTicket == null) {
            throw new BaseBusinessException(BizError.PRODUCT_IS_NOT_EXIST);
        }

        TerminalUser terminalUser = terminalUserService.getTerminalUser(terminalUserId);
        if (terminalUser == null) {
            throw new BaseBusinessException(SysError.TERMINAlUSER_NOT_EXIST);
        }
        /**判断 票是否可用*/
        validateTicket(orderTicket);
        User user;
        if (userService.hasBeing(username)) {
            user = userService.getByUsername(username);
        } else {
            user = new User();
            user.setUsername(username);
            user.setSalt(UUIDUtil.randomChar(8));
            user.setPassword(username);
            authService.register(user);
            smsMessageService.append(username, SmsTypeEnum.SYS_REG, new String[]{PemassConst.CLOUDMONEY_LATEST_URL});
        }

        orderTicket.setTicketerUserId(terminalUser.getId());
        orderTicket.setUseStatus(OrderItemStatusEnum.USED);
        orderTicket.setUseTime(new Date());
        orderTicket.setUsedUserId(user.getId());

        orderItemDao.merge(orderTicket);

        return true;
    }


    /**
     * 根据检票员,查询检票记录
     *
     * @param ticketerUserId 检票id
     * @param pageSize       每页显示多少条
     * @param pageIndex      第几页
     * @return
     */
    @Override
    public DomainPage selectTicketInfo(Long ticketerUserId, Long pageSize, Long pageIndex) {
        return jpaBaseDao.getEntitiesByPage(OrderItem.class, "ticketerUser.id", ticketerUserId, pageIndex, pageSize);
    }


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
    @Override
    @SuppressWarnings("unchecked")
    public DomainPage getOrderList(int flag, List<Long> id, String isDistribution, Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, Map<String, Object> compoundFieldNameValueMap, long pageIndex, long pageSize) {
        if (id.size() < 1) id.add(0L);
        DomainPage domainPage = orderDao.getOrderList(Order.class, flag, id, fieldNameValueMap, fuzzyFieldNameValueMap, compoundFieldNameValueMap, pageIndex, pageSize);

        List list = new ArrayList();
        for (int i = 0; i < domainPage.getDomains().size(); i++) {
            Object[] objects = new Object[4];
            if (isDistribution.equals("N")) {
                objects[3] = null;
            } else {
                OrderItem orderItem = (OrderItem) domainPage.getDomains().get(i);
                ProductSnapshot snapshot = productSnapshotService.getSnapshotById(orderItem.getProductSnapshotId());
                Product product = orderDao.getEntityById(Product.class, snapshot.getProductId());
                Product parentProduct = orderDao.getEntityById(Product.class, product.getParentProductId());
                objects[3] = parentProduct;
            }
            objects[0] = domainPage.getDomains().get(i);
            OrderItem orderItem = (OrderItem) domainPage.getDomains().get(i);
            ProductSnapshot snapshot = productSnapshotService.getSnapshotById(orderItem.getProductSnapshotId());
            long orderId = new BigInteger(((OrderItem) objects[0]).getOrderId().toString()).longValue();
            long productId = new BigInteger(snapshot.getProductId().toString()).longValue();
            objects[1] = jpaBaseDao.getEntityById(Order.class, orderId);
            objects[2] = jpaBaseDao.getEntityById(Product.class, productId);
            list.add(objects);
        }
        domainPage.setDomains(list);
        return domainPage;
    }

    @Override
    public DomainPage getOrdersByConditions(Map<String, Object> conditions, DomainPage domainPage) {
        Preconditions.checkNotNull(conditions);
        return orderItemDao.getOrderItemsByConditions(conditions, domainPage);
    }

    @Override
    public DomainPage getCustomizationOrdersByCondition(Map<String, Object> conditions, DomainPage domainPage) {
        Preconditions.checkNotNull(conditions);
        return orderDao.getCustomizationOrdersByConditions(conditions, domainPage);
    }

    @Override
    public List<OrderVO> getCustomizationOrdersByCondition(Map<String, Object> conditions) {
        Preconditions.checkNotNull(conditions);
        return orderDao.getCustomizationOrdersByConditions(conditions);
    }

    @Override
    public List<OrderItem> selectOrderItemWithFieldsByOrderId(Long orderId) {
        return orderItemDao.selectOrderItemWithFieldsByOrderId(orderId);
    }

    /**
     * 根据订单号查询订单明细
     *
     * @param fieldNameValueMap
     * @return
     */
    @Override
    public List<OrderItem> getOrderItemList(Map<String, Object> fieldNameValueMap) {
        return jpaBaseDao.getEntitiesByFieldList(OrderItem.class, fieldNameValueMap);
    }

    @Override
    public List<OrderTicket> selectOrderItemByOrderId(Long orderId, List<OrderItemStatusEnum> itemStatus) {
        return orderItemDao.selectOrderItemByOrderId(orderId, itemStatus);
    }

    @Override
    public Long selectWillOverdueTicket(Long uid) {
        return orderItemDao.selectWillOverdueTicket(uid);
    }

    @Override
    public void updateOrderItem(Long orderItemId) {
        OrderItem orderItem = jpaBaseDao.getEntityById(OrderItem.class, orderItemId);
        //orderItem.setUseStatus(OrderItemStatusEnum.OUT_OF_DATE);
        jpaBaseDao.merge(orderItem);
    }

    @Override
    public List selectThisMonthSalesVolume(Long organizationId) {
        return orderItemDao.selectThisMonthSalesVolume(organizationId);
    }

    @Override
    public Long selectSalesVolume(Long organizationId, List<Long> cashierIds) {
        return orderItemDao.selectSalesVolume(organizationId, cashierIds);
    }

    @Override
    public List<OrderItem> getByIdList(Long uid) {
        return orderItemDao.getEntitiesByIdList(uid);
    }

    @Override
    public List<OrderItem> selectOrderItem(Long orderId) {
        return orderItemDao.getOrderItemList(orderId);
    }

    @Override
    public OrderItem getOrderItemByTicketCode(String ticketCode) {
        return jpaBaseDao.getEntityByField(OrderItem.class, "ticketCode", ticketCode);
    }


    @Override
    public Integer usedTicket(Long terminalUserId) {
        TerminalUser terminalUser = jpaBaseDao.getEntityById(TerminalUser.class, terminalUserId);
        Integer result = 0;
        Organization organization = organizationService.getOrganizationById(terminalUser.getOrganizationId());
        if (organization.getAccountRole().equals(AccountRoleEnum.ROLE_LANDSCAPE)) {//如果是景区下属检票员
            List<Product> orgProductList = jpaBaseDao.getEntitiesByField(Product.class, "organization", organization);

            for (int i = 0; i < orgProductList.size(); i++) {
                List<Product> productList = new ArrayList<Product>();
                productList = jpaBaseDao.getEntitiesByField(Product.class, "originProductId", orgProductList.get(i).getId());
                for (int j = 0; j < productList.size(); j++) {
                    List<OrderItem> orderItemList = new ArrayList<OrderItem>();
                    Map<String, Object> param = new HashMap<String, Object>();
                    param.put("product", productList.get(i));
                    param.put("useStatus", OrderItemStatusEnum.UNUSED);
                    orderItemList = jpaBaseDao.getEntitiesByFieldList(OrderItem.class, param);
                    result = result + orderItemList.size();
                }
            }
            return result;
        } else {
            List<Product> productList = new ArrayList<Product>();
            Organization organizations = organizationService.getOrganizationById(terminalUser.getOrganizationId());
            productList = jpaBaseDao.getEntitiesByField(Product.class, "organization", organizations);
            for (int j = 0; j < productList.size(); j++) {
                List<OrderItem> orderItemList = new ArrayList<OrderItem>();
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("product", productList.get(j));
                param.put("useStatus", OrderItemStatusEnum.UNUSED);
                orderItemList = jpaBaseDao.getEntitiesByFieldList(OrderItem.class, param);
                result = result + orderItemList.size();
            }
            return result;
        }
    }

    @Override
    public Double getDirectProfit(Long organizationId) {
        Double directProfit = 0.0;
        //根据商户id获取商户信息
        Organization organization = jpaBaseDao.getEntityById(Organization.class, organizationId);
        //根据商户id获取该商户有分销关系的收银员id的集合
        List<Long> cashierIds = terminalUserService.getAllCashierIdByField(organizationId, "N");
        if (cashierIds.size() != 0) {
            //根据商户id获取该商户直销订单信息
            List<OrderItem> list = orderItemDao.selectOrderItemByIds(cashierIds);
            //计算利润
            for (OrderItem orderitem : list) {
                ProductSnapshot snapshot = productSnapshotService.getSnapshotById(orderitem.getProductSnapshotId());
                Product product = productService.getProductInfo(snapshot.getProductId());
                Order order = jpaBaseDao.getEntityById(Order.class, orderitem.getId());
                if (organization.getAccountRole() == AccountRoleEnum.ROLE_SUPPLIER) { //如果是供应商
                    //directProfit += (order.getTotalPrice() + order.getTotalPointE()) - ((product.getImportPrice() + 2) * order.getAmount());
                } else if (organization.getAccountRole() == AccountRoleEnum.ROLE_PRIMARY_DISTRIBUTION) {   //如果是分销商
                    if (product.getParentProductId() == 0) {  //如果是分销商自己发布的商品
                        //directProfit += (order.getTotalPrice() + order.getTotalPointE()) - ((product.getImportPrice() - 2) * order.getAmount());
                    } else {  //如果是从供应商分销下来的商品
                        //directProfit += (order.getTotalPrice() + order.getTotalPointE()) - (product.getImportPrice() * order.getAmount());
                    }
                }
            }
        }

        return directProfit;
    }

    @Override
    public Double getDistributionProfit(Long organizationId) {
        Double distributionProfit = 0D;
        //根据商户id获取商户信息
        Organization organization = jpaBaseDao.getEntityById(Organization.class, organizationId);
        //根据商户id获取该商户有分销关系的收银员id的集合
        List<Long> cashierIds = terminalUserService.getAllCashierIdByField(organizationId, "Y");
        if (cashierIds.size() != 0) {
            //根据商户id获取该商户直销订单信息
            List<OrderItem> list = orderItemDao.selectOrderItemByIds(cashierIds);
            //计算利润
            for (OrderItem orderitem : list) {
                Order order = jpaBaseDao.getEntityById(Order.class, orderitem.getId());
                Double totalPrice = ArithmeticUtil.add(order.getTotalPrice(), order.getTotalPointE());
                ProductSnapshot snapshot = productSnapshotService.getSnapshotById(orderitem.getProductSnapshotId());
                Product product = productService.getProductInfo(snapshot.getProductId());
                if (organization.getAccountRole() == AccountRoleEnum.ROLE_SUPPLIER) { //如果是供应商
                    //distributionProfit += (product.getLowerPrice() - product.getImportPrice() - 2) * order.getAmount();
                } else if (organization.getAccountRole() == AccountRoleEnum.ROLE_PRIMARY_DISTRIBUTION) {   //如果是分销商
                    if (product.getParentProductId() == 0) {  //如果是分销商自己发布的商品
                        //distributionProfit += (product.getLowerPrice() - product.getImportPrice() - 2) * order.getAmount();
                    } else {  //如果是从供应商分销下来的商品
                        //distributionProfit += (product.getLowerPrice() - product.getImportPrice()) * order.getAmount();
                    }
                } else if (organization.getAccountRole() == AccountRoleEnum.ROLE_SECOND_DISTRIBUTION) {   //如果是二级分销商
                    distributionProfit += (totalPrice - product.getLowerPrice()) * order.getAmount();
                }
            }
        }
        return distributionProfit;
    }


    @Override
    public DomainPage selectCheckTicketRecordList(Long organizationId, Map<String, Object> dateMap, long pageIndex, long pageSize) {
        return orderItemDao.selectCheckingTicketRecord(organizationId, dateMap, pageIndex, pageSize);
    }


    public void validateTicket(OrderTicket orderTicket) {

        if (!OrderItemStatusEnum.UNUSED.equals(orderTicket.getUseStatus())) {
            throw new BaseBusinessException(EcError.TICKET_HAS_USED);
        }

        if (orderTicket.getExpiryTime().before(new Date())) {
            throw new BaseBusinessException(EcError.ORDER_EXPIRY);
        }

    }

    @Override
    public void saveOrderItem4CustomizationOrder(Order order, Integer amount, CollectMoneyStrategySnapshot strategySnapshot) {
        this.doSaveOrderItem(order, amount, null, strategySnapshot);
    }

    /**
     * 保存订单项
     *
     * @param order
     * @param productSnapshot
     * @param amount
     */
    @Override
    public void saveOrderItem4Order(Order order, Integer amount, ProductSnapshot productSnapshot) {
        this.doSaveOrderItem(order, amount, productSnapshot, null);
    }

    private void doSaveOrderItem(Order order, Integer amount, ProductSnapshot productSnapshot, CollectMoneyStrategySnapshot strategySnapshot) {
        Preconditions.checkNotNull(order);
        Preconditions.checkState(productSnapshot != null || strategySnapshot != null, "商品快照和策略快照不能同时为空");
        //保存订单项
        OrderItem orderItem = new OrderItem();
        orderItem.setAmount(amount);
        orderItem.setOrderId(order.getId());
        orderItem.setOrderIdentifier(order.getOrderIdentifier());
        if (strategySnapshot != null) {
            orderItem.setCollectMoneyStrategySnapshotId(strategySnapshot.getId());
        }
        if (productSnapshot != null) {
            orderItem.setProductSnapshotId(productSnapshot.getId());
        }
        orderItem.setPrice(order.getTotalPrice());
        jpaBaseDao.persist(orderItem);
    }


    @Override
    public List<OrderItem> selectByOrderId(Long orderId) {
        return jpaBaseDao.getEntitiesByField(OrderItem.class, "orderId", orderId);
    }
}