/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.dao.poi;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;

import java.util.List;

/**
 * @Description: PointPoolOrganizationDao
 * @Author: oliver.he
 * @CreateTime: 2015-07-17 16:32
 */
public interface PointPoolOrganizationDao extends BaseDao {

    /**
     * 分页获取特约商户
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectAllPointPoolOrganization(long pageIndex, long pageSize);

    List selectByOrganizationId(Long organizationId);



}