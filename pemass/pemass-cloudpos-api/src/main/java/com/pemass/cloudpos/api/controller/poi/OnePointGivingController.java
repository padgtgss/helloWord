/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudpos.api.controller.poi;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.pojo.poi.OnePointDetailAmountPojo;
import com.pemass.pojo.poi.OnePointDetailGivePojo;
import com.pemass.pojo.poi.OnePointDetailPojo;
import com.pemass.service.pemass.poi.OnePointDetailService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 一圆购积分明细
 * @Author: lin.shi
 * @CreateTime: 2015-06-11 21:14
 */
@Controller
@RequestMapping("/onePointGiving")
public class OnePointGivingController {

    @Resource
    private OnePointDetailService onePointDetailService;


    /**
     * 积分列表
     *
     * @param username
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CASHIER')")
    @RequestMapping(value = "useableAmount/detail", method = RequestMethod.GET)
    @ResponseBody
    public Object onePointSearch(Long uid,Long pageIndex,Long pageSize) {
        DomainPage domainPage = onePointDetailService.selectPointGiveDetailAmount(uid, pageSize, pageIndex);
        List list = new ArrayList();
        for (Object object : domainPage.getDomains()) {
            Object[] temp = (Object[]) object;
            OnePointDetailGivePojo pojo = new OnePointDetailGivePojo();
            pojo.setUserId(Long.valueOf(temp[0].toString()));
            pojo.setUserName(temp[1].toString());

            pojo.setUseableAmount(Long.valueOf(temp[2].toString()));
            list.add(pojo);
        }
        domainPage.setDomains(list);
        return domainPage;
    }

}