/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.poi;


import com.pemass.common.core.annotation.Auth;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.poi.OnePointConsumeDetail;
import com.pemass.pojo.poi.OnePointConsumeDetailPojo;
import com.pemass.service.pemass.poi.OnePointConsumeDetailService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: OnePointConsumeDetailController
 * @Author: lin.shi
 * @CreateTime: 2015-06-11 21:42
 */
@Controller
@RequestMapping("/onePointConsumeDetail")
public class OnePointConsumeDetailController {
    @Resource
    private OnePointConsumeDetailService onePointConsumeDetailService;

    /**
     * 消费记录统计
     *
     * @param uid
     * @return
     */
    @Auth(entity = OnePointConsumeDetail.class,parameter = "uid")
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public Object consumeDetail(Long uid, Long pageSize, Long pageIndex) {
       return onePointConsumeDetailService.consumeDetail(uid,pageIndex,pageSize);
    }


    /**
     * 统计花费壹购积分赚取的金额
     *
     * @param uid
     * @return
     */
    @Auth(entity = OnePointConsumeDetail.class,parameter = "uid")
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/profitCount/{uid}", method = RequestMethod.GET)
    @ResponseBody
    public Object profitCount(@PathVariable("uid") Long uid) {
        Object[] objects = onePointConsumeDetailService.getProfitCount(uid);
        OnePointConsumeDetailPojo pojo = new OnePointConsumeDetailPojo();
        pojo.setMyselfProfitCount(Double.valueOf(objects[0].toString()));
        pojo.setFriendProfitCount(Double.valueOf(objects[1].toString()));
        return pojo;
    }


    /**
     * 花费积分-->还款额赚取详情
     *
     * @param uid
     * @return
     */
    @Auth(entity = OnePointConsumeDetail.class,parameter = "uid")
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/profit/{uid}", method = RequestMethod.GET)
    @ResponseBody
    public Object friendProfitDetails(@PathVariable("uid") Long uid,boolean isYouself, Long pageIndex, Long pageSize) {
        DomainPage domainPage = onePointConsumeDetailService.selectProfitDetails(uid,isYouself, pageIndex, pageSize);
        List<OnePointConsumeDetailPojo> list = new ArrayList<OnePointConsumeDetailPojo>();
        for (Object temp : domainPage.getDomains()) {
            Object[] object = (Object[]) temp;
            OnePointConsumeDetailPojo pojo = new OnePointConsumeDetailPojo();

            pojo.setOrderIdentifier(object[0].toString());
            pojo.setConsumption(Integer.valueOf(object[1].toString()));
            pojo.setProfit(Double.valueOf(object[2].toString()));
            pojo.setCreateTime((Date) object[3]);
            pojo.setUsername(object[4].toString());
            list.add(pojo);
        }
        domainPage.setDomains(list);
        return domainPage;
    }


    /**
     * 帮某一位朋友花费壹购积分详情
     * @param uid
     * @param belongUserName
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Auth(entity = OnePointConsumeDetail.class,parameter = "uid")
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/friend/{username}", method = RequestMethod.GET)
    @ResponseBody
    public Object consume4Friend(Long uid,@PathVariable("username") String belongUserName,long pageIndex,long pageSize){
        DomainPage domainPage = onePointConsumeDetailService.selectForFriendDetail(uid,belongUserName,pageIndex,pageSize);
        List list = new ArrayList();
        OnePointConsumeDetailPojo pojo = null;
        for(Object obj: domainPage.getDomains()){
            Object[] temp = (Object[])obj;
            pojo = new OnePointConsumeDetailPojo();
            pojo.setOrderIdentifier(temp[0].toString());
            pojo.setConsumption(Integer.valueOf(temp[1].toString()));
            pojo.setProfit(Double.valueOf(temp[2].toString()));
            pojo.setCreateTime((Date)temp[3]);
            list.add(pojo);
        }
        domainPage.setDomains(list);
        return domainPage;
    }

}