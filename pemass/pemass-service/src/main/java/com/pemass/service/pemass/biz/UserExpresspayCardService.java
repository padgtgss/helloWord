/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.biz;


import com.pemass.persist.domain.jpa.biz.UserExpresspayCard;

/**
 * @Description: UserPresspayCardService
 * @Author: lin.shi
 * @CreateTime: 2015-08-26 17:12
 */
public interface UserExpresspayCardService {

    /**
     * 根据用户获取当前绑定的银通卡
     * @param userId
     * @return
     */
    UserExpresspayCard getByUserId(Long userId);


    /**
     * 绑定银通卡
     * @param userId
     * @param cardNumber
     */
    UserExpresspayCard bindExpresspayCard(Long userId,String cardNumber);

    /**
     * 解绑银通卡
     * @param userId
     * @param cardNumber
     */
    boolean unBindExpresspayCard(Long userId,String cardNumber);
}