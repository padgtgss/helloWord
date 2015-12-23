package com.pemass.service.pemass.poi.impl;

import com.pemass.persist.domain.jpa.poi.Present;
import com.pemass.persist.enumeration.PointTypeEnum;
import com.pemass.service.pemass.poi.PresentService;
import common.AbstractPemassServiceTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.annotation.Resource;

public class PresentServiceImplTest extends AbstractPemassServiceTest {

    @Resource
    private PresentService presentService;

    @Test
    public void testSelectReceivePresentById() throws Exception {
    }

    @Test
    public void testSavePresent() throws Exception {

    }

    @Test
    public void testGetById() throws Exception {

    }

    @Test
    public void testGetNoneIssuePresentCountByOrganizationId() throws Exception {

    }

    @Test
    public void testSelectPresentByPackId() throws Exception {

    }

    @Test
    public void testSelectPack() throws Exception {

    }

    @Test
    public void testGrant() throws Exception {
        boolean result = presentService.grant(52L, "18628220680");
        Assert.assertTrue(result);
    }

    @Test
    public void testSelectHasReceivePresentByOrganizationId() throws Exception {

    }

    @Test
    public void testPosttingDomainPage() throws Exception {

    }

    @Test
    public void testPackAndGrant() throws Exception {

    }

    @Test
    public void testPack() throws Exception {
        Present present = presentService.pack(6L, "测试红包", PointTypeEnum.P, 10, 1, null, "haha");
        Assert.assertNotNull(present);
    }

    @Test
    public void testGetPresentItemsByPresentId() throws Exception {

    }

    @Test
    public void testUnpack() throws Exception {
       presentService.unpack(52L);
    }

    @Test
    public void testComparison() throws Exception {

    }

    @Test
    public void testUpdatePresentExpiryTime() throws Exception {

    }
}
