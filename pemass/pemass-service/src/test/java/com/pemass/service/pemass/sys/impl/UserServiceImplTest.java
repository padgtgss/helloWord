package com.pemass.service.pemass.sys.impl;

import com.pemass.service.pemass.sys.UserService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2015/9/23.
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:applicationContext-persist.xml"})
public class UserServiceImplTest extends AbstractTestNGSpringContextTests {

    @Resource
    private UserService userService;

    @Test
    public void testGetRegisterUserAmount() throws Exception {
        Long amount = userService.getRegisterUserAmount();
        System.out.println("++++++++所有注册用户数量：" + amount);
    }

    @Test
    public void testGetAddUsersAmountByDay() throws Exception {
        Long amount = userService.getAddUsersAmountByDay();
        System.out.println("++++++++当日新增用户总量：" + amount);
    }
}