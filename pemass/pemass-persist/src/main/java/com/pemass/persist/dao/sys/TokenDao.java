/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.sys;

import com.pemass.persist.domain.redis.Token;

/**
 * @Description: TokenDao
 * @Author: estn.zuo
 * @CreateTime: 2015-04-09 18:59
 */
public interface TokenDao {

    /**
     * 插入一条Token信息
     *
     * @param token
     * @return
     */
    boolean insert(Token token);

    /**
     * 根据Token值查询Token信息
     *
     * @param tokenValue
     * @return
     */
    Token selectTokenByTokenValue(String tokenValue);

    void delToken(String targetUUID);
}
