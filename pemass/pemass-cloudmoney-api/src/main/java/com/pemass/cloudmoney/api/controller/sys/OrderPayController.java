/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.sys;
import com.pemass.service.pemass.sys.OrderPayService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Description: OrderPayControll
 * @Author: lin.shi
 * @CreateTime: 2015-09-22 10:56
 */
@Controller
@RequestMapping("/orderPay")
public class OrderPayController {

    @Resource
    private OrderPayService orderPayService;

    /**
     * 获取付款码
     * @param uid
     * @return
     */
    @RequestMapping(value = "/user/{uid}" , method = RequestMethod.GET)
    @ResponseBody
    public Object getOrderPay(@PathVariable("uid")Long uid){
        return orderPayService.getById(uid);
    }

}