/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudpos.api.controller.poi;

import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.persist.domain.jpa.biz.Site;
import com.pemass.persist.domain.jpa.sys.TerminalUser;
import com.pemass.persist.domain.jpa.sys.User;
import com.pemass.persist.domain.vo.PointAreaVO;
import com.pemass.pojo.poi.UserPointAggregationPojo;
import com.pemass.service.exception.SysError;
import com.pemass.service.pemass.biz.SiteService;
import com.pemass.service.pemass.poi.OnePointDetailService;
import com.pemass.service.pemass.poi.UserPointDetailService;
import com.pemass.service.pemass.sys.TerminalUserService;
import com.pemass.service.pemass.sys.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 积分相关
 */
@Controller
@RequestMapping("/pointDetail")
public class PointDetailController {

    @Resource
    private UserService userService;

    @Resource
    private TerminalUserService terminalUserService;

    @Resource
    private OnePointDetailService onePointDetailService;

    @Resource
    private UserPointDetailService userPointDetailService;

    @Resource
    private SiteService siteService;
    /**
     * 获取当前C端用户积分
     *
     * @param username
     * @param terminalUserId
     * @param productId
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CASHIER')")
    @RequestMapping(value = "query", method = RequestMethod.GET,params = {"username","terminalUserId","productId"})
    @ResponseBody
    public Object getPoint(String username, Long terminalUserId, Long productId) {

        User user = userService.getByUsername(username);
        if ( null == user){
            throw new BaseBusinessException(SysError.USER_NOT_EXIST);
        }
        TerminalUser terminalUser = terminalUserService.getTerminalUser(terminalUserId);

        Site site = siteService.getSiteInfo(terminalUser.getSiteId());
        PointAreaVO pointAreaVO = new PointAreaVO(site.getProvinceId(), site.getCityId(), site.getDistrictId(),  terminalUser.getSiteId(), site.getOrganizationId());

        UserPointAggregationPojo userPointAggregation = userPointDetailService.getUserPointAggregation(user.getId(), pointAreaVO);
        userPointAggregation.setUseableAmountO(onePointDetailService.getByRemainPointCount(user.getId()).intValue());
        userPointAggregation.setGivingUseableAmountO(onePointDetailService.selectPointGivingAmount(user.getId(), true).intValue());

        return userPointAggregation;
    }



    /**
     * 获取当前C端用户积分
     *
     * @param username
     * @param terminalUserId
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CASHIER')")
    @RequestMapping(value = "query", method = RequestMethod.GET,params = {"username","terminalUserId"})
    @ResponseBody
    public Object getPoint(String username, Long terminalUserId) {

        User user = userService.getByUsername(username);
        if (null == user) {
            throw new BaseBusinessException(SysError.USER_NOT_EXIST);
        }
        TerminalUser terminalUser = terminalUserService.getTerminalUser(terminalUserId);

        UserPointAggregationPojo  userPointAggregation = userPointDetailService.getUserPointAggregation(user.getId(), new PointAreaVO(terminalUser.getOrganizationId()));
        userPointAggregation.setUseableAmountO(onePointDetailService.getByRemainPointCount(user.getId()).intValue());
        userPointAggregation.setGivingUseableAmountO(onePointDetailService.selectPointGivingAmount(user.getId(), true).intValue());

        return userPointAggregation;
    }


    

}