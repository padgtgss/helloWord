package com.pemass.service.pemass.log.impl;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.mongo.log.QuartzLogMessage;
import com.pemass.persist.domain.mongo.push.PushMessage;
import com.pemass.service.pemass.log.QuartzLogService;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: QuartzLogServiceImpl
 * @Author: xy
 * @CreateTime: 2015-07-31 15:19
 */
@Service
public class QuartzLogServiceImpl implements QuartzLogService {
    @Resource
    private Datastore morphiaDatastore;

    @Transactional
    @Override
    public boolean insert(QuartzLogMessage quartzLogMessage) {
        if(quartzLogMessage == null){
            return false;
        }

        morphiaDatastore.save(quartzLogMessage);
        System.out.println("======插入成功=======");
        return true;
    }

    @Override
    public DomainPage selectLogByConditions(Map<String, Object> conditions, Map<String, Object> fuzzyConditions, DomainPage domainPage) {
        Query<QuartzLogMessage> query = morphiaDatastore.createQuery(QuartzLogMessage.class);

        this.setCriteria(conditions, fuzzyConditions, domainPage, query);
        List<QuartzLogMessage> quartzLogMessages = query.asList();
        Long totalCount = query.countAll();

        DomainPage dp = new DomainPage(domainPage.getPageSize(), domainPage.getPageIndex(), totalCount);
        dp.getDomains().addAll(quartzLogMessages);
        return dp;
    }
    private void setCriteria(Map<String, Object> conditions, Map<String, Object> fuzzyConditions, DomainPage domainPage, Query<QuartzLogMessage> query) {
        /*-- 精确条件 --*/
        if (conditions != null) {
            Set<String> keySet = conditions.keySet();
            Iterator<String> keySetIterator = keySet.iterator();
            while (keySetIterator.hasNext()) {
                String key = keySetIterator.next();
                query.filter(key, conditions.get(key));
            }
        }

        /*-- 模糊条件 --*/
        if (fuzzyConditions != null) {
            Set<String> keySet = fuzzyConditions.keySet();
            Iterator<String> keySetIterator = keySet.iterator();
            while (keySetIterator.hasNext()) {
                String key = keySetIterator.next();
                query.field(key).contains(fuzzyConditions.get(key).toString());
            }
        }

        /*-- 分页条件 --*/
        Long offset = (domainPage.getPageIndex() - 1) * domainPage.getPageSize();
        Long pageSize = domainPage.getPageSize();
        query.offset(Integer.parseInt(offset.toString())).limit(Integer.parseInt(pageSize.toString()));

        /*-- 排序条件 --*/
        query.order("-createTime");
    }
}
