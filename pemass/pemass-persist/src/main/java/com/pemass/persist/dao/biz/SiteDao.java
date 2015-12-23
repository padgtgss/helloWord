/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.biz;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.domain.BaseDomain;
import com.pemass.persist.domain.jpa.biz.Site;

import java.util.Map;

/**
 * @Description: SiteService
 * @Author: pokl.huang
 * @CreateTime: 2014-12-10 10:36
 */
public interface SiteDao extends BaseDao {

    String getDistance(Site site, String longitude, String latitude);

    <T extends BaseDomain> DomainPage getsiteListBydistance(String siteName, String siteType, Long cityId, Long districtId, Integer distance,
                                                            String longitude, String latitude,
                                                            Long pageIndex, Long pageSize);

    DomainPage getEntitiesByFieldList(Class clazz,Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap,long pageIndex,long pageSize);
}
