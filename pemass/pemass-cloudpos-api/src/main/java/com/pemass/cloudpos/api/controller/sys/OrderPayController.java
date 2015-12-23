/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudpos.api.controller.sys;

import com.pemass.persist.domain.jpa.biz.Site;
import com.pemass.persist.domain.jpa.sys.TerminalUser;
import com.pemass.persist.domain.redis.OrderPay;
import com.pemass.persist.domain.vo.PointAreaVO;
import com.pemass.pojo.poi.UserPointAggregationPojo;
import com.pemass.service.pemass.biz.SiteService;
import com.pemass.service.pemass.poi.OnePointDetailService;
import com.pemass.service.pemass.poi.UserPointDetailService;
import com.pemass.service.pemass.sys.OrderPayService;
import com.pemass.service.pemass.sys.TerminalUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Description: OrderPayControll
 * @Author: lin.shi
 * @CreateTime: 2015-09-22 16:23
 */
@Controller
@RequestMapping("/orderPay")
public class OrderPayController {

    @Resource
    private OrderPayService orderPayService;

    @Resource
    private TerminalUserService terminalUserService;

    @Resource
    private OnePointDetailService onePointDetailService;

    @Resource
    private UserPointDetailService userPointDetailService;

    @Resource
    private SiteService siteService;

    @RequestMapping(value = "/validate", method = RequestMethod.GET,params = {"payCode"})
    @ResponseBody
    public Object validate(String payCode,Long terminalUserId,Long productId){
        OrderPay orderPay = orderPayService.validate(payCode);

        TerminalUser terminalUser = terminalUserService.getTerminalUser(terminalUserId);

        Site site = siteService.getSiteInfo(terminalUser.getSiteId());
        PointAreaVO pointAreaVO = new PointAreaVO(site.getProvinceId(), site.getCityId(), site.getDistrictId(),  terminalUser.getSiteId(), site.getOrganizationId());

        UserPointAggregationPojo userPointAggregation = userPointDetailService.getUserPointAggregation(orderPay.getUserId(), pointAreaVO);
        userPointAggregation.setUseableAmountO(onePointDetailService.getByRemainPointCount(orderPay.getUserId()).intValue());
        userPointAggregation.setGivingUseableAmountO(onePointDetailService.selectPointGivingAmount(orderPay.getUserId(), true).intValue());
        userPointAggregation.setUsername(orderPay.getUsername());

        return userPointAggregation;

    }
}