/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.dao.bas.impl;

import com.pemass.persist.dao.bas.StampDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

/**
 * @Description: StampDaoImpl
 * @Author: estn.zuo
 * @CreateTime: 2015-08-19 23:53
 */
@Repository
public class StampDaoImpl implements StampDao {

    private Log logger = LogFactory.getLog(StampDaoImpl.class);

    @Resource
    private JedisPool jedisPool;

    @Override
    public String getApiStamp(String stamp) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(1);
            return jedis.get(stamp);
        } catch (Exception e) {
            logger.error(e);
        } finally {
            jedisPool.returnResource(jedis);
        }
        return null;
    }

    @Override
    public boolean setApiStamp(String stamp, int expire) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(1);
            jedis.set(stamp, stamp);
            jedis.expire(stamp, expire);
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
    public boolean setPortalStamp(String stamp, int expire) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(1);
            jedis.set(stamp, "-100");
            jedis.expire(stamp, expire);
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
    public Long incPortalStampValue(String stamp) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(1);
            return jedis.incr(stamp);
        } catch (Exception e) {
            logger.error(e);
            return 0L;
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }


}
