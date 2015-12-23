package com.pemass.service.pemass.biz;

import com.pemass.common.server.domain.BaseDomain;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.biz.Site;

import java.util.List;
import java.util.Map;

/**
 * @author luoc
 * @Description: ${todo}
 * @date 2014/10/13
 */
public interface SiteService {
    /**
     * 保存营业点信息
     *
     * @param site
     * @return
     */
    Site saveSiteInfo(Site site);

    /**
     * 获取营业点分页列表
     *
     * @param map
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getSiteInfoList(Map map, Map fuzzyMap, long pageIndex, long pageSize);

    /**
     * 获取营业点分页列表
     *
     * @param conditions
     * @param fuzzyConditions
     * @param collectionConditions
     * @param domainPage
     * @return
     */
    DomainPage getSiteInfoList(Map<String, Object> conditions, Map<String, Object> fuzzyConditions,
                               Map<String, List<Object>> collectionConditions, DomainPage domainPage);

    /**
     * 根据Id获取营业场所资料详情
     *
     * @param siteId
     * @return
     */
    Site getSiteInfo(long siteId);

    /**
     * 根据账户获取营业场所资料
     *
     * @param fieldNameValueMap
     * @param orderByFiledName
     * @return
     */
    List<Site> getSiteInfoListByAccountId(Map fieldNameValueMap, String orderByFiledName);

    /**
     * 根据账户id获取营业场所
     *
     * @param organizationId
     * @return
     */
    List<Site> getSiteInfoListByOrganizationId(Long organizationId);

    /**
     * 修改营业点信息
     *
     * @return
     */
    public Site updateSiteInfoState(Site site);

    /**
     * 编辑营业点信息
     *
     * @param site
     * @param target
     * @return
     */
    Site updateSiteInfo(Site site, Site target);

    /**
     * 查询根据id,查询营业点
     *
     * @param siteId
     * @return
     */
    List<Site> selectSiteByIds(Long[] siteId);

    /**
     * 根据经纬度获取与营业点之间的距离
     *
     * @param site
     * @param longitude
     * @param latitude
     * @return
     */
    String getDistance(Site site, String longitude, String latitude);

    /**
     * 根据UUID获取营业点图集
     *
     * @param uuid
     * @return
     */
    List<String> getpreviewImageListByUUid(String uuid);


    /**
     * 根据条件获取营业点列表--app发现
     *
     * @param siteName
     * @param longitude
     * @param latitude
     * @param pageIndex
     * @param pageSize
     * @param <T>
     * @return
     */
    <T extends BaseDomain> DomainPage getsiteListBydistance(String siteName, String siteType, Long cityId, Long districtId, Integer distance,
                                                            String longitude, String latitude,
                                                            Long pageIndex, Long pageSize);


    /**
     * 根据产品ID获取在售卖此产品的营业点列表
     *
     * @param productId
     * @return
     */
    <T extends BaseDomain> DomainPage getSiteListByproduct(Long productId, Long cityId, Long pageIndex, Long pageSize);

    /**
     * 根据id获取营业点
     * @param siteId
     * @return
     */
    Site getSiteById(Long siteId);


}
