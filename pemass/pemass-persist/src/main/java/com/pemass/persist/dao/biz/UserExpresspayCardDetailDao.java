/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.biz;

/**
 * @Description: UserExpresspayCardDetailDao
 * @Author: lin.shi
 * @CreateTime: 2015-08-27 15:26
 */
public interface UserExpresspayCardDetailDao {


    /**
     * 获取圈存总额
     * @param uid
     * @return
     */
    Object[] getDepositMoneyCount(Long uid);
}