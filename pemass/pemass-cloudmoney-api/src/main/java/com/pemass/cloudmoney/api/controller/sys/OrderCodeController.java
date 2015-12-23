/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.sys;

import com.pemass.persist.domain.redis.OrderCode;
import com.pemass.service.pemass.sys.OrderCodeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Description: OrderController
 * @Author: lin.shi
 * @CreateTime: 2015-09-28 12:07
 */
@Controller
@RequestMapping("/orderCode")
public class OrderCodeController {
    @Resource
    private OrderCodeService orderCodeService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Object get(String orderIdentifier){
        return orderCodeService.get(orderIdentifier);
    }




}