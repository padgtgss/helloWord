/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.poi;

import com.google.common.collect.ImmutableMap;
import com.pemass.service.pemass.poi.PointCouponService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


/**
 * @Description: CouponController
 * @Author: 积分券
 * @CreateTime: 2015-06-01 18:03
 */
@Controller
@RequestMapping("/pointCoupon")
public class PointCouponController {

    @Resource
    private PointCouponService couponService;

    /**
     * 积分券充值
     * @return
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Object recharge(String packIdentifier,String cardSecret){
        return ImmutableMap.of("result",couponService.couponRecharge(packIdentifier,cardSecret));
    }
}