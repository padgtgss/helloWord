/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.cloudmoney.api.controller.ec;

import com.pemass.persist.domain.jpa.ec.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: PayController
 * @Author: estn.zuo
 * @CreateTime: 2015-06-26 16:43
 */
@Controller
@RequestMapping("pay")
public class PayController {


    /**
     * 获取支付宝支付链接地址
     *
     * @param order
     * @return
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("link")
    @ResponseBody
    public Object getPayLink(Order order) {

        return null;
    }


    @RequestMapping(value = "notify")
    @ResponseBody
    public ResponseEntity<String> notifyFromAlipay(HttpServletRequest request) throws Exception {
        boolean result = true;
        String msg = result ? "success" : "fail";
        return new ResponseEntity<String>(msg, HttpStatus.OK);
    }

}