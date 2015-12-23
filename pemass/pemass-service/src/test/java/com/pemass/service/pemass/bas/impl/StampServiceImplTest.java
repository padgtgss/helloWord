package com.pemass.service.pemass.bas.impl;

import com.pemass.persist.dao.bas.StampDao;
import com.pemass.service.pemass.bas.StampService;
import common.AbstractPemassServiceTest;
import org.testng.annotations.Test;

import javax.annotation.Resource;

/**
 * Created by estn on 15/8/20.
 */
public class StampServiceImplTest  extends AbstractPemassServiceTest{

    @Resource
    private StampService stampService;

    @Resource
    private StampDao stampDao;

    @Test
    public void testVerifyApiStamp() throws Exception {
        stampService.verifyApiStamp("123");
    }

    @Test
    public void testSetPortalStamp() throws Exception {
        stampService.setPortalStamp("aaa");
    }

    @Test
    public void testVerifyPortalStamp() throws Exception {
        stampService.verifyPortalStamp("aaa11");
    }

    @Test
    public void test001() {
        System.out.println(stampDao.incPortalStampValue("1111"));
        System.out.println();
    }

    public static void main(String[] args) {

    }
}