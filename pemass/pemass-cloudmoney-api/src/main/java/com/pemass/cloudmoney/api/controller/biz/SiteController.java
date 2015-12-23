/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.cloudmoney.api.controller.biz;


import com.pemass.common.core.pojo.Body;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.domain.jpa.biz.Site;
import com.pemass.persist.domain.jpa.poi.CollectMoneyScheme;
import com.pemass.persist.domain.jpa.poi.CollectMoneyStrategy;
import com.pemass.persist.domain.jpa.poi.PointPoolOrganization;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.enumeration.CollectMoneySchemeEnum;
import com.pemass.persist.enumeration.PointTypeEnum;
import com.pemass.pojo.biz.SiteImagePojo;
import com.pemass.pojo.poi.CollectMoneySchemePojo;
import com.pemass.pojo.poi.CollectMoneyStategyPojo;
import com.pemass.pojo.biz.SitePojo;
import com.pemass.pojo.sys.BodyPojo;
import com.pemass.service.pemass.biz.SiteService;
import com.pemass.service.pemass.poi.CollectMoneySchemeService;
import com.pemass.service.pemass.poi.CollectMoneyStrategyService;
import com.pemass.service.pemass.poi.PointPoolOrganizationService;
import com.pemass.service.pemass.sys.OrganizationService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: OrderController
 * @Author: pokl.huang
 * @CreateTime: 2014-11-18 09:40
 */
@Controller
@RequestMapping("/site")
public class SiteController {

    private Log logger = LogFactory.getLog(SiteController.class);

    @Resource
    private SiteService siteService;

    @Resource
    private OrganizationService organizationService;

    @Resource
    private PointPoolOrganizationService pointPoolOrganizationService;

    @Resource
    private CollectMoneyStrategyService collectMoneyStrategyService;

    @Resource
    private CollectMoneySchemeService collectMoneySchemeService;

    /**
     * 按照名称搜索 景区下属的营业点
     * 或者按照productID 搜索
     *
     * @param siteName
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET, params = {"longitude", "latitude"})
    @ResponseBody
    public Object search(String siteName, String siteType, Long cityId, Long districtId, Integer distance,
                         String longitude, String latitude,
                         Long pageIndex, Long pageSize) {

        DomainPage domainPage = siteService.getsiteListBydistance(siteName, siteType, cityId, districtId, distance, longitude, latitude, pageIndex, pageSize);
        List<SitePojo> sitePojoList = new ArrayList<SitePojo>();
        for (int i = 0; i < domainPage.getDomains().size(); i++) {
            SitePojo sitePojo = new SitePojo();
            Object[] obj = (Object[]) domainPage.getDomains().get(i);
            sitePojo.setId(Long.valueOf(obj[0].toString()));
            sitePojo.setSiteName(obj[1].toString());
            sitePojo.setLocation(obj[2].toString());
            sitePojo.setIsOneMerchant(Integer.valueOf(obj[3].toString()));
            sitePojo.setDistance(Double.valueOf(obj[4].toString()));

            Site site = siteService.getSiteInfo(sitePojo.getId());

            sitePojo.setLatitude(site.getLatitude());
            sitePojo.setLongitude(site.getLongitude());//经纬度 方便地图展示
            sitePojo.setSitePhone(site.getSitePhone());

            /**检索当前商户的收款策略*/
            CollectMoneyStrategy collectMoneyStrategy = collectMoneyStrategyService.getCurrentStrategyByOrganizationId(site.getOrganizationId());
            if (collectMoneyStrategy != null) {
                CollectMoneyStategyPojo collectMoneyStrategyPojo = new CollectMoneyStategyPojo();
                collectMoneyStrategyPojo = detailMerge(collectMoneyStrategy);
                sitePojo.setCollectMoneyStategyPojo(collectMoneyStrategyPojo);
            }

            List<String> previewImageList = siteService.getpreviewImageListByUUid(site.getUuid());
            if (previewImageList.size() != 0) {
                sitePojo.setPreviewImage(previewImageList.get(0));
            } else {
                sitePojo.setPreviewImage(null);
            }
            sitePojoList.add(sitePojo);
        }

        domainPage.getDomains().clear();
        domainPage.setDomains(sitePojoList);

        return domainPage;
    }


    /**
     * 按照名称搜索 景区下属的营业点
     * 或者按照productID 搜索
     *
     * @param productId
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET, params = "productId")
    @ResponseBody
    public Object search(Long productId, Long cityId, Long pageIndex, Long pageSize) {

        DomainPage domainPage = new DomainPage();

        domainPage = siteService.getSiteListByproduct(productId, cityId, pageIndex, pageSize);

        List<SitePojo> sitePojoList = new ArrayList<SitePojo>();
        List<Site> siteList = domainPage.getDomains();
        for (int i = 0; i < siteList.size(); i++) {
            SitePojo sitePojo = new SitePojo();
            Site site = siteList.get(i);

            sitePojo.setId(site.getId());
            sitePojo.setSiteName(site.getSiteName());
            sitePojo.setSitePhone(site.getSitePhone());
            sitePojo.setLocation(site.getLocation());
            sitePojo.setDistance(null);
            sitePojo.setSummary(null);
            sitePojo.setPreviewImage(null);
            sitePojo.setContent(null);
            sitePojo.setServiceTime(site.getServiceTime());
            sitePojo.setLatitude(site.getLatitude());
            sitePojo.setLongitude(site.getLongitude());//经纬度 方便地图展示

            sitePojoList.add(sitePojo);

        }
        domainPage.getDomains().clear();
        domainPage.setDomains(sitePojoList);

        return domainPage;
    }


    /**
     * 根据ID获取营业点详情
     *
     * @param siteId
     * @return
     */
    @RequestMapping(value = "/{siteId}", method = RequestMethod.GET)
    @ResponseBody
    public Object getSiteInfoById(@PathVariable("siteId") Long siteId) {
        Site site = siteService.getSiteInfo(siteId);
        Organization organization = organizationService.getOrganizationById(site.getOrganizationId());
        SitePojo sitePojo = new SitePojo();

        List<String> previewImageList = siteService.getpreviewImageListByUUid(site.getUuid());

        sitePojo.setId(site.getId());
        sitePojo.setSiteName(site.getSiteName());
        sitePojo.setSitePhone(site.getSitePhone());
        sitePojo.setLocation(site.getLocation());
        sitePojo.setDistance(null);
        /**是否是返现【加速】商户**/
        sitePojo.setIsOneMerchant(organization.getIsOneMerchant());
        /**是否是特约商户**/
        List<PointPoolOrganization> pointPoolOrganizationList = pointPoolOrganizationService.selectByOrganizationId(organization.getId());
        if (pointPoolOrganizationList != null && pointPoolOrganizationList.size() > 0) {
            sitePojo.setIsSpecial(1);
        } else {
            sitePojo.setIsSpecial(0);
        }
        /**检索当前商户的收款策略*/
        CollectMoneyStrategy collectMoneyStrategy = collectMoneyStrategyService.getCurrentStrategyByOrganizationId(site.getOrganizationId());
        if (collectMoneyStrategy != null) {
            CollectMoneyStategyPojo collectMoneyStategyPojo = new CollectMoneyStategyPojo();
            collectMoneyStategyPojo = detailMerge(collectMoneyStrategy);
            sitePojo.setCollectMoneyStategyPojo(collectMoneyStategyPojo);
        }

        List<BodyPojo> bodyPojoList = new ArrayList<BodyPojo>();
        for (Body body : site.getSummary()) {
            BodyPojo bodyPojo = new BodyPojo();
            MergeUtil.merge(body, bodyPojo);
            bodyPojoList.add(bodyPojo);
        }
        sitePojo.setSummary(bodyPojoList);

        if (previewImageList.size() != 0) {
            sitePojo.setPreviewImage(previewImageList.get(0));
        } else {
            sitePojo.setPreviewImage(null);
        }

        sitePojo.setContent(site.getContent());
        sitePojo.setServiceTime(site.getServiceTime());

        List<SiteImagePojo> siteImagePojoList = new ArrayList<SiteImagePojo>();
        for (int i = 0; i < previewImageList.size(); i++) {
            String url = previewImageList.get(i);
            SiteImagePojo siteImagePojo = new SiteImagePojo();
            siteImagePojo.setUrl(url);
            siteImagePojoList.add(siteImagePojo);
        }
        sitePojo.setSiteImagePojoList(siteImagePojoList);

        return sitePojo;
    }


    /**
     * 封装数据‘策略’详细
     *
     * @return
     */
    private CollectMoneyStategyPojo detailMerge(CollectMoneyStrategy strategy) {
        CollectMoneyStategyPojo moneyStrategy = new CollectMoneyStategyPojo();

        moneyStrategy.setStartTime(strategy.getStartTime());
        moneyStrategy.setEndTime(strategy.getEndTime());
        moneyStrategy.setCollectMoneyDiscount(strategy.getDiscount() == null ? null : strategy.getDiscount());
        moneyStrategy.setPaiDeduction(strategy.getIsDeductionPointP());
        moneyStrategy.setIsBeiDeduction(strategy.getIsDeductionPointE());
        moneyStrategy.setSchemePojoP(schemePojoPMerge(strategy));
        moneyStrategy.setSchemePojoE(schemePojoEMerge(strategy));

        return moneyStrategy;
    }


    /**
     * 解析'派'积分类型方案
     *
     * @param strategy
     * @return
     */
    private CollectMoneySchemePojo schemePojoPMerge(CollectMoneyStrategy strategy) {
        CollectMoneySchemePojo pojo = new CollectMoneySchemePojo();
        CollectMoneyScheme paiCollectMoneyScheme = null;
        if (strategy.getPointPSchemeId() != null) {
            paiCollectMoneyScheme = collectMoneySchemeService.getById(strategy.getPointPSchemeId());
        }
        if (paiCollectMoneyScheme != null && PointTypeEnum.P.equals(paiCollectMoneyScheme.getPointType())) {
            pojo = (show(paiCollectMoneyScheme));
        }
        return pojo;
    }

    /**
     * 解析'贝'积分类型方案
     *
     * @param strategy
     * @return
     */
    private CollectMoneySchemePojo schemePojoEMerge(CollectMoneyStrategy strategy) {
        CollectMoneySchemePojo pojo = new CollectMoneySchemePojo();
        CollectMoneyScheme beiCollectMoneyScheme = null;
        if (strategy.getPointESchemeId() != null) {
            beiCollectMoneyScheme = collectMoneySchemeService.getById(strategy.getPointESchemeId());
        }
        if (beiCollectMoneyScheme != null && PointTypeEnum.E.equals(beiCollectMoneyScheme.getPointType())) {
            pojo = (show(beiCollectMoneyScheme));
        }
        return pojo;
    }

    /**
     * 根据方案封装数据
     *
     * @param moneyScheme
     * @return
     */
    private CollectMoneySchemePojo show(CollectMoneyScheme moneyScheme) {
        CollectMoneySchemePojo schemePojoE = new CollectMoneySchemePojo();

        if (CollectMoneySchemeEnum.SHOPPING_AMOUNT.equals(moneyScheme.getSchemeType())) {
            schemePojoE.setMiniAmount(moneyScheme.getMiniAmount());
            schemePojoE.setMiniGiveAmount(moneyScheme.getMiniGiveAmount());
        }
        if (CollectMoneySchemeEnum.PERCENTAGE.equals(moneyScheme.getSchemeType())) {
            schemePojoE.setConversionFactor(moneyScheme.getConversionFactor());
        }
        if (CollectMoneySchemeEnum.IMMOBILIZATION.equals(moneyScheme.getSchemeType())) {
            schemePojoE.setImmobilizationPresented(moneyScheme.getImmobilizationPresented());
        }
        schemePojoE.setSchemeType(moneyScheme.getSchemeType().toString());
        return schemePojoE;
    }
}

