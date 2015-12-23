/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.sys;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.enumeration.AccountRoleEnum;

import java.util.List;

/**
 * @Description: OrganizationDao
 * @Author: zhou hang
 * @CreateTime: 2015-05-08 16:56
 */
public interface OrganizationDao  extends BaseDao{
    /**
     *审核后台  根据管理区域 查询所有商家
     * @param id
     * @return
     */
    List<Organization> getOrganizationByArea(Long id);

    /**
     * 查询不包含当前id的商户
     * @param id
     * @param provinceId
     * @return
     */
    List<Organization> getOrganizationByNoIds(Long id, Long provinceId);

    DomainPage selectWithPointPool(Long pointPoolId,long pageIndex,long pageSize);

    /**
     * 根据角色获取该角色商户的ID集合
     *
     * @param accountRole 角色
     * @return 返回结果
     */
    List<Long> getOrganizationIDByAccountRole(AccountRoleEnum accountRole);

}
