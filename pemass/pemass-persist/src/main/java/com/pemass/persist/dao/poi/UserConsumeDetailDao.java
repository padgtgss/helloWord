/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.dao.poi;

import com.pemass.persist.enumeration.ConsumeTypeEnum;
import com.pemass.persist.enumeration.PointTypeEnum;

/**
 * @Description: UserConsumeDetailDao
 * @Author: estn.zuo
 * @CreateTime: 2015-07-23 20:35
 */
public interface UserConsumeDetailDao {

    boolean updateAmount(ConsumeTypeEnum consumeType, Long consumeTargetId);

    Integer getPointConsumeAmountByPointPoolId(Long pointPoolId);


    /**
     * 查询冻结积分
     * @param uid
     * @param pointType
     * @param isGeneral
     * @return
     */
    Integer getCongelaTionAmount(Long uid, PointTypeEnum pointType, Boolean isGeneral);
}
