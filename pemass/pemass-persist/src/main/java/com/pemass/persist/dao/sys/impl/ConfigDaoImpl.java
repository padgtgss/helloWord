/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */
package com.pemass.persist.dao.sys.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.pemass.common.core.constant.SystemConst;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.dao.sys.ConfigDao;
import com.pemass.persist.domain.redis.Config;
import com.pemass.persist.enumeration.ConfigEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: ConfigDaoImpl
 * @Author: estn.zuo
 * @CreateTime: 2015-07-29 11:35
 */
@Repository
public class ConfigDaoImpl implements ConfigDao {

    private Log logger = LogFactory.getLog(AppDaoImpl.class);

    @Resource
    private JedisPool jedisPool;

    @Override
    public boolean insert(Config config) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = Config.REDIS_KEY + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + config.getKey();
            ImmutableMap<String, String> map = ImmutableMap.of(
                    "key", config.getKey(),
                    "value", config.getValue(),
                    "title", config.getTitle(),
                    "createTime", new Date().toString()
            );
            jedis.hmset(key, map);
        } catch (Exception e) {
            logger.error(e);
            return false;
        } finally {
            jedisPool.returnResource(jedis);
        }
        return true;
    }

    @Override
    public Config update(Config config) {
        Preconditions.checkNotNull(config.getKey());
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = Config.REDIS_KEY + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + config.getKey();
            ImmutableMap<String, String> map = ImmutableMap.of(
                    "key", config.getKey(),
                    "value", config.getValue(),
                    "title", config.getTitle()
            );
            jedis.hmset(key, map);
            return config;
        } catch (Exception e) {
            logger.error(e);
            return null;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    @Override
    public Config getByKey(ConfigEnum keyEnum) {
        Jedis jedis = null;
        Config config = null;
        try {
            jedis = jedisPool.getResource();
            String key = Config.REDIS_KEY + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + keyEnum.toString();
            Map<String, String> values = jedis.hgetAll(key);
            if (values != null && !values.isEmpty()) {
                config = new Config();
                config.setKey(values.get("key"));
                config.setTitle(values.get("title"));
                config.setValue(values.get("value"));
                config.setCreateTime(values.get("createTime"));
                return config;
            }
        } catch (Exception e) {
            logger.error(e);
            return null;
        } finally {
            jedisPool.returnResource(jedis);
        }
        return null;
    }

    @Override
    public DomainPage<Config> search(Long pageIndex, Long pageSize) {
        Jedis jedis = null;
        DomainPage<Config> domainPage = new DomainPage<Config>();
        domainPage.setPageIndex(pageIndex);
        domainPage.setPageSize(pageSize);
        try {
            jedis = jedisPool.getResource();
            String key = Config.REDIS_KEY + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + "*";
            Set<String> keys = jedis.keys(key);
            if (null != keys && !keys.isEmpty()) {
                domainPage.setTotalCount(keys.size());
                List<Config> configs = Lists.newArrayList();

                for (Iterator<String> iterator = keys.iterator(); iterator.hasNext(); ) {
                    String paramKey = iterator.next();
                    Map<String, String> map = jedis.hgetAll(paramKey);
                    Config config = new Config();
                    if (null != map && !map.isEmpty()) {
                        config.setKey(map.get("key"));
                        config.setValue(map.get("value"));
                        config.setTitle(map.get("title"));
                        config.setCreateTime(map.get("createTime"));
                        configs.add(config);
                    }
                }
                Collections.sort(configs, new SortByCreateTime());
                List<Config> sortedConfigs = Lists.newArrayList();

                for (int i = (pageIndex.intValue() - 1) * pageSize.intValue(); (i < pageIndex.intValue() * pageSize.intValue()) && (i < domainPage.getTotalCount()); i++) {
                    Config config = configs.get(i);
                    sortedConfigs.add(config);
                }

                domainPage.setDomains(sortedConfigs);
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return domainPage;
    }


    class SortByCreateTime implements Comparator {
        public int compare(Object o1, Object o2) {
            Config s1 = (Config) o1;
            Config s2 = (Config) o2;
            return s1.getCreateTime().compareTo(s2.getCreateTime());
        }
    }
}
