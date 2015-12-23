/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.sys.impl;

import com.google.common.collect.ImmutableMap;
import com.pemass.common.core.constant.SystemConst;
import com.pemass.persist.dao.sys.OrderPayDao;
import com.pemass.persist.domain.redis.OrderPay;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Description: OrderPayDaoImpl
 * @Author: lin.shi
 * @CreateTime: 2015-09-22 11:04
 */
@Repository
public class OrderPayDaoImpl implements OrderPayDao {

    private Log logger = LogFactory.getLog(OrderPayDaoImpl.class);

    @Resource
    private JedisPool jedisPool;

    @Override
    public OrderPay getOrderPayById(String payCode) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(2);
            String key = OrderPay.REDIS_KEY + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + payCode;
            Map<String, String> map = jedis.hgetAll(key);
            if (null != map && !map.isEmpty()) {
                OrderPay orderPay = new OrderPay();
                orderPay.setPayCode(map.get("payCode"));
                orderPay.setUsername(map.get("username"));
                orderPay.setUserId(Long.parseLong(map.get("userId")));
                return orderPay;
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
    public Boolean save(OrderPay orderPay) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(2);
            String key = orderPay.REDIS_KEY + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + orderPay.getPayCode();
            ImmutableMap<String, String> map = ImmutableMap.of("userId", orderPay.getUserId().toString(), "payCode", orderPay.getPayCode(),"username", orderPay.getUsername());
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
    public void delOrderPay(OrderPay orderPay) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(2);
            String key = orderPay.REDIS_KEY + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + orderPay.getPayCode();
            jedis.del(key);
        }catch (Exception e){
            logger.error(e);
        }
        finally {
            jedisPool.returnResource(jedis);
        }
    }
}
