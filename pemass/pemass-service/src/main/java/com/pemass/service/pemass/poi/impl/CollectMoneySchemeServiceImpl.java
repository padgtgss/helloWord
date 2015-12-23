/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.poi.impl;

import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.poi.CollectMoneyScheme;
import com.pemass.service.pemass.poi.CollectMoneySchemeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description: CollectMoneySchemeServiceImpl
 * @Author: zhou hang
 * @CreateTime: 2015-06-26 09:51
 */
@Service
public class CollectMoneySchemeServiceImpl implements CollectMoneySchemeService {

    @Resource
    private BaseDao jpaBaseDao;

    @Override
    public CollectMoneyScheme getById(Long collectMoneySchemeId) {
        return jpaBaseDao.getEntityById(CollectMoneyScheme.class,collectMoneySchemeId);
    }
}