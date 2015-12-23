package com.pemass.service.pemass.sys;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.jpa.sys.OrganizationCategory;

import java.util.List;
import java.util.Map;

/**
 * @Description: OrganizationCategoryService
 * @Author: xy
 * @CreateTime: 2015-09-15 11:09
 */
public interface OrganizationCategoryService {

    /**
     * 分页查询商户类型
     * @param map
     * @param pageIndex
     * @param pageSize
     * @return
     */
    DomainPage selectOrganizationCategoryList(Map<String,Object> map,long pageIndex,long pageSize);

    /**
     * 查询单个商户类型
     * @param organizationCategoryId
     * @return
     */
    OrganizationCategory getOrganizationCategoryById(long organizationCategoryId);

    /**
     * 修改商户类型
     * @param organizationCategory
     */
    void updateOrganizationCategory(OrganizationCategory organizationCategory);

    /**
     * 添加商户类型
     * @param organizationCategory
     */
    void addOrganizationCategory(OrganizationCategory organizationCategory);

    /**
     * 查询商户类型集合
     * @return
     */
    List<OrganizationCategory> selectOrganizationCategoryList();
}
