/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.sys;

import com.pemass.persist.domain.jpa.sys.CreditRecord;

/**
 * @Description: CreditRecordService
 * @Author: lin.shi
 * @CreateTime: 2015-08-05 19:41
 */
public interface CreditRecordService {

    /**
     * 申请授信
     * @param creditRecord
     */
    void applyCredit(CreditRecord creditRecord);

    /**
     * 根据用户ID查询该用户关联的授信信息
     * @param userId
     * @return
     */
    CreditRecord getByUserId(Long userId);
}