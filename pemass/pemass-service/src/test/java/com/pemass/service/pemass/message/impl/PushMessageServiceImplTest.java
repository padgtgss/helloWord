package com.pemass.service.pemass.message.impl;

import com.google.common.collect.Maps;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.persist.domain.mongo.push.UserPushMessage;
import com.pemass.service.pemass.log.QuartzLogService;
import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.Map;

@ContextConfiguration(locations = {"classpath:applicationContext.xml",
        "classpath:applicationContext-persist.xml"})
public class PushMessageServiceImplTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private Datastore morphiaDatastore;

    @Resource
    private QuartzLogService quartzLogService;

    @Test
    public void testSelectByUid() throws Exception {
//        long messageCount = morphiaDatastore.getCount(UserPushMessage.class);
//        System.out.println(messageCount);
        Map<String, Object> conditions = Maps.newHashMap();
        Map<String, Object> fuzzyConditions = Maps.newHashMap();
        DomainPage dp = new DomainPage(1L,1L,1L);
        DomainPage domainPage = quartzLogService.selectLogByConditions(conditions,fuzzyConditions,dp);

    }
}