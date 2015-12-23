/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.ec.impl;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Doubles;
import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.core.util.UUIDUtil;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.dao.ec.OrderTicketDao;
import com.pemass.persist.domain.jpa.biz.Product;
import com.pemass.persist.domain.jpa.biz.ProductSnapshot;
import com.pemass.persist.domain.jpa.ec.OrderTicket;
import com.pemass.persist.domain.jpa.sys.TerminalUser;
import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.persist.enumeration.OrderItemStatusEnum;
import com.pemass.persist.enumeration.SmsTypeEnum;
import com.pemass.service.constant.PemassConst;
import com.pemass.service.exception.BizError;
import com.pemass.service.exception.EcError;
import com.pemass.service.exception.SysError;
import com.pemass.service.pemass.bas.SmsMessageService;
import com.pemass.service.pemass.biz.ProductService;
import com.pemass.service.pemass.biz.ProductSnapshotService;
import com.pemass.service.pemass.ec.OrderTicketService;
import com.pemass.service.pemass.sys.AuthService;
import com.pemass.service.pemass.sys.TerminalUserService;
import com.pemass.service.pemass.sys.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: OrderTicketServiceImpl
 * @Author: pokl.huang
 * @CreateTime: 2015-06-10 14:16
 */
@Service
public class OrderTicketServiceImpl implements OrderTicketService {

    private Log logger = LogFactory.getLog(OrderTicketServiceImpl.class);

    @Resource
    private BaseDao jpaBaseDao;

    @Resource
    private OrderTicketDao orderTicketDao;

    @Resource
    private ProductSnapshotService productSnapshotService;

    @Resource
    private ProductService productService;

    @Resource
    private TerminalUserService terminalUserService;

    @Resource
    private UserService userService;

    @Resource
    private AuthService authService;

    @Resource
    private SmsMessageService smsMessageService;


    @Override
    public void saveOrderTicket(OrderTicket orderTicket) {
        Preconditions.checkNotNull(orderTicket);
        jpaBaseDao.persist(orderTicket);
    }

    @Override
    public List<OrderTicket> getOrderTicketsByOrderId(Long orderId) {
        return jpaBaseDao.getEntitiesByField(OrderTicket.class, "orderId", orderId);
    }

    @Override
    public OrderTicket confirmationTicket(String ticketCode, Long terminalUserId) {
        OrderTicket orderTicket = jpaBaseDao.getEntityByField(OrderTicket.class, "ticketCode", ticketCode);
        if (orderTicket == null) {
            throw new BaseBusinessException(EcError.ORDER_NOT_FOUND);
        }
        TerminalUser terminalUser = jpaBaseDao.getEntityById(TerminalUser.class, terminalUserId);
        /**判断票是否可以检票*/

        ProductSnapshot snapshot = productSnapshotService.getSnapshotById(orderTicket.getProductSnapshotId());
        Product product = productService.getProductInfo(snapshot.getProductId());
        if (!this.hasAuthorityTicket(terminalUser, product)) {
            throw new BaseBusinessException(EcError.CAN_NOT_CHECK_TICKET);
        }
        return orderTicket;
    }

    @Override
    public DomainPage selectTicketInfo(Long ticketerUserId, Long pageSize, Long pageIndex) {
        return orderTicketDao.getEntitiesByPage(OrderTicket.class, "ticketerUserId", ticketerUserId, pageIndex, pageSize);
    }


    /**
     * 检票员是否有权检阅该商品
     *
     * @param ticketer
     * @param product
     * @return
     */
    private boolean hasAuthorityTicket(TerminalUser ticketer, Product product) {

        //当前检票员可以检阅，当前检票员组织下的所有商品
        if (Doubles.compare(ticketer.getOrganizationId(), product.getOrganizationId()) == 0) {
            return true;
        }

        //景区的检票员可以检阅景区的所有商品
        Product originProduct = productService.getProductInfo(product.getOriginProductId());
        if (Doubles.compare(ticketer.getOrganizationId(), originProduct.getOrganizationId()) == 0) {
            return true;
        }

        return false;
    }

    public String generateTicketCode() {
        String ticketcode = null;
        String headcode = "X" + UUIDUtil.randomNumber(11);//X无意义 只为下标从1开始 便于理解

        String validatecode = null;
        Integer result = 0;
        List<Integer> integers = new ArrayList<Integer>();
        integers.add(0);//无意义 只为下标从1开始 便于理解
        for (int i = 1; i <= 11; i++) {
            String s = headcode.charAt(i) + "";
            integers.add(Integer.parseInt(s));
        }
        for (int i = 2; i <= 11; i = i + 2) {
            result = result + integers.get(i);//求偶数位之和
        }
        Integer res_1 = result * 3;//偶数位之和*3
        result = 0;
        for (int i = 3; i <= 11; i = i + 2) {

            result = result + integers.get(i);//求从3开始的奇数位之和
        }
        Integer res_2 = res_1 + result;
        Integer res_3 = 0;

        for (int k = 1; ; k++)//获取大于或者等于res_2的且为10的最小整数倍的数 res_3
        {
            if ((res_3 > res_2) || (res_3 == res_2)) break;
            else res_3 = k * 10;
        }
        result = res_3 - res_2;
        validatecode = result.toString();
        ticketcode = headcode.substring(1) + validatecode;

        //判断票码是否唯一，否则进行生成
        OrderTicket orderTicket = orderTicketDao.getEntityByField(OrderTicket.class, "ticketCode", ticketcode);
        if (orderTicket == null) {
            return ticketcode;
        }
        return generateTicketCode();
    }

    @Override
    public DomainPage selectTickets(Long uid, List<String> orderItemStatusEnums, long pageIndex, long pageSize) {
        return orderTicketDao.selectTickets(uid, orderItemStatusEnums, pageIndex, pageSize);
    }

    @Transactional
    @Override
    public Boolean shareTicket(Long ticketId) {
        OrderTicket orderTicket = jpaBaseDao.getEntityById(OrderTicket.class, ticketId);
        orderTicket.setIsShared(1);
        jpaBaseDao.merge(orderTicket);
        return true;
    }

    @Override
    public List<OrderTicket> selectAllOrderTicket() {
        return jpaBaseDao.getAllEntities(OrderTicket.class);
    }

    @Override
    public void updateOrderTicket(OrderTicket orderTicket) {
        jpaBaseDao.merge(orderTicket);
    }

    @Override
    public OrderTicket getOrderTicketById(Long orderTicketId) {
        return jpaBaseDao.getEntityById(OrderTicket.class, orderTicketId);
    }

    /**
     * 检票
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
        boolean isRegister = false;
        User user;
        if (userService.hasBeing(username)) {
            user = userService.getByUsername(username);
        } else {
            user = new User();
            user.setUsername(username);
            user.setSalt(UUIDUtil.randomChar(8));
            user.setPassword(username);
            authService.register(user);
            isRegister = true;
        }

        orderTicket.setTicketerUserId(terminalUser.getId());
        orderTicket.setUseStatus(OrderItemStatusEnum.USED);
        orderTicket.setUseTime(new Date());
        orderTicket.setUsedUserId(user.getId());

        jpaBaseDao.merge(orderTicket);
        if (isRegister) {
            smsMessageService.append(username, SmsTypeEnum.SYS_REG, new String[]{PemassConst.CLOUDMONEY_LATEST_URL});
        }
        return true;
    }

    @Override
    public OrderTicket getOrderTicketByTicketCode(String ticketCode) {
        return jpaBaseDao.getEntityByField(OrderTicket.class, "ticketCode", ticketCode);
    }


    public void validateTicket(OrderTicket orderTicket) {

        if (!OrderItemStatusEnum.UNUSED.equals(orderTicket.getUseStatus())) {
            throw new BaseBusinessException(EcError.TICKET_HAS_USED);
        }

        if (orderTicket.getExpiryTime().before(new Date())) {
            throw new BaseBusinessException(EcError.ORDER_EXPIRY);
        }

    }
}

