/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.poi;

import com.pemass.persist.domain.jpa.poi.CollectMoneyScheme;

/**
 * @Description: CollectMoneySchemeService
 * @Author: zhou hang
 * @CreateTime: 2015-06-26 09:47
 */
public interface CollectMoneySchemeService {
    /**
     * 查询策略方案
     * @param collectMoneySchemeId
     * @return
     */
    CollectMoneyScheme getById(Long collectMoneySchemeId);

}
