/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.service.pemass.poi;

import com.pemass.persist.domain.jpa.poi.CollectMoneyStrategy;
import com.pemass.persist.domain.jpa.poi.CollectMoneyStrategySnapshot;

/**
 * @Description: CollectMoneyStrategySnapshotService
 * @Author: estn.zuo
 * @CreateTime: 2015-08-03 16:09
 */
public interface CollectMoneyStrategySnapshotService {

    /**
     * 保存自定义收款策略快照
     *
     * @param strategy 原始自定义收款策略
     * @return
     */
    CollectMoneyStrategySnapshot insertFromStrategy(CollectMoneyStrategy strategy);

    /**
     * 根据策略快照ID查询快照详情
     *
     * @param collectMoneyStrategySnapshotId
     * @return
     */
    CollectMoneyStrategySnapshot getById(Long collectMoneyStrategySnapshotId);

}
