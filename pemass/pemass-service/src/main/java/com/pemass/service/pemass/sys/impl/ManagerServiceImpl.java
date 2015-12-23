/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.sys.impl;

import com.google.common.collect.Lists;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.core.util.MD5Util;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.dao.poi.PresentPackDao;
import com.pemass.persist.domain.jpa.sys.Company;
import com.pemass.persist.domain.jpa.sys.Manager;
import com.pemass.service.pemass.sys.CompanyService;
import com.pemass.service.pemass.sys.ManagerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description: ManagerServiceImpl
 * @Author: estn.zuo
 * @CreateTime: 2014-10-15 18:34
 */
@Service
public class ManagerServiceImpl implements ManagerService {

    @Resource
    private BaseDao jpaBaseDao;

    @Resource
    private PresentPackDao presentPackDao;

    @Resource
    private CompanyService companyService;

    @Override
    public void insert(Manager manager) {
        jpaBaseDao.persist(manager);
    }

    @Override
    public Manager update(Manager manager) {
        Manager targetManager = jpaBaseDao.getEntityById(Manager.class, manager.getId());
        targetManager = (Manager) MergeUtil.merge(manager, targetManager);
        return jpaBaseDao.merge(targetManager);
    }

    @Override
    public List<Manager> select(Manager manager, DomainPage page) {
        return null;
    }

    @Override
    public Manager selectByManagername(String managername) {
        return jpaBaseDao.getEntityByField(Manager.class, "managername", managername);
    }

    @Override
    public Manager getManagerById(Long managerId) {
        return jpaBaseDao.getEntityById(Manager.class, managerId);
    }

    @Override
    public DomainPage getManagerByCondition(Map<String, Object> conditions, Map<String, Object> fuzzyConditions,
                                            Map<String, List<Object>> collectionConditions, Map<String, Object[]> intervalConditions,
                                            Map<String, BaseDao.OrderBy> orderByConditions, DomainPage domainPage) {
         DomainPage domain=  presentPackDao.getEntitiesPagesByFieldList(Manager.class, conditions, fuzzyConditions,
                collectionConditions, intervalConditions, orderByConditions, domainPage.getPageIndex(), domainPage.getPageSize());

        if (domain != null && domain.getDomains().size() > 0) {
            for (int i = 0; i < domain.getDomains().size(); i++) {
                Object[] objects = new Object[2];
                objects[0] = domain.getDomains().get(i);
                if (((Manager) domain.getDomains().get(i)).getCompanyId() != 0) {
                    Company company = jpaBaseDao.getEntityById(Company.class, ((Manager) domain.getDomains().get(i)).getCompanyId());
                    objects[1] = company;
                }else{
                    objects[1] = domain.getDomains().get(i);
                }
                domain.getDomains().set(i, objects);
            }
        }
        return domain;
    }

    @Override
    public boolean checkPassword(Manager manager) {
        Manager targetManager = jpaBaseDao.getEntityById(Manager.class, manager.getId());
        return targetManager.getPassword().equals(MD5Util.encrypt(manager.getPassword(), targetManager.getSalt()));
    }

    @Override
    public boolean checkManagerName(String managerName) {
        return jpaBaseDao.getEntitiesByField(Manager.class, "managername", managerName).size() == 0 ? true : false;
    }
}
