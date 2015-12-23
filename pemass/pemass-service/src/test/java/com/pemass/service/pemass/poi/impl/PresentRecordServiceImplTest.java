package com.pemass.service.pemass.poi.impl;

import com.pemass.common.core.pojo.DomainPage;
import com.pemass.service.pemass.poi.PresentRecordService;
import common.AbstractPemassServiceTest;
import org.testng.annotations.Test;

import javax.annotation.Resource;

/**
 * Created by estn on 15/8/5.
 */
public class PresentRecordServiceImplTest extends AbstractPemassServiceTest {

    @Resource
    private PresentRecordService presentRecordService;

    @Test
    public void testGetByidList() throws Exception {
        DomainPage domainPage = presentRecordService.selectByFromUserId(7L, 1, 10);
        System.out.println(domainPage);
    }
}