/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.dao.sys.impl;

import com.pemass.common.core.constant.SystemConst;
import com.pemass.persist.dao.sys.LoginFailTimesDao;
import com.pemass.persist.domain.redis.LoginFailTimes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

/**
 * @Description: LoginFailTimesDaoImpl
 * @Author: estn.zuo
 * @CreateTime: 2015-06-23 17:42
 */
@Repository
public class LoginFailTimesDaoImpl implements LoginFailTimesDao {

    private Log logger = LogFactory.getLog(LoginFailTimesDaoImpl.class);

    @Resource
    private JedisPool jedisPool;

    @Override
    public Boolean insert(LoginFailTimes loginFailTimes) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            /*--    key : login_fail_times:18010558729
                    value :  3
            --*/
            String key = LoginFailTimes.REDIS_KEY + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + loginFailTimes.getUsername();
            String value = loginFailTimes.getTimes() + "";
            jedis.set(key, value);

            /*-- 设置当天晚上凌晨为过期时间 --*/
            DateTime now = new DateTime();
            DateTime expireTime = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 23, 59, 59, 0);
            jedis.expire(key, (int) ((expireTime.getMillis() - now.getMillis()) / 1000));
        } catch (Exception e) {
            logger.error(e);
            return false;
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return true;
    }

    @Override
    public Boolean clear(String username) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            /*--    key : login_fail_times:18010558729
            --*/
            String key = LoginFailTimes.REDIS_KEY + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + username;
            jedis.del(key);
        } catch (Exception e) {
            logger.error(e);
            return false;
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return true;
    }

    @Override
    public Boolean increase(String username) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = LoginFailTimes.REDIS_KEY + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + username;
            jedis.incr(key);
        } catch (Exception e) {
            logger.error(e);
            return false;
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return true;
    }

    @Override
    public Integer getLoginFailTimesByUsername(String username) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = LoginFailTimes.REDIS_KEY + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + username;
            String value = jedis.get(key);
            if (value == null) {
                return 0;
            }
            return Integer.parseInt(value);
        } catch (Exception e) {
            logger.error(e);
            return 0;
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }
}
