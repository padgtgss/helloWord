package com.pemass.service.pemass.sys;

import com.pemass.common.server.dao.BaseDao;
import com.pemass.persist.domain.jpa.poi.Present;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;

@ContextConfiguration(locations = {"classpath*:applicationContext.xml",
        "/persist.xml",
        "classpath*:applicationContext-jms.xml"})
public class AccountServiceTest extends AbstractTestNGSpringContextTests {

    @Resource
    private BaseDao jpaBaseDao;

    @Test
    public void testRegister() throws Exception {

        Present present = jpaBaseDao.getEntityById(Present.class, 2400L);

        System.out.println(present);

        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(present));

    }
}