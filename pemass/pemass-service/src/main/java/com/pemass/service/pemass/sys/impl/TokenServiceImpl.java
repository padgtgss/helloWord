/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.sys.impl;

import com.pemass.common.core.util.UUIDUtil;
import com.pemass.persist.dao.sys.TokenDao;
import com.pemass.persist.domain.redis.Token;
import com.pemass.service.pemass.sys.TokenService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description: TokenServiceImpl
 * @Author: estn.zuo
 * @CreateTime: 2014-10-30 15:05
 */
@Service
public class TokenServiceImpl implements TokenService {

    @Resource
    private TokenDao tokenDao;

    @Override
    public Token insert(Token token) {
        token.setToken(UUIDUtil.randomWithoutBar());
        token.setRefreshToken(UUIDUtil.randomWithoutBar());
        tokenDao.insert(token);
        return token;
    }

    @Override
    public Token retrieveToken(String token) {
        return tokenDao.selectTokenByTokenValue(token);
    }

    @Override
    public void delToken(String targetUUID) {
        tokenDao.delToken(targetUUID);
    }
}
