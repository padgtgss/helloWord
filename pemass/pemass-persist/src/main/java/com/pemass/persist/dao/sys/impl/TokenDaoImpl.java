/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.sys.impl;

import com.pemass.common.core.constant.ConfigurationConst;
import com.pemass.common.core.constant.SystemConst;
import com.pemass.persist.dao.sys.TokenDao;
import com.pemass.persist.domain.redis.Token;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @Description: TokenDaoImpl
 * @Author: estn.zuo
 * @CreateTime: 2015-04-09 19:09
 */
@Repository
public class TokenDaoImpl implements TokenDao {

    private Log logger = LogFactory.getLog(TokenDaoImpl.class);

    @Resource
    private JedisPool jedisPool;

    @Override
    public boolean insert(Token token) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = Token.REDIS_KEY + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + token.getToken();
            jedis.select(1);
            Map<String ,String> map = new HashMap<String, String>();
            map.put("targetUUID", token.getTargetUUID());
            map.put("authority", token.getAuthority());
            map.put("deviceId", token.getDeviceId() + "");
            map.put("token", token.getToken());
            map.put("refreshToken", token.getRefreshToken());
            map.put("targetId", token.getTargetId() + "");


            jedis.hmset(key, map);
            // 设置Token的有效期
            jedis.expire(key, Integer.parseInt(ConfigurationConst.TOKEN_EXPIRY + ""));
        } catch (Exception e) {
            logger.error(e);
            return false;
        } finally {
            jedisPool.returnResource(jedis);
        }
        return true;
    }

    @Override
    public Token selectTokenByTokenValue(String tokenValue) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = Token.REDIS_KEY + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + tokenValue;
            jedis.select(1);
            Map<String, String> map = jedis.hgetAll(key);
            if (null != map) {
                Token token = new Token();
                token.setAuthority(map.get("authority"));
                token.setDeviceId(Long.parseLong(map.get("deviceId")));
                token.setRefreshToken(map.get("refreshToken"));
                token.setTargetUUID(map.get("targetUUID"));
                token.setToken(map.get("token"));
                token.setTargetId(Long.parseLong(map.get("targetId")));
                return token;
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return null;
    }

    @Override
    public void delToken(String targetUUID) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = Token.REDIS_KEY + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + "*";
            jedis.select(1);
            Set<String> results= jedis.keys(key);
            if (null != results && !results.isEmpty()) {
                for (Iterator<String> iterator = results.iterator(); iterator.hasNext(); ) {
                    String param_key = iterator.next();
                    Map<String, String> map = jedis.hgetAll(param_key);
                    if (null != map && !map.isEmpty()) {
                        Token token = selectTokenByTokenValue(map.get("token"));
                        if (token.getTargetUUID().equals(targetUUID)){
                            jedis.del(Token.REDIS_KEY + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + token.getToken());
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }
}
