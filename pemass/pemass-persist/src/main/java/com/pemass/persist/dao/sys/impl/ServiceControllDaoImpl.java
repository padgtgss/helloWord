package com.pemass.persist.dao.sys.impl;

import com.pemass.common.core.constant.SystemConst;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.common.server.domain.Expression;
import com.pemass.common.server.domain.Operation;
import com.pemass.persist.dao.sys.ServiceControllDao;
import com.pemass.persist.domain.jpa.sys.ServiceControll;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @Description: ServiceControllDaoImpl
 * @Author: pokl.huang
 * @CreateTime: 2015-08-26 20:28
 */
@Repository
public class ServiceControllDaoImpl extends JPABaseDaoImpl implements ServiceControllDao {

    private Log logger = LogFactory.getLog(ServiceControllDaoImpl.class);

    private String REDIS_KEY = "APP_SERVICE_CONTROLL";

    @Resource
    private JedisPool jedisPool;


    public Boolean grant(String appid, List<String> urls) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = REDIS_KEY + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + appid;
            for (String url : urls) {
                jedis.sadd(key, url);
            }

        } catch (Exception e) {
            logger.error(e);
            return false;
        } finally {
            jedisPool.returnResource(jedis);
        }
        return true;
    }

    @Override
    public Boolean hasAuth(String appid, String url) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = REDIS_KEY + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + appid;

            /**
             *   判断是否包含所有("B/**" 或者 "C/**" 如果有的话,表示全部)
             */
            String allUrl = url.substring(0, 1) + "/**";
            if (jedis.sismember(key, allUrl)) {
                return true;
            }

            return jedis.sismember(key, url);
        } catch (Exception e) {
            logger.error(e);
            return false;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    @Override
    public DomainPage<String> getByAppId(String appid, Long pageIndex, Long pageSize) {
        Jedis jedis = null;
        DomainPage domainPage = new DomainPage();
        domainPage.setPageIndex(pageIndex);
        domainPage.setPageSize(pageSize);
        String key = null;
        try {
            jedis = jedisPool.getResource();
             key = REDIS_KEY + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + appid;
            Set<String> urls = new HashSet<String>();
            urls = jedis.smembers(key);

            if(urls!= null && urls.size()>0){
                domainPage.setTotalCount(urls.size());
                List<String> list = new ArrayList<String>();
                for (Iterator<String> iterator = urls.iterator(); iterator.hasNext(); ) {
                    String str = iterator.next();
                     list.add(str);
                }

                List<String> pageData = new ArrayList<String>();
                for (int i = (pageIndex.intValue() - 1) * pageSize.intValue(); (i < pageIndex.intValue() * pageSize.intValue()) && (i < domainPage.getTotalCount()); i++) {
                   pageData.add(list.get(i));
                }

                domainPage.setDomains(pageData);
            }

        }catch (Exception e) {
            logger.error(e);
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }

        return domainPage;
    }

    @Override
    public void unGrant(String appid, String url) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = REDIS_KEY + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + appid;
            jedis.srem(key,url);
        }catch (Exception e) {
            logger.error(e);
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public ServiceControll getByUrl(String url) {
        Expression urlEx = new Expression("url", Operation.Equal,url);
        List<ServiceControll> serviceControlls = this.getEntitiesByExpression(ServiceControll.class, urlEx);
        if(serviceControlls!= null && serviceControlls.size()>0){
            return serviceControlls.get(0);
        }
        return null;
    }

}

