package com.pemass.service.pemass.poi.impl;

import com.pemass.persist.domain.jpa.poi.PointPoolOrganization;
import com.pemass.service.pemass.poi.PointPoolOrganizationService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2015/7/20.
 */
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:applicationContext-persist.xml"})
public class PointPoolOrganizationServiceImplTest extends AbstractTestNGSpringContextTests {

    @Resource
    private PointPoolOrganizationService pointPoolOrganizationService;

    @Test
    public void testSelectAllPointPoolOrganization() {
        List<PointPoolOrganization> list = pointPoolOrganizationService.selectAllPointPoolOrganization();
        System.out.println(list.size());
    }
}