/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.dao.poi;

import com.pemass.persist.enumeration.ConsumeTypeEnum;

/**
 * @Description: OrganizationConsumeDetailDao
 * @Author: estn.zuo
 * @CreateTime: 2015-08-04 20:05
 */
public interface OrganizationConsumeDetailDao {
    boolean updateAmount(ConsumeTypeEnum consumeType, Long consumeTargetId);

    boolean updateByPresentPackId(Integer auditStatus, ConsumeTypeEnum consumeType, Long presentPackId);

    Integer getPointUnsuccessfulConsumeAmount(Long pointPoolId);

    Integer getCountConsumeAmountByPresentPackId(Long presentPackId, ConsumeTypeEnum consumeType);
}
