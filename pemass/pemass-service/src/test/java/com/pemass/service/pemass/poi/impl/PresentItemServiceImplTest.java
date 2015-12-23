package com.pemass.service.pemass.poi.impl;

import com.pemass.persist.enumeration.PresentItemTypeEnum;
import com.pemass.service.pemass.poi.PresentItemService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;


@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:applicationContext-persist.xml"})
public class PresentItemServiceImplTest extends AbstractTestNGSpringContextTests {
    @Resource
    private PresentItemService presentItemService;

    @Test
    public void testGetPresentItemAmountByPresentPackId() throws Exception {
        Integer amount = presentItemService.getPresentItemAmountByPresentPackId(17L);
        System.out.println("--------红包项的数量："+amount);
    }

    @Test
    public void testGetTotalPointByPresentPackId() throws Exception {
        Integer totalPointP = presentItemService.getTotalPointByPresentPackId(17L, PresentItemTypeEnum.P);
        Integer totalPointE = presentItemService.getTotalPointByPresentPackId(17L, PresentItemTypeEnum.E);
        System.out.println("-----消耗的E积分总数："+totalPointP);
        System.out.println("-----消耗的E积分总数："+totalPointE);
    }
}