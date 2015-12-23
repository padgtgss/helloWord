/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.sys;

import com.pemass.persist.domain.redis.Token;

/**
 * @Description: TokenService
 * @Author: estn.zuo
 * @CreateTime: 2014-10-30 15:04
 */
public interface TokenService {

    Token insert(Token token);

    Token retrieveToken(String token);

    /**
     * 根据UUID来删除token
     *
     * @param targetUUID
     */
    void delToken(String targetUUID);
}
