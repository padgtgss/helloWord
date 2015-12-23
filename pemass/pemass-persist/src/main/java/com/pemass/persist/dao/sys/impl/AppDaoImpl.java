/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.sys.impl;

import com.google.common.collect.ImmutableMap;
import com.pemass.common.core.constant.SystemConst;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.dao.sys.AppDao;
import com.pemass.persist.domain.redis.App;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: AppDaoImpl
 * @Author: pokl.huang
 * @CreateTime: 2015-04-09 11:36
 */
@Repository
public class AppDaoImpl implements AppDao {

    private Log logger = LogFactory.getLog(AppDaoImpl.class);

    @Resource
    private JedisPool jedisPool;

    @Override
    public App getAppById(String appid) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = App.REDIS_KEY + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + appid;
            Map<String, String> map = jedis.hgetAll(key);
            if (null != map && !map.isEmpty()) {
                App app = new App();
                app.setAppid(appid);
                app.setKey(map.get("key"));
                app.setAppname(map.get("appname"));
                app.setCreatetime(map.get("createtime"));
                return app;
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
    public boolean saveApp(App app) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = App.REDIS_KEY + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + app.getAppid();
            ImmutableMap<String, String> map = ImmutableMap.of("appid", app.getAppid(), "appname", app.getAppname(), "key", app.getKey(), "createtime", new Date().toString());
            jedis.hmset(key, map);
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
    public App getAppByName(String appName) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = App.REDIS_KEY + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + "*";

            Set<String> keys = new HashSet<String>();
            keys = jedis.keys(key);
            if (null != keys && !keys.isEmpty()) {

                for (Iterator<String> iterator = keys.iterator(); iterator.hasNext(); ) {
                    String param_key = iterator.next();
                    Map<String, String> map = jedis.hgetAll(param_key);
                    App app = new App();
                    if (null != map && !map.isEmpty() && StringUtils.equals(map.get("appname").toString(),appName)) {
                        app.setAppid(map.get("appid"));
                        app.setKey(map.get("key"));
                        app.setAppname(map.get("appname"));
                        app.setCreatetime(map.get("createtime"));

                        return app;
                    }
                }

            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
        return null;
    }


    @Override
    public boolean delApp(String appid) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = App.REDIS_KEY + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + appid;

            Map<String, String> map = jedis.hgetAll(key);
            if (null != map && !map.isEmpty()) {
                jedis.del(key);
            }
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
    public DomainPage getApps(String appid, Long pageIndex, Long pageSize) {
        Jedis jedis = null;
        DomainPage domainPage = new DomainPage();
        domainPage.setPageIndex(pageIndex);
        domainPage.setPageSize(pageSize);
        String key = null;
        try {
            jedis = jedisPool.getResource();
            if (StringUtils.isNotBlank(appid)) {
                key = App.REDIS_KEY + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + "*" + appid + "*";
            } else {
                key = App.REDIS_KEY + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + "*";
            }

            Set<String> keys = new HashSet<String>();
            keys = jedis.keys(key);
            if (null != keys && !keys.isEmpty()) {
                domainPage.setTotalCount(keys.size());
                List<App> apps = new ArrayList<App>();

                for (Iterator<String> iterator = keys.iterator(); iterator.hasNext(); ) {
                    String param_key = iterator.next();
                    Map<String, String> map = jedis.hgetAll(param_key);
                    App app = new App();
                    if (null != map && !map.isEmpty()) {
                        app.setAppid(map.get("appid"));
                        app.setKey(map.get("key"));
                        app.setAppname(map.get("appname"));
                        app.setCreatetime(map.get("createtime"));
                    }
                    apps.add(app);
                }
                Collections.sort(apps, new SortByCreatetime());
                List<App> re_apps = new ArrayList<App>();

                for (int i = (pageIndex.intValue() - 1) * pageSize.intValue(); (i < pageIndex.intValue() * pageSize.intValue()) && (i < domainPage.getTotalCount()); i++) {
                    App app = apps.get(i);
                    re_apps.add(app);
                }


                domainPage.setDomains(re_apps);
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


    class SortByCreatetime implements Comparator {
        public int compare(Object o1, Object o2) {
            App s1 = (App) o1;
            App s2 = (App) o2;
            return s1.getCreatetime().compareTo(s2.getCreatetime());
        }
    }

}

