/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.dao.bas.impl;

import com.pemass.persist.dao.bas.SequenceDao;
import com.pemass.persist.enumeration.SequenceEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

/**
 * @Description: 自增序列Dao实现类
 * @Author: estn.zuo
 * @CreateTime: 2015-07-10 00:23
 */
@Repository
public class SequenceDaoImpl implements SequenceDao {

    private Log logger = LogFactory.getLog(SequenceDaoImpl.class);

    public static final String REDIS_KEY = "sequence";

    @Resource
    private JedisPool jedisPool;

    @Override
    public Long increase(SequenceEnum field) {
        return increase(field, 1);
    }

    @Override
    public Long increase(SequenceEnum field, int step) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.hincrBy(REDIS_KEY, field.name(), step);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return null;
    }

}

