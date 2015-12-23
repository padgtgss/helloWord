/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudpos.api.controller.bas;

import com.google.common.collect.ImmutableMap;
import com.pemass.persist.enumeration.SmsTypeEnum;
import com.pemass.service.pemass.bas.SmsMessageService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Description: DeviceController
 * @Author: zhou hang
 * @CreateTime: 2014-11-10 11:40
 */
@Controller
@RequestMapping("/message")
public class MessageController {
    @Resource
    private SmsMessageService smsMessageService;

    /**
     * 自定义订单
     * 发送短信
     *
     * @param username 手机号码
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CASHIER')")
    @RequestMapping(value = "customizationorder", method = RequestMethod.POST)
    @ResponseBody
    public Object sendMessage(String username) {
        smsMessageService.append(username, SmsTypeEnum.CUSTOM_ORDER_REGED, null);
        return ImmutableMap.of("result", true);
    }



    /**
     * 发送退货短信
     *
     * @param username 手机号码
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CASHIER')")
    @RequestMapping(value = "returnorder", method = RequestMethod.POST)
    @ResponseBody
    public Object returnMessage(String username) {
        smsMessageService.append(username, SmsTypeEnum.ORDER_RETURN, null);

        return ImmutableMap.of("result", true);
    }

}