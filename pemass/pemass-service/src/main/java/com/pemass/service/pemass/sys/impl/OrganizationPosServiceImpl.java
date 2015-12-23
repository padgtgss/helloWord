/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.service.pemass.sys.impl;

import com.google.common.base.Preconditions;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.enumeration.AvailableEnum;
import com.pemass.persist.domain.jpa.sys.OrganizationPos;
import com.pemass.service.pemass.sys.OrganizationPosService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description: OrganizationPosServiceImpl
 * @Author: estn.zuo
 * @CreateTime: 2015-07-15 15:30
 */
@Service
public class OrganizationPosServiceImpl implements OrganizationPosService {

    @Resource
    private BaseDao jpaBaseDao;

    @Override
    public OrganizationPos insert(OrganizationPos organizationPos) {
        Preconditions.checkNotNull(organizationPos);
        jpaBaseDao.persist(organizationPos);
        return organizationPos;
    }

    @Override
    public OrganizationPos selectByPosSerial(String posSerial) {
        Preconditions.checkNotNull(posSerial);
        return jpaBaseDao.getEntityByField(OrganizationPos.class, "posSerial", posSerial);
    }

    @Transactional
    @Override
    public Boolean unbind(String posSerial) {
        Preconditions.checkNotNull(posSerial);
        OrganizationPos organizationPos = jpaBaseDao.getEntityByField(OrganizationPos.class, "posSerial", posSerial);
        organizationPos.setAvailable(AvailableEnum.UNAVAILABLE);
        jpaBaseDao.merge(organizationPos);
        return true;
    }

    @Override
    public List<OrganizationPos> getOrganizationPosList(Map<String, Object> fieldNameValueMap ) {
        return jpaBaseDao.getEntitiesByFieldList(OrganizationPos.class, fieldNameValueMap);
    }
}
