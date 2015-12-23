/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.poi;


import com.pemass.common.core.annotation.Auth;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.poi.PointPool;
import com.pemass.persist.domain.jpa.poi.Present;
import com.pemass.persist.domain.jpa.poi.UserPointDetail;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.enumeration.ConsumeTypeEnum;
import com.pemass.persist.enumeration.PointChannelEnum;
import com.pemass.persist.enumeration.PointTypeEnum;
import com.pemass.pojo.poi.PointDetailPojo;
import com.pemass.pojo.poi.PointPoolPojo;
import com.pemass.pojo.sys.OrganizationPojo;
import com.pemass.service.pemass.poi.PointPoolService;
import com.pemass.service.pemass.poi.PresentService;
import com.pemass.service.pemass.poi.UserConsumeDetailService;
import com.pemass.service.pemass.poi.UserPointDetailService;
import com.pemass.service.pemass.poi.impl.PointPoolOrganizationServiceImpl;
import org.apache.commons.lang.StringUtils;
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
 * @Description: 用户积分明细
 * @Author: zhou hang
 * @CreateTime: 2014-11-25 11:06
 */
@Controller
@RequestMapping("/userPointDetail")
public class UserPointDetailController {

    @Resource
    private UserPointDetailService pointDetailService;

    @Resource
    private PresentService presentService;

    @Resource
    private PointPoolService poolService;

    @Resource
    private UserConsumeDetailService consumeDetailService;

    @Resource
    private PointPoolOrganizationServiceImpl pointPoolOrganizationService;

    /**
     * 通用E积分收支明细
     *
     * @param uid
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Auth(entity = UserPointDetail.class, parameter = "uid")
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/p/general", method = RequestMethod.GET)
    @ResponseBody
    public Object userPointDetail4GeneralP(Long uid, long pageIndex, long pageSize) {
        DomainPage domainPage = pointDetailService.selectGeneralPointPByUserId(uid, pageIndex, pageSize);
        return this.parseUserDetail(domainPage);
    }


    /**
     * 统计当前用户通用E积分总数
     *
     * @param uid
     * @return
     */
    @Auth(entity = UserPointDetail.class, parameter = "uid")
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/p/general/user/{uid}", method = RequestMethod.GET)
    @ResponseBody
    public Object userPointDetail4GeneralP(@PathVariable("uid") Long uid) {
        PointDetailPojo pojo = new PointDetailPojo();
        pojo.setAmount(pointDetailService.getUserPointDetailGeneralPCount(uid));
        pojo.setFreeAmount(consumeDetailService.getCongelationAmount(uid, PointTypeEnum.P, true));
        return pojo;
    }


    /**
     * 定向E积分收支明细
     *
     * @param uid
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Auth(entity = UserPointDetail.class, parameter = "uid")
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/p/direction", method = RequestMethod.GET)
    @ResponseBody
    public Object userPointDetail4DirectionP(Long uid, Long pointPoolId, long pageIndex, long pageSize) {
        DomainPage domainPage = pointDetailService.selectUserDirectionPointDetail(uid, pointPoolId, pageIndex, pageSize);
        return this.parseUserDetail(domainPage);

    }


    /**
     * 定向积分类别查询
     *
     * @return
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/p/direction/list", method = RequestMethod.GET)
    @ResponseBody
    public Object userPointDetailDirectionP() {

        PointPool pool = poolService.getPointPool();
        PointPoolPojo poolPojo = new PointPoolPojo();
        if (pool != null) {
            poolPojo.setId(pool.getId());
            poolPojo.setIssueName(pool.getIssueName());
        }

        return poolPojo;
    }


    /**
     * E通币收支明细
     *
     * @param uid
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Auth(entity = UserPointDetail.class, parameter = "uid")
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/e/general", method = RequestMethod.GET)
    @ResponseBody
    public Object userPointDetail4GeneralE(Long uid, long pageIndex, long pageSize) {
        DomainPage domainPage = pointDetailService.selectGeneralPointEByUserId(uid, pageIndex, pageSize);
        return this.parseUserDetail(domainPage);

    }


    /**
     * 获取所有的E通币
     *
     * @param uid
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Auth(entity = UserPointDetail.class, parameter = "uid")
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/e/general/user/{uid}", method = RequestMethod.GET)
    @ResponseBody
    public Object userPointDetail4GeneralECount(@PathVariable("uid") Long uid) {
        PointDetailPojo pojo = new PointDetailPojo();
        pojo.setAmount(pointDetailService.getUserPointDetailECount(uid));
        pojo.setFreeAmount(consumeDetailService.getCongelationAmount(uid, PointTypeEnum.E, true));
        return pojo;
    }


    /**
     * 获取某种定向积分的可以商户
     *
     * @param pointPoolId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/p/direction/organization", method = RequestMethod.GET)
    @ResponseBody
    public Object directionPUsedOrganization(Long pointPoolId, long pageIndex, long pageSize) {
        DomainPage domainPage = pointPoolOrganizationService.selectAllPointPoolOrganization(pageIndex, pageSize);
        List list = new ArrayList();
        for (Object objects : domainPage.getDomains()) {
            Object[] obj = (Object[]) objects;
            OrganizationPojo pojo = new OrganizationPojo();
            pojo.setOrganizationName(obj[2].toString());
            list.add(pojo);
        }
        domainPage.setDomains(list);
        return domainPage;

    }


    /**
     * 分批次获取当前用户的可用E积分
     *
     * @param uid
     * @param isDirection 是否是获取定向积分
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Auth(entity = UserPointDetail.class, parameter = "uid")
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/p", method = RequestMethod.GET)
    @ResponseBody
    public Object userPointDetailP(Long uid, boolean isDirection, long pageIndex, long pageSize) {
        DomainPage domainPage = pointDetailService.selectUserPointDetailP(uid, isDirection, pageIndex, pageSize);
        List list = new ArrayList();
        PointDetailPojo pojo = null;
        for (Object obj : domainPage.getDomains()) {
            Object[] temp = (Object[]) obj;
            pojo = new PointDetailPojo();
            pojo.setPointPurchaseId(Long.valueOf(temp[0].toString()));
            pojo.setAmount(Integer.valueOf(temp[1].toString()));
            pojo.setExpiryTime((Date) temp[2]);
            list.add(pojo);
        }
        domainPage.setDomains(list);
        return domainPage;
    }


    /**
     * 解析积分收支明细
     *
     * @param domainPage
     * @return
     */
    private DomainPage parseUserDetail(DomainPage domainPage) {
        List list = new ArrayList();
        PointDetailPojo pojo = null;
        for (Object object : domainPage.getDomains()) {
            Object[] detail = (Object[]) object;
            pojo = new PointDetailPojo();
            Integer amount = Integer.valueOf(detail[1].toString());
            if (amount >= 0) {
                //    pojo.setSource(PointChannelEnum.valueOf().getDescription());
                pojo.setPointChannel(PointChannelEnum.valueOf(detail[0].toString()));
                pojo.setExpiryTime((Date) detail[3]);
                if ( pojo.getPointChannel().equals(PointChannelEnum.PRESENT) && detail[4] != null ) {
                    if (StringUtils.isNotBlank(detail[4].toString())) {
                        Present present = presentService.getById(Long.parseLong(detail[4].toString()));
                        if (present != null)
                            pojo.setSourceName(present.getSourceName());
                    }
                }
            } else {
                //      pojo.setSource(ConsumeTypeEnum.valueOf(detail[0].toString()).getDescription());
                pojo.setConsumeType(ConsumeTypeEnum.valueOf(detail[0].toString()));
            }
            pojo.setAmount(amount);
            pojo.setCreateTime((Date) detail[2]);


            list.add(pojo);
        }
        domainPage.setDomains(list);
        return domainPage;
    }

}