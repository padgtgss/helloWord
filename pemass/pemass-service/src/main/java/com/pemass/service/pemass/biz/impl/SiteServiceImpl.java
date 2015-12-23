package com.pemass.service.pemass.biz.impl;

import com.google.common.collect.Lists;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.domain.BaseDomain;
import com.pemass.common.server.enumeration.AvailableEnum;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.dao.biz.SiteDao;
import com.pemass.persist.dao.poi.PresentPackDao;
import com.pemass.persist.domain.jpa.bas.Province;
import com.pemass.persist.domain.jpa.bas.Resources;
import com.pemass.persist.domain.jpa.biz.Product;
import com.pemass.persist.domain.jpa.biz.Site;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.domain.jpa.sys.TerminalUser;
import com.pemass.service.pemass.biz.ProductService;
import com.pemass.service.pemass.biz.SiteService;
import com.pemass.service.pemass.sys.OrganizationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: SiteServiceImpl
 * @Author: luoc
 * @CreateTime: 2014-10-13 10:50
 */
@Service
public class SiteServiceImpl implements SiteService {

    @Resource
    private BaseDao jpaBaseDao;

    @Resource
    private PresentPackDao presentPackDao;

    @Resource
    private SiteDao siteDao;

    @Resource
    private ProductService productService;

    @Resource
    private OrganizationService organizationService;


    /**
     * 新增营业场所
     *
     * @param site
     */
    @Override
    public Site saveSiteInfo(Site site) {
        jpaBaseDao.persist(site);
        return site;

    }

    /**
     * 获取营业场所集合
     *
     * @param map
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public DomainPage getSiteInfoList(Map map, Map fuzzyMap, long pageIndex, long pageSize) {
        DomainPage domainPage = siteDao.getEntitiesByFieldList(Site.class, map, fuzzyMap, pageIndex, pageSize);
        List<Object[]> list = Lists.newArrayList();
        if (domainPage.getDomains() != null && domainPage.getDomains().size() > 0) {
            for (int i = 0; i < domainPage.getDomains().size(); i++) {
                Object[] objects = new Object[2];
                objects[0] = domainPage.getDomains().get(i);
                objects[1] = organizationService.getOrganizationById(((Site) objects[0]).getOrganizationId());
                list.add(objects);
            }
        }
        domainPage.setDomains(list);
        return domainPage;

    }

    @Override
    public DomainPage getSiteInfoList(Map<String, Object> conditions, Map<String, Object> fuzzyConditions, Map<String, List<Object>> collectionConditions, DomainPage domainPage) {
        DomainPage domainPage1 = presentPackDao.getTheBusinessPremises(conditions, fuzzyConditions, collectionConditions, domainPage.getPageIndex(), domainPage.getPageSize());
        if (domainPage1.getDomains().size() > 0 && domainPage1 != null) {
            for (int i = 0; i < domainPage1.getDomains().size(); i++) {
                Object[] objects = new Object[3];
                objects[0] = domainPage1.getDomains().get(i);
                BigInteger bigInteger1 = new BigInteger(((Site) objects[0]).getProvinceId().toString());
                BigInteger bigInteger2 = new BigInteger(((Site) objects[0]).getOrganizationId().toString());
                long provinceId = bigInteger1.longValue();
                long organizationId = bigInteger2.longValue();
//                Site site = (Site)domainPage1.getDomains().get(i);
//                objects[0]=site;
//                BigInteger bigInteger = new BigInteger(site.getProvinceId().toString());
//                long provinceId = bigInteger.longValue();
                Province province = jpaBaseDao.getEntityById(Province.class, provinceId);
                Organization organization = jpaBaseDao.getEntityById(Organization.class, organizationId);
                objects[1] = province;
//                Organization organization=jpaBaseDao.getEntityById(Organization.class,site.getOrganizationId());
                objects[2] = organization;
                domainPage1.getDomains().set(i, objects);
            }
        }
        return domainPage1;
    }

    /**
     * 获取营业场所资料详情
     *
     * @param siteId
     * @return
     */
    @Override
    public Site getSiteInfo(long siteId) {
        return jpaBaseDao.getEntityById(Site.class, siteId);
    }

    /**
     * 根据账户获取营业场所资料
     *
     * @param fieldNameValueMap
     * @param orderByFiledName
     * @return
     */
    @Override
    public List<Site> getSiteInfoListByAccountId(Map fieldNameValueMap, String orderByFiledName) {
        return jpaBaseDao.getEntitiesByFieldList(Site.class, fieldNameValueMap, orderByFiledName, null);
    }

    @Override
    public List<Site> getSiteInfoListByOrganizationId(Long organizationId) {

        return jpaBaseDao.getEntitiesByField(Site.class, "organizationId", organizationId);
    }

    /**
     * 修改营业点信息
     *
     * @return
     */
    public Site updateSiteInfoState(Site site) {
        List<TerminalUser> terminalUsers = jpaBaseDao.getEntitiesByField(TerminalUser.class, "siteId", site.getId());
        Site targetSite = jpaBaseDao.getEntityById(Site.class, site.getId());
        targetSite = (Site) MergeUtil.merge(site, targetSite);
        for (TerminalUser terminalUser : terminalUsers) {
            terminalUser.setAvailable(AvailableEnum.UNAVAILABLE);
            jpaBaseDao.merge(terminalUser);
        }
        return jpaBaseDao.merge(targetSite);
    }

    /**
     * 修改营业点信息
     *
     * @return
     */
    public Site updateSiteInfo(Site site, Site target) {
        MergeUtil.merge(site, target);
        jpaBaseDao.merge(target);

        return target;
    }

    /**
     * 查询营业点
     *
     * @param siteId
     * @return
     */
    @Override
    public List<Site> selectSiteByIds(Long[] siteId) {
        List<Site> siteList = new ArrayList<Site>();
        for (int i = 0; i < siteId.length; i++) {
            Site site = jpaBaseDao.getEntityById(Site.class, siteId[i]);
            if (site != null) {
                siteList.add(site);
            }
        }
        return siteList;
    }

    @Override
    public String getDistance(Site site, String longitude, String latitude) {

        return siteDao.getDistance(site, longitude, latitude);
    }

    @Override
    public List<String> getpreviewImageListByUUid(String uuid) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("targetUUID", uuid);
        List<Resources> resourcesList = siteDao.getEntitiesByFieldList(Resources.class, param, "sequence", BaseDao.OrderBy.ASC);
        List<String> previewImageList = new ArrayList<String>();

        for (int i = 0; i < resourcesList.size(); i++) {
            Resources resources = resourcesList.get(i);
            String previewImage = resources.getUrl();
            previewImageList.add(previewImage);
        }
        return previewImageList;
    }

    @Override
    public <T extends BaseDomain> DomainPage getsiteListBydistance(String siteName, String siteType, Long cityId, Long districtId, Integer distance,
                                                                   String longitude, String latitude,
                                                                   Long pageIndex, Long pageSize) {
        return siteDao.getsiteListBydistance(siteName, siteType,cityId,districtId, distance,longitude, latitude, pageIndex, pageSize);
    }

    @Override
    public <T extends BaseDomain> DomainPage getSiteListByproduct(Long productId, Long cityId, Long pageIndex, Long pageSize) {
        Product product = productService.getProductInfo(productId);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("organizationId", product.getOrganizationId());
        if (cityId != null){
            param.put("cityId", cityId);
        }

        DomainPage domainPage = siteDao.getEntitiesPagesByFieldList(Site.class, param, pageIndex, pageSize);
        return domainPage;
    }

    @Override
    public Site getSiteById(Long siteId) {
        return jpaBaseDao.getEntityById(Site.class, siteId);
    }
}
