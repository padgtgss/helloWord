/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudpos.api.controller.sys;

import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.core.pojo.Body;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.domain.jpa.biz.ProductSnapshot;
import com.pemass.persist.domain.jpa.biz.Site;
import com.pemass.persist.domain.jpa.ec.Order;
import com.pemass.persist.domain.jpa.ec.OrderItem;
import com.pemass.persist.domain.jpa.poi.CollectMoneyStrategySnapshot;
import com.pemass.persist.domain.jpa.sys.TerminalUser;
import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.persist.domain.redis.Config;
import com.pemass.persist.enumeration.AccountStatusEnum;
import com.pemass.persist.enumeration.ConfigEnum;
import com.pemass.pojo.biz.ProductSnapshotPojo;
import com.pemass.pojo.biz.SitePojo;
import com.pemass.pojo.ec.OrderItemPojo;
import com.pemass.pojo.ec.OrderPojo;
import com.pemass.pojo.poi.CollectMoneyStrategySnapshotPojo;
import com.pemass.pojo.sys.BodyPojo;
import com.pemass.service.exception.EcError;
import com.pemass.service.exception.SysError;
import com.pemass.service.pemass.biz.ProductSnapshotService;
import com.pemass.service.pemass.biz.SiteService;
import com.pemass.service.pemass.ec.OrderItemService;
import com.pemass.service.pemass.poi.CollectMoneyStrategySnapshotService;
import com.pemass.service.pemass.sys.ConfigService;
import com.pemass.service.pemass.sys.OrderCodeService;
import com.pemass.service.pemass.sys.TerminalUserService;
import com.pemass.service.pemass.sys.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: OrderCodeController
 * @Author: lin.shi
 * @CreateTime: 2015-09-28 17:15
 */
@Controller
@RequestMapping("/orderCode")
public class OrderCodeController {

    @Resource
    private OrderCodeService orderCodeService;

    @Resource
    private SiteService siteService;

    @Resource
    private TerminalUserService terminalUserService;

    @Resource
    private OrderItemService orderItemService;

    @Resource
    private ProductSnapshotService productSnapshotService;

    @Resource
    private ConfigService configService;

    @Resource
    private UserService userService;

    @Resource
    private CollectMoneyStrategySnapshotService collectMoneyStrategySnapshotService;

    @RequestMapping(value ="/validate",method = RequestMethod.GET)
    @ResponseBody
    public Object validate(String code, Long terminalUserId){
        Order order = orderCodeService.validate(code);
        if (order == null) {
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

        return fetchOrderInfoPojoFromOrder(order);
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
}