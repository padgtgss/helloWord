/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.persist.dao.poi.impl;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Ordering;
import com.pemass.common.core.constant.SystemConst;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.server.dao.jpa.JPABaseDaoImpl;
import com.pemass.persist.dao.poi.CollectMoneyStrategyDao;
import com.pemass.persist.domain.jpa.poi.CollectMoneyStrategy;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @Description: CollectMoneyStrategyDaoImpl
 * @Author: zhou hang
 * @CreateTime: 2014-12-26 21:48
 */
@Repository
public class CollectMoneyStrategyDaoImpl extends JPABaseDaoImpl implements CollectMoneyStrategyDao {

    @Override
    @SuppressWarnings("all")
    public List<Object[]> selectByTerminalUserId(Long terminalUserId) {
        StringBuffer sb = new StringBuffer();
        sb.append("select c.id,c.strategy_name from poi_collect_money_strategy c ");
        sb.append(" left join sys_organization so on so.id=c.organization_id  ");
        sb.append(" where c.available=1 and so.available=1 ");
        sb.append(" and so.id=?1 and c.end_time>?2 and c.is_valid=1 and c.start_time <?3 ");
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, terminalUserId);
        query.setParameter(2, new Date());
        query.setParameter(3, new Date());
        return query.getResultList();
    }

    @Override
    @SuppressWarnings("all")
    public DomainPage getCollectMoneyStrategyByCondition(Map<String, Object> conditions, long pageIndex, long pageSize) {
        checkNotNull(conditions);
        /** 获取满足条件的结果 **/
        final Long organizationId = (Long) conditions.get("organizationId");
        StringBuilder sql = new StringBuilder("SELECT cms.* FROM poi_collect_money_strategy cms");
        sql.append(" WHERE cms.available = 1");
        if (organizationId != null) sql.append(" AND cms.organization_id = " + organizationId);
        sql.append(" ORDER BY cms.create_time DESC");
        sql.append(" LIMIT " + (pageIndex - 1) * pageSize + "," + pageSize);

        Query query = em.createNativeQuery(sql.toString(), CollectMoneyStrategy.class);
        List<CollectMoneyStrategy> resultList = query.getResultList();

        /** 对结果集进行排序 **/
        Comparator<CollectMoneyStrategy> comparator = new Comparator<CollectMoneyStrategy>() {
            @Override
            public int compare(CollectMoneyStrategy o1, CollectMoneyStrategy o2) {
                int isValidO1 = apply(o1.getIsValid());
                int isValidO2 = apply(o2.getIsValid());

                return isValidO1 == isValidO2 ? 0 : (isValidO1 > isValidO2 ? 1 : -1);
            }

            /* 重新定义这个字段的优先级 有效优先级最高 其次是已过期 最后是失效 */
            private int apply(int isValid) {
                if (isValid == 0) return 0;
                else if (isValid == 1) return 2;
                else return 1;
            }
        };
        resultList = Ordering.from(comparator).sortedCopy(resultList);

        /** 获取总记录 **/
        Function<Map<String, Object>, Long> getTotalCount = new Function<Map<String, Object>, Long>() {
            StringBuilder totalCountSql;

            {
                totalCountSql = new StringBuilder("SELECT COUNT(cms.id) FROM poi_collect_money_strategy cms");
                totalCountSql.append(" WHERE cms.available = 1");
                if (organizationId != null) totalCountSql.append(" AND cms.organization_id = " + organizationId);
            }

            @Override
            public Long apply(Map<String, Object> input) {
                Query totalCountQuery = em.createNativeQuery(totalCountSql.toString());
                List totalCountResultList = totalCountQuery.getResultList();
                Long totalCount = 0L;
                if (totalCountResultList.size() == 1)
                    totalCount = Long.parseLong(totalCountResultList.get(0).toString());

                return totalCount;
            }
        };

        long totalCount = getTotalCount.apply(conditions);

        /** 封装结果并返回 **/
        DomainPage returnDomainPage = new DomainPage(pageSize, pageIndex, totalCount);
        returnDomainPage.setDomains(resultList);

        return returnDomainPage;
    }

    @Override
    @SuppressWarnings("all")
    public void updateCollectMoneyStrategy() {
        StringBuffer sb = new StringBuffer();
        sb.append("update poi_collect_money_strategy ");
        sb.append(" set is_valid = 2");
        sb.append(" where id in ");
        sb.append(" (select t.id from (select id from poi_collect_money_strategy where end_time < now() and available =1) t)");
        Query query = em.createNativeQuery(sb.toString());
        query.executeUpdate();
    }

    @Override
    @SuppressWarnings("all")
    public boolean checkStrategy(Long organizationId, Date startTime, Date endTime, String executeStartTime, String executeEndTime) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        String startDateTime = new DateTime(startTime).toString(formatter);
        String endDateTime = new DateTime(endTime).toString(formatter);

        StringBuilder sql = new StringBuilder("SELECT cms.execute_start_time,cms.execute_end_time FROM poi_collect_money_strategy cms");
        sql.append(" WHERE cms.available = 1");
        sql.append(" AND cms.is_valid = 1");
        if (organizationId != null) sql.append(" AND cms.organization_id = " + organizationId);
        if (startTime != null && endTime != null) {
            sql.append(" AND ( cms.start_time <= '" + endDateTime + "'");
            sql.append(" AND cms.end_time >= '" + startDateTime + "'");
            sql.append(" )");
        }

        Query query = em.createNativeQuery(sql.toString());
        @SuppressWarnings("unchecked") List<Object[]> results = query.getResultList();
        for (Object[] objArray : results) {
            String dbExecuteStartTime = (String) objArray[0];
            String dbExecuteEndTime = (String) objArray[1];

            boolean checkResult = checkExecuteTime(dbExecuteStartTime, dbExecuteEndTime, executeStartTime, executeEndTime);
            if (!checkResult) return false;
        }

        return true;
    }


    /**
     * 校验时间段是否重复
     *
     * @param dbExecuteStartTime 数据库段开始时间点
     * @param dbExecuteEndTime   数据库点结束时间点
     * @param executeStartTime   待检测点开始时间点
     * @param executeEndTime     待检测点结束时间点
     * @return 校验结果
     */
    private boolean checkExecuteTime(String dbExecuteStartTime, String dbExecuteEndTime, String executeStartTime, String executeEndTime) {
        int dbExecuteStartIntTime = getExecuteTime(dbExecuteStartTime);
        int dbExecuteEndIntTime = getExecuteTime(dbExecuteEndTime);
        int executeStartIntTime = getExecuteTime(executeStartTime);
        int executeEndIntTime = getExecuteTime(executeEndTime);

        if (dbExecuteStartIntTime <= executeEndIntTime && dbExecuteEndIntTime >= executeStartIntTime)
            return false;
        else
            return true;
    }

    /**
     * 将时间点转换为 long
     *
     * @param executeTime 时间点
     * @return 返回结果
     */
    private int getExecuteTime(String executeTime) {
        int executeIntTime;
        List<String> executeTimes = Splitter.on(SystemConst.REDIS_KEY_SEPARATOR_SYMBOL)
                .omitEmptyStrings()
                .trimResults()
                .splitToList(executeTime);

        int hour = Integer.parseInt(executeTimes.get(0));
        int minute = Integer.parseInt(executeTimes.get(0));

        executeIntTime = hour * 60 * 60 + minute * 60;
        return executeIntTime;
    }

}