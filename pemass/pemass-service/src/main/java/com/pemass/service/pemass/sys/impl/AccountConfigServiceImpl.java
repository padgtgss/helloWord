/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.sys.impl;

import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.service.pemass.sys.AccountConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * @Description: AccountConfigServiceImpl
 * @Author: zh
 * @CreateTime: 2014-10-13 13:27
 */
@Service
public class AccountConfigServiceImpl implements AccountConfigService {

    @Resource
    private BaseDao jpaBaseDao;

    /**
     * 根据id查询用户信息
     * @param configId
     * @return
     */
    @Override
    public Organization getConfigInfoById(Long configId) {
        return jpaBaseDao.getEntityById(Organization.class, configId);
    }

    /**
     * 查询用户详情
     * @param fieldNameValueMap
     * @param orderByFiledName
     * @return
     */
    @Override
    public List getConfigInfoList(Map fieldNameValueMap,String orderByFiledName) {
        return jpaBaseDao.getEntitiesByFieldList(Organization.class, fieldNameValueMap, orderByFiledName, null);
    }
}