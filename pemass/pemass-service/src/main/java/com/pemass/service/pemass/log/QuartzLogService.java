package com.pemass.service.pemass.log;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.mongo.log.QuartzLogMessage;

import java.util.Map;

/**
 * @Description: QuartzLogService
 * @Author: xy
 * @CreateTime: 2015-07-31 15:18
 */
public interface QuartzLogService  {

    /**
     * 插入一条日志
     * @param quartzLogMessage
     * @return
     */
    boolean insert(QuartzLogMessage quartzLogMessage);

    /**
     * 查询日志
     * @param conditions
     * @param fuzzyConditions
     * @param domainPage
     * @return
     */
    DomainPage selectLogByConditions(Map<String, Object> conditions, Map<String, Object> fuzzyConditions, DomainPage domainPage);
}
