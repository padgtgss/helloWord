/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.sys.impl;

import com.google.common.collect.ImmutableMap;
import com.pemass.common.core.constant.SystemConst;
import com.pemass.persist.dao.sys.OrderCodeDao;
import com.pemass.persist.domain.redis.OrderCode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Description: OrderDaoImpl
 * @Author: lin.shi
 * @CreateTime: 2015-09-28 10:42
 */
@Repository
public class OrderCodeDaoImpl implements OrderCodeDao {

    private Log logger = LogFactory.getLog(OrderPayDaoImpl.class);

    @Resource
    private JedisPool jedisPool;

    @Override
    public OrderCode get(String orderCode) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(3);
            String key = OrderCode.REDIS_KEY + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + orderCode;
            Map<String, String> map = jedis.hgetAll(key);
            if (null != map && !map.isEmpty()) {
                OrderCode order = new OrderCode();
                order.setOrderIdentifier(map.get("orderIdentifier"));
                order.setCode(map.get("code"));
                return order;
            }
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


    @Override
    public Boolean save(OrderCode order) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(3);
            String key = OrderCode.REDIS_KEY + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + order.getCode();
            ImmutableMap<String, String> map = ImmutableMap.of("orderIdentifier", order.getOrderIdentifier(), "code", order.getCode());
            jedis.hmset(key, map);
            jedis.expire(key, 300);
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
    public void delOrder(OrderCode order) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(3);
            String key = OrderCode.REDIS_KEY + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + order.getCode();
            jedis.del(key);
        }catch (Exception e){
            logger.error(e);
        }
        finally {
            jedisPool.returnResource(jedis);
        }
    }
}