/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.sys;

import com.pemass.persist.domain.jpa.sys.Account;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.service.pemass.sys.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Description: OrganizationController
 * @Author: zhou hang
 * @CreateTime: 2015-05-26 16:09
 */
@Controller
@RequestMapping("/organization")
public class OrganizationController {

    @Resource
    private AccountService accountService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Object save(Account account, Organization organization) {
        account = accountService.register(account);
//        organization = accountService.updateRegister(account, organization);
        return organization;
    }

}