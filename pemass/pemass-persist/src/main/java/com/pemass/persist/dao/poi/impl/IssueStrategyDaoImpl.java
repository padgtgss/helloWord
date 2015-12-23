/**
 * Copyright  2015  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.poi.impl;

import com.pemass.common.core.constant.SystemConst;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.core.util.DateUtil;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.poi.IssueStrategyDao;
import com.pemass.persist.enumeration.StrategyStatus;
import com.pemass.persist.enumeration.StrategyTypeEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * @Description: IssueStrategyDaoImpl
 * @Author: zhou hang
 * @CreateTime: 2015-05-06 14:52
 */
@Repository
public class IssueStrategyDaoImpl extends JPABaseDaoImpl implements IssueStrategyDao {


    private Log logger = LogFactory.getLog(IssueStrategyDaoImpl.class);

    private String REDIS_KEY = "ISSUE_STRATEGY";

    @Resource
    private JedisPool jedisPool;

    @Override
    public DomainPage findById(long pageIndex, long pageSize) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT distinct pis.id,sum(psc.amount),so.organization_name,pis.strategy_name,pis.expiry_time,pis.users,so.logo ");
        sb.append("from poi_strategy_content psc ");
        sb.append("LEFT JOIN poi_issue_strategy pis on psc.issue_strategy_id=pis.id ");
        sb.append("LEFT JOIN sys_organization so on so.id=pis.organization_id ");
        sb.append(" where pis.strategy_status=?1 and pis.strategy_type=?2 and pis.expiry_time>=?3  GROUP BY pis.id ");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, StrategyStatus.EXECUTED.toString());
        query.setParameter(2, StrategyTypeEnum.SEND_PRESENT_SQUARE.toString());
        query.setParameter(3, DateUtil.gap(new Date(), "yyyy-MM-dd HH:mm:ss"));
        query.setFirstResult((int) ((pageIndex - 1) * pageSize));
        query.setMaxResults((int) pageSize);

        List<Object[]> list = query.getResultList();
        long totalCount = getEntityTotalCount().longValue();
        DomainPage domainPage = new DomainPage(pageSize, pageIndex, totalCount);
        domainPage.setDomains(list);
        return domainPage;
    }

    @Override
    public List selectIssueStrategyByField() {
        String sql = "select p.id from poi_issue_strategy p where p.available = 1 and  p.strategy_status != 'REPEAL' and " +
                " (p.strategy_type = 'SEND_PRESENT_SQUARE' or p.strategy_type = 'CONTENT_PRESENT_NOSHOW' or p.strategy_type = 'UAV')";
        Query query = em.createNativeQuery(sql);
        return query.getResultList();
    }


    private BigInteger getEntityTotalCount() {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT count(distinct pis.id) ");
        sb.append("from poi_strategy_content psc ");
        sb.append("LEFT JOIN poi_issue_strategy pis on psc.issue_strategy_id=pis.id ");
        sb.append(" where pis.strategy_status=?1 and pis.strategy_type=?2 and pis.expiry_time>=?3 ");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, StrategyStatus.EXECUTED.toString());
        query.setParameter(2, StrategyTypeEnum.SEND_PRESENT_SQUARE.toString());
        query.setParameter(3, DateUtil.gap(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return (BigInteger) query.getResultList().get(0);
    }

    @Override
    public Boolean bindPresentSquareToIssueStrategy(Long presentSquareId, Long issueStrategyId, Date expiryTime) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = REDIS_KEY + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + issueStrategyId;
            jedis.lpush(key, presentSquareId + "");
            // 设置有效期
            int seconds = Seconds.secondsBetween(new DateTime(), new DateTime(expiryTime)).getSeconds();
            jedis.expire(key, Integer.parseInt(seconds + ""));
        } catch (Exception e) {
            logger.error(e);
            return false;
        } finally {
            jedisPool.returnResource(jedis);
        }
        return true;
    }

    @Override
    public Long popupPresentSquareId(Long issueStrategyId) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = REDIS_KEY + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + issueStrategyId;
            String presentSquareId = jedis.lpop(key);
            if ("nil".equals(presentSquareId)) {
                return null;
            }
            return Long.parseLong(presentSquareId);
        } catch (Exception e) {
            logger.error(e);
            return null;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    @Override
    public Boolean hasAlreadyGrab(Long userId, Long issueStrategyId, Date expiryTime) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = REDIS_KEY + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + "GRAB" + SystemConst.REDIS_KEY_SEPARATOR_SYMBOL + issueStrategyId;
            Long result = jedis.sadd(key, userId + "");
            // 设置有效期(当天有效)
            int seconds = Seconds.secondsBetween(new DateTime(), new DateTime().minuteOfDay().withMaximumValue()).getSeconds();
            jedis.expire(key, Integer.parseInt(seconds + ""));
            return result == 0;
        } catch (Exception e) {
            logger.error(e);
            return false;
        } finally {
            jedisPool.returnResource(jedis);
        }
    }


}