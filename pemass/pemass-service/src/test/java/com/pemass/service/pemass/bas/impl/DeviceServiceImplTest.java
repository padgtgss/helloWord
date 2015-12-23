package com.pemass.service.pemass.bas.impl;

import com.pemass.persist.enumeration.DeviceTypeEnum;
import com.pemass.service.pemass.bas.DeviceService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2015/9/23.
 */

@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:applicationContext-persist.xml"})
public class DeviceServiceImplTest extends AbstractTestNGSpringContextTests {

    @Resource
    private DeviceService deviceService;

    @Test
    public void testGetDownloadAmountByType() throws Exception {
        Long amount = deviceService.getDownloadAmountByType(DeviceTypeEnum.ANDROID);
        System.out.println("==========安卓的下载量："+amount);

        amount = deviceService.getDownloadAmountByType(DeviceTypeEnum.IOS);
        System.out.println("==========IOS的下载量："+amount);
    }
}