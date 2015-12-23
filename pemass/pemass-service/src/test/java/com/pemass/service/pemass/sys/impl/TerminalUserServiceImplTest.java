package com.pemass.service.pemass.sys.impl;

import com.pemass.service.pemass.sys.TerminalUserService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;

@ContextConfiguration(locations = {"classpath*:applicationContext.xml",
        "classpath:applicationContext-persist.xml"})
public class TerminalUserServiceImplTest extends AbstractTestNGSpringContextTests {

    @Resource
    private TerminalUserService terminalUserService;
    @Test
    public void testGetAllCashierIdByField() throws Exception {
       /* Long organizationId = 12L;
        String isDistribution = "n";
        List result = terminalUserService.getAllCashierIdByField(organizationId,isDistribution);
        System.out.println(result.size()+"----------------------");*/
    }
}