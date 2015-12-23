/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.poi;

import com.google.common.collect.ImmutableMap;
import com.pemass.common.core.annotation.Auth;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.poi.OnePointDetail;
import com.pemass.pojo.poi.OnePointDetailGivePojo;
import com.pemass.service.pemass.poi.OnePointDetailService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: OnePointGiveController
 * @Author: lin.shi
 * @CreateTime: 2015-06-15 17:05
 */
@Controller
@RequestMapping("/onePointGiving")
public class OnePointGivingController {

    @Resource
    private OnePointDetailService onePointDetailService;

    /**
     * 赠送给朋友的积分余额
     * <p/>
     *
     * @param uid
     * @param isGiven 是否是被赠送（true 积分为朋友赠送的，false为自己送给朋友的）
     * @return
     */
    @Auth(entity = OnePointDetail.class,parameter = "uid")
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/useableAmount", method = RequestMethod.GET)
    @ResponseBody
    public Object pointGiveUseableAmount(Long uid, boolean isGiven) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("amount", onePointDetailService.selectPointGivingAmount(uid, isGiven));
        return map;
    }

    /**
     * 获取一元购积分赠送或被赠送记录 并做消费统计
     *
     * @param uid       当前用户
     * @param isGiven    是否是被朋友赠送
     * @param pageSize
     * @param pageIndex
     * @return
     */
    @Auth(entity = OnePointDetail.class,parameter = "uid")
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public Object pointToFriendDetails(Long uid, boolean isGiven, Long pageSize, Long pageIndex) {
        DomainPage domainPage = onePointDetailService.pointGiveSearch(uid, isGiven, pageIndex, pageSize);
        List<Object[]> list = domainPage.getDomains();
        List<OnePointDetailGivePojo> detailPojos = new ArrayList<OnePointDetailGivePojo>();
        OnePointDetailGivePojo pojo = null;
        for (Object[] obj : list) {
            pojo = new OnePointDetailGivePojo();
            pojo.setId(Long.valueOf(obj[0].toString()));
            pojo.setAmount(Long.valueOf(obj[1].toString()));
            pojo.setUseableAmount(Long.valueOf(obj[2].toString()));
            pojo.setUserName(obj[3].toString());
            pojo.setProfit(Double.valueOf(obj[4].toString()));
            pojo.setCreateTime((Date) obj[5]);
            detailPojos.add(pojo);
        }
        domainPage.setDomains(detailPojos);
        return domainPage;
    }

    /**
     * 赠送积分
     *
     * @param uid      赠送人
     * @param username 被赠送人的手机号
     * @param amount   积分数
     * @return
     */
    @Auth(entity = OnePointDetail.class,parameter = "uid")
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Object givePoint(Long uid, String username, Integer amount) {
        return ImmutableMap.of("result", onePointDetailService.givePoint(uid, username, amount));
    }

    /**
     * 回收积分
     *
     * @param onePointDetailId id
     * @return
     */
   // @Auth(entity = OnePointDetail.class,parameter = "onePointDetailId",fieldName = "id ")
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/{onePointDetailId}/recycle", method = RequestMethod.POST)
    @ResponseBody
    public Object recyclePoint(@PathVariable("onePointDetailId") Long onePointDetailId) {
        return ImmutableMap.of("result", onePointDetailService.pointRecycle(onePointDetailId));
    }

    /**
     * 获取所有赠送详情根据朋友做统计
     * @param uid
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Auth(entity = OnePointDetail.class,parameter = "uid")
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/useableAmount/detail", method = RequestMethod.GET)
    @ResponseBody
    public Object givenDetail( Long uid ,long pageIndex,long pageSize){
        DomainPage domainPage = onePointDetailService.selectPointGiveDetailAmount(uid, pageSize, pageIndex);
        List list = new ArrayList();
        for(Object object : domainPage.getDomains()){
            Object[] temp = (Object[])object;
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