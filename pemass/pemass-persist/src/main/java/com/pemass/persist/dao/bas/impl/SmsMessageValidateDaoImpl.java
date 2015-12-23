package com.pemass.persist.dao.bas.impl;

import com.pemass.common.core.constant.SystemConst;
import com.pemass.common.core.util.UUIDUtil;
import com.pemass.persist.dao.bas.SmsMessageValidateDao;
import com.pemass.persist.dao.sys.impl.ServiceControllDaoImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

/**
 * @Description: SmsMessageValidateDaoImpl
 * @Author: pokl.huang
 * @CreateTime: 2015-08-27 16:33
 */
@Repository
public class SmsMessageValidateDaoImpl implements SmsMessageValidateDao {

    private Log logger = LogFactory.getLog(ServiceControllDaoImpl.class);

    @Resource
    private JedisPool jedisPool;

    private String REDIS_KEY = "SMS_VALIDATE";

    @Override
    public boolean validateSms(String telephone) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = REDIS_KEY + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + telephone;
            Long length = jedis.scard(key);
            if (length == 0){
                jedis.sadd(key, UUIDUtil.randomUUID());
                jedis.expire(key, 86400);
                return true;
            }else if (length < 3  && length >0){
                jedis.sadd(key, UUIDUtil.randomUUID());
                return true;
            }else {
                return false;
            }

        } catch (Exception e) {
            logger.error(e);
            return false;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }
}

