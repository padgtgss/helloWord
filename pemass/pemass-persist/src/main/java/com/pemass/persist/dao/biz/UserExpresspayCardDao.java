/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.biz;

/**
 * @Description: UserExpresspayCardDao
 * @Author: lin.shi
 * @CreateTime: 2015-08-26 18:48
 */
public interface UserExpresspayCardDao {

    /**
     * 解绑银通卡
     * @param userId
     * @param cardNumber
     * @return
     */
    boolean unBindUserExpresspayCard(Long userId,String cardNumber);
}