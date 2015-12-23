/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.poi;

import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.poi.LotteryDraw;
import com.pemass.persist.domain.jpa.poi.PointPurchase;
import com.pemass.persist.enumeration.PointTypeEnum;

import java.util.List;

/**
 * @Description: LotteryDrawDao
 * @Author: zhou hang
 * @CreateTime: 2015-01-27 11:23
 */
public interface LotteryDrawDao extends BaseDao {
    /**
     * 查询奖项
     * @param uid
     * @return
     */
    List<LotteryDraw> getEntityById(Long uid);

    /**
     * 查询系统默认账户所有积分
     * @return
     * @param typeEnum
     * @param organizationId
     */
    List<PointPurchase> getBySystemUser(PointTypeEnum typeEnum, Long organizationId);

}
