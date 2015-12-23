package com.pemass.service.pemass.poi.impl;

import com.pemass.persist.enumeration.PointTypeEnum;
import com.pemass.service.pemass.poi.OrganizationPointDetailService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/21.
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:applicationContext-persist.xml"})
public class OrganizationPointDetailServiceImplTest extends AbstractTestNGSpringContextTests {

    @Resource
    private OrganizationPointDetailService organizationPointDetailService;

    @Test
    public void testGetUseableAmount() {
        Map<String, Object> fieldMap = new HashMap<String, Object>();
        fieldMap.put("organizationId", 1L);
        fieldMap.put("pointType", PointTypeEnum.E);
        Long amount = organizationPointDetailService.getUseableAmount(fieldMap);
        System.out.println(amount + "-----------");
    }

    @Test
    public void testGetPointAmountByPointPoolId() {
        Integer amount = organizationPointDetailService.getPointAmountByPointPoolId(5L);
        System.out.println("+++++++++++++++++++" + amount);
    }
}