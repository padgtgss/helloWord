/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.poi;

import com.google.common.collect.ImmutableMap;
import com.pemass.common.core.annotation.Auth;
import com.pemass.persist.domain.jpa.biz.Site;
import com.pemass.persist.domain.jpa.poi.UserPointDetail;
import com.pemass.persist.domain.jpa.sys.CreditRecord;
import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.persist.domain.vo.PointAreaVO;
import com.pemass.persist.enumeration.PointTypeEnum;
import com.pemass.pojo.poi.UserPointAggregationPojo;
import com.pemass.service.pemass.biz.SiteService;
import com.pemass.service.pemass.poi.OnePointConsumeDetailService;
import com.pemass.service.pemass.poi.OnePointDetailService;
import com.pemass.service.pemass.poi.UserConsumeDetailService;
import com.pemass.service.pemass.poi.UserPointDetailService;
import com.pemass.service.pemass.sys.CreditRecordService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Description: 我的钱包
 * @Author: zhou hang
 * @CreateTime: 2014-11-25 11:06
 */
@Controller
@RequestMapping("/wallet")
public class WalletController {

    @Resource
    private UserPointDetailService userPointDetailService;

    @Resource
    private OnePointDetailService onePointDetailService;

    @Resource
    private OnePointConsumeDetailService onePointConsumeDetailService;


    @Resource
    private CreditRecordService creditRecordService;

    @Resource
    private UserConsumeDetailService consumeDetailService;

    @Resource
    private SiteService siteService;


    /**
     * 根据用户ID，返回该用户的钱包信息
     *
     * @param uid
     * @return
     */
    @Auth(entity = UserPointDetail.class, parameter = "uid")
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/{uid}", method = RequestMethod.GET)
    @ResponseBody
    public Object selectWallet(@PathVariable("uid") Long uid) {

        UserPointAggregationPojo userPointAggregationPojo = userPointDetailService.getUserPointAggregation(uid, null);

        //壹购积分
        userPointAggregationPojo.setOnePointProfitTotal(onePointConsumeDetailService.getPointCounsumeDetailCount(uid));
        userPointAggregationPojo.setUseableAmountO(onePointDetailService.getByRemainPointCount(uid).intValue());
        userPointAggregationPojo.setGivingUseableAmountO(onePointDetailService.selectPointGivingAmount(uid, true).intValue());

        //授信
        CreditRecord creditRecord = creditRecordService.getByUserId(uid);
        if (creditRecord != null) {
            userPointAggregationPojo.setEndTime(creditRecord.getEndTime());
            userPointAggregationPojo.setAuditStatus(creditRecord.getAuditStatus());
            userPointAggregationPojo.setCreditAmount(creditRecord.getCreditLimit() - creditRecord.getUseableCreditLimit());
        }

        //积分冻结数
        userPointAggregationPojo.setFreezeDirectionAmountP(consumeDetailService.getCongelationAmount(uid, PointTypeEnum.P, false));
        userPointAggregationPojo.setFreezeGeneralAmountP(consumeDetailService.getCongelationAmount(uid, PointTypeEnum.P, true));
        userPointAggregationPojo.setFreezeUseableAmountE(consumeDetailService.getCongelationAmount(uid, PointTypeEnum.E, true));
        userPointAggregationPojo.setFreezeGivingUseableAmountO(onePointConsumeDetailService.getFreeGivingPointAmount(uid).intValue());
        return userPointAggregationPojo;
    }


    /**
     * 获取可用积分
     * @param uid
     * @param siteId
     * @return
     */
    @Auth(entity = UserPointDetail.class, parameter = "uid")
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/useablePoint/{uid}", method = RequestMethod.GET)
    @ResponseBody
    public Object getPointCount(@PathVariable("uid") Long uid, Long siteId) {

        Site site = siteService.getSiteInfo(siteId);
        PointAreaVO pointAreaVO = new PointAreaVO(site.getProvinceId(), site.getCityId(), site.getDistrictId(), siteId, site.getOrganizationId());

        UserPointAggregationPojo userPointAggregation = userPointDetailService.getUserPointAggregation(uid, pointAreaVO);
        userPointAggregation.setUseableAmountO(onePointDetailService.getByRemainPointCount(uid).intValue());
        userPointAggregation.setGivingUseableAmountO(onePointDetailService.selectPointGivingAmount(uid, true).intValue());

        return userPointAggregation;
    }


    /**
     * 一键还款
     *
     * @param uid
     * @return
     */
    @Auth(entity = User.class,parameter = "uid")
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/repayment", method = RequestMethod.POST)
    @ResponseBody
    public Object repayment(Long uid) {

        return ImmutableMap.of("result", onePointDetailService.repayment(uid));
    }
}