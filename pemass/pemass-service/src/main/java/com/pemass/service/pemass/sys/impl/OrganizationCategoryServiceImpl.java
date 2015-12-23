package com.pemass.service.pemass.sys.impl;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.sys.OrganizationCategory;
import com.pemass.service.pemass.sys.OrganizationCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description: OrganizationCategoryServiceImpl
 * @Author: xy
 * @CreateTime: 2015-09-15 11:11
 */
@Service
public class OrganizationCategoryServiceImpl implements OrganizationCategoryService{

    @Resource
    private BaseDao jpaBaseDao;

    @Override
    public DomainPage selectOrganizationCategoryList(Map<String, Object> map, long pageIndex, long pageSize) {
        return jpaBaseDao.getEntitiesPagesByFieldList(OrganizationCategory.class,map,pageIndex,pageSize);
    }

    @Override
    public OrganizationCategory getOrganizationCategoryById(long organizationCategoryId) {
        return jpaBaseDao.getEntityById(OrganizationCategory.class,organizationCategoryId);
    }

    @Override
    public void updateOrganizationCategory(OrganizationCategory organizationCategory) {
        jpaBaseDao.merge(organizationCategory);
    }

    @Override
    public void addOrganizationCategory(OrganizationCategory organizationCategory) {
        jpaBaseDao.persist(organizationCategory);
    }

    @Override
    public List<OrganizationCategory> selectOrganizationCategoryList() {
        return jpaBaseDao.getAllEntities(OrganizationCategory.class);
    }
}
