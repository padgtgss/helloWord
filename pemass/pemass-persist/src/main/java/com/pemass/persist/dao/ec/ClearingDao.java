package com.pemass.persist.dao.ec;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;

import java.util.List;
import java.util.Map;

/**
 * @Description: ClearingDao
 * @Author: luoc
 * @CreateTime: 2015-08-18 10:36
 */
public interface ClearingDao extends BaseDao{

    /**
     * 清分表数据统计
     * @return
     */
    List<Object[]> getGroupClearing();

    /**
     * 查询商户清分表
     * @param map
     * @param organizationId
     * @return
     */
    DomainPage getOrganizationClearList(Map<String,Object> map,Long organizationId,long pageIndex,long pageSize);

    /**
     * 查询商户分润数据
     * @param map
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage getBusinessProfitsList(Map<String,Object> map,long pageIndex,long pageSize);

    List getBusinessProfitsOrganizationList(int status);
 }
