package com.pemass.service.pemass.poi.impl;

import com.pemass.persist.domain.jpa.poi.UserPointDetail;
import com.pemass.persist.enumeration.PointChannelEnum;
import com.pemass.persist.enumeration.PointTypeEnum;
import com.pemass.service.pemass.poi.UserPointDetailService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.Date;

@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:applicationContext-persist.xml"})
public class UserPointDetailServiceImplTest extends AbstractTestNGSpringContextTests {

    @Resource
    private UserPointDetailService userPointDetailService;


    @Test
    public void testInsert() {
        UserPointDetail userPointDetail = new UserPointDetail();
        userPointDetail.setPointType(PointTypeEnum.E);
        userPointDetail.setPointPurchaseId(4L);
        userPointDetail.setAmount(100);
        userPointDetail.setArea("00:00:00:00:4");
        userPointDetail.setExpiryTime(new Date());
        userPointDetail.setPointChannel(PointChannelEnum.PURCHASE_GOODS);
        userPointDetail.setUseableAmount(100);
        userPointDetail.setUserId(1L);
        userPointDetail.setOrganizationId(6L);
        userPointDetail = userPointDetailService.insert(userPointDetail);
        System.out.println(userPointDetail.getId());

    }

    @Test
    public void testGetUserPointAmountByPointPoolId() {
        Integer amount = userPointDetailService.getUserPointAmountByPointPoolId(1L);
        System.out.println();
        System.out.println(amount + "-------------");
    }

    @Test
    public void testGetExpiredAmountByPointPoolId() {
        Integer amount = userPointDetailService.getExpiredAmountByPointPoolId(1L);
        System.out.println();
        System.out.println(amount + "++++++++++===");
    }

}