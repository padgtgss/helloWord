package com.pemass.service.pemass.biz.impl;

import com.pemass.service.pemass.biz.UserExpresspayCardService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;

@ContextConfiguration(locations = {"classpath*:applicationContext.xml","classpath*:applicationContext-persist.xml"})
public class UserExpresspayCardServiceImplTest extends AbstractTestNGSpringContextTests {

    @Resource
    private UserExpresspayCardService expresspayCardService;


    @Test
    public void testBindCard()throws Exception{

        expresspayCardService.bindExpresspayCard(1L,"23456789005");

    }


}