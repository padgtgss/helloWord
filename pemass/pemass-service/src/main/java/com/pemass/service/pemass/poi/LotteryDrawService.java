/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.poi;

/**
 * @Description: LotteryDrawService
 * @Author: zhou hang
 * @CreateTime: 2015-01-27 11:17
 */
public interface LotteryDrawService {
    /**
     * 抽大奖
     * @param uid 用户id
     * @param prize  奖项
     * @return
     */
    void insertExecute(Long uid, Integer prize);
}
