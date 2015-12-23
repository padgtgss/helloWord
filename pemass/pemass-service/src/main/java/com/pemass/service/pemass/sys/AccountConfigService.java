/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.sys;


import com.pemass.persist.domain.jpa.sys.Organization;

import java.util.List;
import java.util.Map;

/**
 * @Description: AccountConfigService
 * @Author: zh
 * @CreateTime: 2014-10-13 13:20
 */
public interface AccountConfigService {

    /**
     * 根据id查询用户信息
     * @param configId
     * @return
     */
    public Organization getConfigInfoById(Long configId);

    /**
     * 查询用户详情
     * @param fieldNameValueMap
     * @param orderByFiledName
     * @return
     */
    public List getConfigInfoList(Map fieldNameValueMap,String orderByFiledName);

}