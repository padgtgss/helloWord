/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.service.pemass.sys;

import com.pemass.persist.domain.jpa.sys.OrganizationPos;

import java.util.List;
import java.util.Map;

/**
 * @Description: OrganizationPosService
 * @Author: estn.zuo
 * @CreateTime: 2015-07-15 15:28
 */
public interface OrganizationPosService {

    /**
     * 插入一条数据商家和机具绑定记录
     *
     * @param organizationPos
     * @return
     */
    OrganizationPos insert(OrganizationPos organizationPos);

    /**
     * 根据POS机具的序号进行查询一条绑定记录
     * @param posSerial
     * @return
     */
    OrganizationPos selectByPosSerial(String posSerial);


    /**
     * 解绑POS机
     * @param posSerial
     * @return
     */
    Boolean unbind(String posSerial);

    /**
     *  根据organizationId查询对应的机具集合
     * @param fieldNameValueMap
     * @return
     */
    List<OrganizationPos> getOrganizationPosList(Map<String, Object> fieldNameValueMap);
}
